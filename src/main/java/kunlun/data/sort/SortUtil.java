/*
 * Copyright (c) 2019. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.sort;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 排序工具集门面：统一持有/管理各类排序目标的 {@link Sorter} 实现.
 * <p>按"多实现共存"设计（区别于单默认实现的门面）：SPI（META-INF/services/kunlun.data.sort.Sorter）
 * 发现全部实现，{@link #applySort(Object, Class, List, Map)} 按排序目标类型（{@link Sorter#supports}）
 * 路由到对应实现，同一应用可同时挂 MyBatis-Plus 与 ES 等多套实现。
 * <p>路由规则：按注册顺序的逆序匹配<b>唯一</b>支持的实现执行；同一排序目标不允许有多个激活的实现，
 * 多命中直接抛 {@link IllegalStateException}（多个实现对同一目标做的是同一件事，执行任一都是
 * "静默替调用者做决定"，防止"以为走的是自己注册的实现，实际走了另一个"）；
 * 需要替换实现时先 {@link #unregisterSorter(Class)} 旧实现再 {@link #registerSorter(Sorter)} 新实现
 * （显式接管），或不经注册表、用 {@link #getSorter(Class)} 显式获取。
 *
 * @author Kahle
 */
public final class SortUtil {
    private static final Logger log = LoggerFactory.getLogger(SortUtil.class);
    private static final Map<Class<? extends Sorter>, Sorter> SORTER_MAP = new ConcurrentHashMap<Class<? extends Sorter>, Sorter>();
    private static final List<Sorter> SORTERS = new CopyOnWriteArrayList<Sorter>();
    private static volatile boolean initialized = false;


    // region ======== 注册与注销 ========

    /**
     * 注册排序器（显式指定 key，同 key 重复注册时替换旧实例）.
     *
     * @param key 实现类（注册 key）
     * @param instance 实现实例
     * @return 入参实例
     */
    public static Sorter registerSorter(Class<? extends Sorter> key, Sorter instance) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Assert.notNull(instance, "Parameter \"instance\" must not null. ");
        init();
        SORTER_MAP.put(key, instance);
        removeByClass(SORTERS, key);
        SORTERS.add(instance);
        return instance;
    }

    /**
     * 注册排序器（按实例类型作 key）.
     *
     * @param instance 实现实例
     * @return 入参实例
     */
    public static Sorter registerSorter(Sorter instance) {
        Assert.notNull(instance, "Parameter \"instance\" must not null. ");
        return registerSorter(instance.getClass(), instance);
    }

    /**
     * 注销排序器（映射与路由列表同步移除）.
     *
     * @param implClass 实现类
     */
    public static void unregisterSorter(Class<? extends Sorter> implClass) {
        Assert.notNull(implClass, "Parameter \"implClass\" must not null. ");
        SORTER_MAP.remove(implClass);
        removeByClass(SORTERS, implClass);
    }

    // endregion


    // region ======== 访问 ========

    /**
     * 取指定实现：已注册/缓存则直接返回，否则反射无参构造并缓存.
     * <p>用于确定性获取某一存储的实现（不受注册顺序影响），如
     * {@code getSorter(MyBatisPlusSorter.class)}。
     *
     * @param implClass 实现类
     * @return 该实现类的实例（同实现类恒为同一实例）
     */
    public static Sorter getSorter(Class<? extends Sorter> implClass) {
        Assert.notNull(implClass, "Parameter \"implClass\" must not null. ");
        Sorter sorter = SORTER_MAP.get(implClass);
        if (sorter != null) { return sorter; }
        try {
            sorter = implClass.newInstance();
        }
        catch (Exception e) {
            throw new IllegalStateException("SortUtil: cannot instantiate sorter " + implClass, e);
        }
        // 双重检查缓存：进锁后再取一次，仍无则写入并返回
        synchronized (SortUtil.class) {
            Sorter prev = SORTER_MAP.get(implClass);
            if (prev != null) { return prev; }
            SORTER_MAP.put(implClass, sorter);
            return sorter;
        }
    }

    /**
     * 取全部已注册的排序器（快照，按注册顺序）.
     *
     * @return 排序器列表（不可变）
     */
    public static List<Sorter> getSorters() {
        init();
        return Collections.unmodifiableList(new ArrayList<Sorter>(SORTERS));
    }

    // endregion


    // region ======== 执行型（路由 + 委托） ========

    /**
     * 应用自定义排序（无扩展选项的便捷重载）.
     *
     * @param target 排序目标对象
     * @param entityClass 主实体类型
     * @param sortFields 调用方传入的排序参数（可为空）
     * @return 有任意一项排序实际生效返回 true
     */
    public static boolean applySort(Object target, Class<?> entityClass, List<SortField> sortFields) {

        return applySort(target, entityClass, sortFields, null);
    }

    /**
     * 应用自定义排序：按排序目标类型路由到支持的 {@link Sorter} 执行.
     * <p>未传排序参数时直接返回；没有任何实现支持该排序目标时告警并忽略；
     * 多个实现同时支持时直接抛 {@link IllegalStateException}——同一排序目标只允许一个激活的实现，
     * 替换实现请先注销旧的再注册新的（见类注释的路由规则）.
     *
     * @param target 排序目标对象
     * @param entityClass 主实体类型
     * @param sortFields 调用方传入的排序参数（可为空）
     * @param options 透传给排序器的通用扩展选项（命名选项包，可为 null，语义见 {@link Sorter#applySort}；
     *                kunlun 的 Dict 即 Map，可直接作为选项包传入）
     * @return 有任意一项排序实际生效返回 true
     */
    public static boolean applySort(Object target, Class<?> entityClass, List<SortField> sortFields, Map<String, Object> options) {
        if (target == null || sortFields == null || sortFields.isEmpty()) { return false; }
        init();
        // 按注册顺序的逆序收集全部支持的实现：多于一个即是注册表状态冲突
        List<Sorter> matches = null;
        List<Sorter> sorters = SORTERS;
        for (int i = sorters.size() - 1; i >= 0; i--) {
            Sorter sorter = sorters.get(i);
            if (!sorter.supports(target)) { continue; }
            if (matches == null) { matches = new ArrayList<Sorter>(); }
            matches.add(sorter);
        }
        if (matches == null) {
            log.warn("SortUtil: no Sorter supports target [{}], sort fields ignored", target.getClass().getName());
            return false;
        }
        if (matches.size() > 1) {
            throw new IllegalStateException("SortUtil: " + matches.size() + " Sorters all support target ["
                    + target.getClass().getName() + "], only one active sorter per target type is allowed; "
                    + "candidates: " + matches + ". "
                    + "Unregister the redundant one or resolve explicitly via getSorter(Class). ");
        }
        return matches.get(0).applySort(target, entityClass, sortFields, options);
    }

    // endregion


    // region ======== 私有的辅助方法 ========

    /**
     * 移除列表中所有指定类型（按 Class 身份）的排序器（倒序索引删除，兼容 CopyOnWriteArrayList）.
     */
    private static void removeByClass(List<Sorter> sorters, Class<?> implClass) {
        for (int i = sorters.size() - 1; i >= 0; i--) {
            if (sorters.get(i).getClass() == implClass) { sorters.remove(i); }
        }
    }

    /**
     * 经 SPI 发现并注册全部实现（仅一次；手动注册会先于 SPI 注册，配合逆序路由即手动注册优先）.
     * <p>依次经多个 ClassLoader（线程上下文 / 接口自身 / 系统）用 ServiceLoader 发现并按实现类去重；
     * 全部来源都未发现时，再兜底用 getResource 直读 SPI 文件手动解析——部分托管类加载器环境
     * （如 JDK8 的 meta-index 优化叠加旧版 surefire/容器 booter）下 getResources 枚举会失效，
     * 表现为 ServiceLoader 找不到实现但 getResource 能定位到注册文件。
     */
    private static void init() {
        if (initialized) { return; }
        synchronized (SortUtil.class) {
            if (initialized) { return; }
            Set<ClassLoader> loaders = new LinkedHashSet<ClassLoader>();
            ClassLoader tccl = Thread.currentThread().getContextClassLoader();
            if (tccl != null) { loaders.add(tccl); }
            ClassLoader interfaceLoader = Sorter.class.getClassLoader();
            if (interfaceLoader != null) { loaders.add(interfaceLoader); }
            ClassLoader systemLoader = ClassLoader.getSystemClassLoader();
            if (systemLoader != null) { loaders.add(systemLoader); }
            for (ClassLoader loader : loaders) {
                try {
                    for (Sorter sorter : ServiceLoader.load(Sorter.class, loader)) {
                        registerDiscovered(sorter);
                    }
                }
                catch (Throwable e) {
                    log.debug("SortUtil: SPI discovery failed for loader [" + loader + "]", e);
                }
            }
            if (SORTERS.isEmpty()) {
                for (ClassLoader loader : loaders) { discoverManually(loader); }
            }
            if (SORTERS.isEmpty()) {
                log.debug("SortUtil: no Sorter found via SPI; "
                        + "register via registerSorter(...) or add META-INF/services/{} ", Sorter.class.getName());
            }
            initialized = true;
        }
    }

    /**
     * 注册 SPI 发现的实现：已被 getSorter(Class) 提前实例化并缓存的实现，也要晋升进路由列表.
     */
    private static void registerDiscovered(Sorter sorter) {
        Sorter prev = SORTER_MAP.putIfAbsent(sorter.getClass(), sorter);
        Sorter effective = prev != null ? prev : sorter;
        if (!SORTERS.contains(effective)) { SORTERS.add(effective); }
    }

    /**
     * 兜底发现：直读 SPI 注册文件逐行解析实现类并实例化（行内 # 注释与空白行跳过）.
     */
    private static void discoverManually(ClassLoader loader) {
        if (loader == null) { return; }
        String path = "META-INF/services/" + Sorter.class.getName();
        URL url = loader.getResource(path);
        if (url == null) { return; }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                int comment = line.indexOf('#');
                if (comment >= 0) { line = line.substring(0, comment); }
                line = line.trim();
                if (line.isEmpty()) { continue; }
                Class<?> implClass = Class.forName(line, true, loader);
                if (!Sorter.class.isAssignableFrom(implClass)) { continue; }
                registerDiscovered((Sorter) implClass.newInstance());
            }
        }
        catch (Throwable e) {
            log.debug("SortUtil: manual SPI discovery failed for [" + url + "]", e);
        }
        finally {
            if (reader != null) {
                try { reader.close(); } catch (IOException ignored) { }
            }
        }
    }

    // endregion


    // region ======== 私有的构造方法 ========

    private SortUtil() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

    // endregion

}
