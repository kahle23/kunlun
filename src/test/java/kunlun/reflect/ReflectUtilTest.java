/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.reflect;

import kunlun.cache.support.SimpleCache;
import kunlun.data.bean.support.FieldBasedBeanMap;
import kunlun.data.paging.support.SerialNumberFillerImpl;
import kunlun.exception.UncheckedException;
import kunlun.reflect.support.CacheReflectService;
import kunlun.reflect.support.JdkReflectService;
import kunlun.test.pojo.entity.system.User;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * ReflectUtil / JdkReflectService 测试。
 * @author Kahle
 */
public class ReflectUtilTest {

    // ==================== 测试夹具 ====================

    public interface Const {
        String CONST = "const-value";
    }

    public static class Parent {
        protected String parentProtected = "pp";
        private String parentPrivate = "pri";
        public String parentPublic = "pub";
        private String shadow = "parent-shadow";

        public String parentGreet() { return "parent"; }

        private String parentSecret() { return "secret"; }
    }

    public static class Child extends Parent implements Const {
        private String childName = "child";
        private String shadow = "child-shadow";
        public static String staticValue = "sv";

        public String childGreet() { return "child"; }

        private String childSecret(String msg) { return "secret-" + msg; }

        public int add(int a, int b) { return a + b; }

        public String greet(String name) { return "hello " + name; }

        public static String staticHello(String who) { return "hello-static " + who; }

        public String crash() { throw new IllegalStateException("crash"); }

        public String boom() throws Exception { throw new Exception("boom-checked"); }
    }

    public static class PrivateCtor {
        public String name = "pc";

        private PrivateCtor() { }
    }

    public static class Pair {
        public int a;
        public int b;

        public Pair(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }

    private static class CountingCache extends SimpleCache {
        private int putCount = 0;

        @Override
        public Object put(Object key, Object value) {
            putCount++;
            return super.put(key, value);
        }
    }

    // ==================== 字段 ====================

    @Test
    public void testGetAllFields() {
        Field[] fields = ReflectUtil.getAllFields(Child.class);
        Set<String> names = new HashSet<String>();
        for (Field field : fields) { names.add(field.getName()); }
        // 无重复
        assertEquals(fields.length, names.size());
        // 本类私有、父类 protected/public/private 均收录
        assertTrue(names.contains("childName"));
        assertTrue(names.contains("parentProtected"));
        assertTrue(names.contains("parentPublic"));
        assertTrue(names.contains("parentPrivate"));
        // User 共 11 个字段
        assertEquals(11, ReflectUtil.getAllFields(User.class).length);
    }

    @Test
    public void testGetFieldVariants() {
        assertNotNull(ReflectUtil.getField(Child.class, "childName"));
        assertNotNull(ReflectUtil.getField(Child.class, "parentProtected"));
        // 父类 private 字段同样返回（已取消访问限制）
        Field parentPrivate = ReflectUtil.getField(Child.class, "parentPrivate");
        assertNotNull(parentPrivate);
        assertEquals(Parent.class, parentPrivate.getDeclaringClass());
        // 接口常量走 public 主路径
        assertNotNull(ReflectUtil.getField(Child.class, "CONST"));
        assertEquals("const-value", ReflectUtil.getFieldValue(new Child(), "CONST"));
        // getField 未命中返回 null；getFieldOrThrow 未命中抛异常且带原始 cause
        assertNull(ReflectUtil.getField(Child.class, "nope"));
        try {
            ReflectUtil.getFieldOrThrow(Child.class, "nope");
            fail();
        }
        catch (UncheckedException e) {
            assertTrue(e.getCause() instanceof NoSuchFieldException);
        }
        // hasField 与 getField 的命中/未命中语义一致（含父类 private、接口常量）
        assertTrue(ReflectUtil.hasField(Child.class, "childName"));
        assertTrue(ReflectUtil.hasField(Child.class, "parentProtected"));
        assertTrue(ReflectUtil.hasField(Child.class, "parentPrivate"));
        assertTrue(ReflectUtil.hasField(Child.class, "CONST"));
        assertFalse(ReflectUtil.hasField(Child.class, "nope"));
    }

    @Test
    public void testSameNameFieldShadowing() {
        // 子类与父类存在同名字段：getField 与 getAllFields 均以子类为准，父类同名字段被遮蔽
        Field field = ReflectUtil.getField(Child.class, "shadow");
        assertNotNull(field);
        assertEquals(Child.class, field.getDeclaringClass());
        int count = 0;
        for (Field f : ReflectUtil.getAllFields(Child.class)) {
            if ("shadow".equals(f.getName())) {
                count++;
                assertEquals(Child.class, f.getDeclaringClass());
            }
        }
        assertEquals(1, count);
        assertEquals("child-shadow", ReflectUtil.getFieldValue(new Child(), "shadow"));
    }

    @Test
    public void testFieldValue() {
        Child child = new Child();
        assertEquals("child", ReflectUtil.getFieldValue(child, "childName"));
        assertEquals("pp", ReflectUtil.getFieldValue(child, "parentProtected"));
        ReflectUtil.setFieldValue(child, "childName", "x");
        assertEquals("x", ReflectUtil.getFieldValue(child, "childName"));
        ReflectUtil.setFieldValue(child, "parentProtected", "y");
        assertEquals("y", ReflectUtil.getFieldValue(child, "parentProtected"));
        // 父类 private 字段可直接读写
        assertEquals("pri", ReflectUtil.getFieldValue(child, "parentPrivate"));
        ReflectUtil.setFieldValue(child, "parentPrivate", "p2");
        assertEquals("p2", ReflectUtil.getFieldValue(child, "parentPrivate"));
        // 静态字段：target 传 Class 或实例均可
        assertEquals("sv", ReflectUtil.getFieldValue(Child.class, "staticValue"));
        ReflectUtil.setFieldValue(Child.class, "staticValue", "sv2");
        assertEquals("sv2", ReflectUtil.getFieldValue(new Child(), "staticValue"));
        // 字段不存在
        try {
            ReflectUtil.getFieldValue(child, "nope");
            fail();
        }
        catch (UncheckedException e) {
            assertTrue(e.getCause() instanceof NoSuchFieldException);
        }
    }

    // ==================== 方法 ====================

    @Test
    public void testGetAllMethods() {
        Method[] methods = ReflectUtil.getAllMethods(Child.class);
        Set<String> names = new HashSet<String>();
        for (Method method : methods) { names.add(method.getName()); }
        assertTrue(names.contains("childGreet"));
        assertTrue(names.contains("parentGreet"));
        // 父类 private 方法同样收录；Object 继承方法收录
        assertTrue(names.contains("parentSecret"));
        assertTrue(names.contains("toString"));
    }

    @Test
    public void testGetMethodVariants() {
        // getMethod：未命中返回 null；命中精确匹配
        assertNull(ReflectUtil.getMethod(Child.class, "nope"));
        assertNotNull(ReflectUtil.getMethod(Child.class, "add", int.class, int.class));
        // getMethodOrThrow：未命中抛异常
        try {
            ReflectUtil.getMethodOrThrow(Child.class, "nope");
            fail();
        }
        catch (UncheckedException e) {
            assertTrue(e.getCause() instanceof NoSuchMethodException);
        }
        // getSimilarMethod：实参为包装类型时经相似匹配命中 int 形参；未命中返回 null
        assertNotNull(ReflectUtil.getSimilarMethod(Child.class, "add", Integer.class, Integer.class));
        assertNull(ReflectUtil.getSimilarMethod(Child.class, "nope", Integer.class, Integer.class));
        // getSimilarMethodOrThrow：未命中抛异常
        try {
            ReflectUtil.getSimilarMethodOrThrow(Child.class, "nope", Integer.class, Integer.class);
            fail();
        }
        catch (UncheckedException e) {
            assertTrue(e.getCause() instanceof NoSuchMethodException);
        }
    }

    // ==================== 构造与实例化 ====================

    @Test
    public void testNewInstance() {
        // 私有构造器可实例化
        PrivateCtor pc = ReflectUtil.newInstance(PrivateCtor.class);
        assertEquals("pc", pc.name);
        // 按实参匹配构造器（Integer 实参匹配 int 形参）
        Pair pair = ReflectUtil.newInstance(Pair.class, 1, 2);
        assertEquals(1, pair.a);
        assertEquals(2, pair.b);
        // 找不到构造器：getConstructor 返回 null，newInstance（内部 OrThrow）抛异常
        assertNull(ReflectUtil.getConstructor(User.class, String.class));
        try {
            ReflectUtil.newInstance(User.class, new Object());
            fail();
        }
        catch (UncheckedException e) {
            assertTrue(e.getCause() instanceof NoSuchMethodException);
        }
        assertTrue(ReflectUtil.getConstructors(Child.class).length > 0);
    }

    // ==================== 属性 ====================

    @Test
    public void testGetPropertyDescriptors() {
        PropertyDescriptor[] descriptors = ReflectUtil.getPropertyDescriptors(User.class);
        // 11 个业务属性 + "class"
        assertEquals(12, descriptors.length);
        Set<String> names = new HashSet<String>();
        for (PropertyDescriptor descriptor : descriptors) { names.add(descriptor.getName()); }
        assertTrue(names.contains("name"));
        assertTrue(names.contains("nickname"));
    }

    // ==================== 操作 ====================

    @Test
    public void testInvoke() {
        Child child = new Child();
        // 基本类型与包装类型互通
        assertEquals(Integer.valueOf(3), ReflectUtil.invoke(child, "add", 1, 2));
        // 私有方法
        assertEquals("secret-m", ReflectUtil.invoke(child, "childSecret", "m"));
        // null 实参可匹配
        assertEquals("hello null", ReflectUtil.invoke(child, "greet", (Object) null));
        // 静态方法：invokeStatic 与 target 传 Class
        assertEquals("hello-static k", ReflectUtil.invokeStatic(Child.class, "staticHello", "k"));
        assertEquals("hello-static k", ReflectUtil.invoke(Child.class, "staticHello", "k"));
        // 目标方法抛运行时异常：原样抛出
        try {
            ReflectUtil.invoke(child, "crash");
            fail();
        }
        catch (IllegalStateException e) {
            assertEquals("crash", e.getMessage());
        }
        // 目标方法抛受检异常：包装后抛出，cause 为业务异常
        try {
            ReflectUtil.invoke(child, "boom");
            fail();
        }
        catch (UncheckedException e) {
            assertNotNull(e.getCause());
            assertEquals("boom-checked", e.getCause().getMessage());
        }
        // 方法不存在
        try {
            ReflectUtil.invoke(child, "nope");
            fail();
        }
        catch (UncheckedException e) {
            assertTrue(e.getCause() instanceof NoSuchMethodException);
        }
    }

    // ==================== 继承场景（公共基类字段） ====================

    /**
     * 模拟"公共出参基类"：字段全部 private 声明在父类、子类只声明业务字段（同 BaseResult1 + Lombok 场景）。
     */
    public static class BaseRow {
        private int serialNumber;
        private String createUser;
        private String createdByName;

        public int getSerialNumber() { return serialNumber; }
    }

    public static class AssetRow extends BaseRow {
        private String assetNo;

        public String getAssetNo() { return assetNo; }
    }

    @Test
    public void testInheritedPrivateFieldOperations() {
        assertNotNull(ReflectUtil.getField(AssetRow.class, "serialNumber"));
        AssetRow row = new AssetRow();
        // 名字版读写操作对父类 private 字段可用
        ReflectUtil.setFieldValue(row, "serialNumber", 7);
        assertEquals(Integer.valueOf(7), ReflectUtil.getFieldValue(row, "serialNumber"));
        ReflectUtil.setFieldValue(row, "createUser", "u1");
        assertEquals("u1", ReflectUtil.getFieldValue(row, "createUser"));
        // 子类自身字段不受影响
        ReflectUtil.setFieldValue(row, "assetNo", "AN-1");
        assertEquals("AN-1", row.getAssetNo());
    }

    @Test
    public void testFieldBasedBeanMapInheritedField() {
        // 数据回填链路（ClassicFiller 的 FieldBasedBeanMap 视图）：父类 private 字段可读（提取查询条件）、可写（回填名称）
        AssetRow row = new AssetRow();
        ReflectUtil.setFieldValue(row, "createUser", "1001");
        FieldBasedBeanMap beanMap = new FieldBasedBeanMap(row);
        assertEquals("1001", beanMap.get("createUser"));
        assertNull(beanMap.get("createdByName"));
        beanMap.put("createdByName", "张三");
        assertEquals("张三", beanMap.get("createdByName"));
        // 回填写入真实落到对象字段上
        assertEquals("张三", ReflectUtil.getFieldValue(row, "createdByName"));
        // 子类字段照常
        beanMap.put("assetNo", "TH0000001");
        assertEquals("TH0000001", row.getAssetNo());
    }

    @Test
    public void testSerialNumberFillerInheritedField() {
        // 序号填充链路：serialNumber 声明在父类 private 时同样可填（第 2 页、每页 10 条）
        List<AssetRow> list = new ArrayList<AssetRow>();
        for (int i = 0; i < 3; i++) { list.add(new AssetRow()); }
        new SerialNumberFillerImpl().fill(list, null, 2, 10);
        assertEquals(11, list.get(0).getSerialNumber());
        assertEquals(12, list.get(1).getSerialNumber());
        assertEquals(13, list.get(2).getSerialNumber());
    }

    // ==================== 门面路由 ====================

    @Test
    public void testFacadeRouting() {
        final String[] lastMethod = new String[1];
        ReflectService stub = (ReflectService) Proxy.newProxyInstance(
                ReflectUtilTest.class.getClassLoader(),
                new Class[]{ReflectService.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) {
                        lastMethod[0] = method.getName();
                        return null;
                    }
                });
        ReflectUtil.setReflectService(stub);
        try {
            assertNull(ReflectUtil.getAllFields(Child.class));
            assertEquals("getAllFields", lastMethod[0]);
            assertNull(ReflectUtil.invoke(new Child(), "add", 1, 2));
            assertEquals("invoke", lastMethod[0]);
        }
        finally {
            ReflectUtil.setReflectService(new CacheReflectService(new JdkReflectService()));
        }
    }

    // ==================== 缓存 ====================

    @Test
    public void testCacheCounting() {
        CountingCache cache = new CountingCache();
        CacheReflectService service = new CacheReflectService(new JdkReflectService(), cache);
        Field[] first = service.getAllFields(Child.class);
        Field[] second = service.getAllFields(Child.class);
        // 元数据仅加载一次，后续命中缓存
        assertEquals(1, cache.putCount);
        assertEquals(first.length, second.length);
        // 对外返回副本，外部修改不污染缓存
        assertNotSame(first, second);
    }

    @Test
    public void testNoCache() {
        // 纯 JDK 实现即无缓存路径
        JdkReflectService service = new JdkReflectService();
        assertEquals(11, service.getAllFields(User.class).length);
        assertEquals(11, service.getAllFields(User.class).length);
        assertNotNull(service.getField(Child.class, "childName"));
        assertEquals("child", service.getFieldValue(new Child(), "childName"));
        assertEquals(Integer.valueOf(3), service.invoke(new Child(), "add", 1, 2));
    }

    @Test
    public void testSetCache() {
        CacheReflectService service = new CacheReflectService(new JdkReflectService());
        assertTrue(service.getDelegate() instanceof JdkReflectService);
        SimpleCache newCache = new SimpleCache();
        service.setCache(newCache);
        assertSame(newCache, service.getCache());
        assertEquals("child", service.getFieldValue(new Child(), "childName"));
    }

    // ==================== 废弃方法冒烟 ====================

    @Test
    @SuppressWarnings("deprecation")
    public void testDeprecatedMethodsSmoke() {
        Child child = new Child();
        assertEquals(ReflectUtil.getAllFields(Child.class).length
                , ReflectUtil.getAccessibleFields(Child.class).length);
        assertTrue(ReflectUtil.getFields(Child.class).length > 0);
        assertTrue(ReflectUtil.getMethods(Child.class).length > 0);
        assertTrue(ReflectUtil.getAccessibleMethods(Child.class).length > 0);
        Field field = ReflectUtil.getField(Child.class, "childName");
        assertNotNull(field);
        // isAccessible 与 JDK 标记的区别：public 字段未调 setAccessible 时 JDK 标记为 false，但天然可访问
        Field publicField = ReflectUtil.getField(Child.class, "parentPublic");
        assertFalse(publicField.isAccessible());
        assertTrue(ReflectUtil.isAccessible(publicField));
        // invoke(Method, target, args) 形式
        List<String> holder = new java.util.ArrayList<String>();
        holder.add("a");
        Method method = ReflectUtil.getMethodOrThrow(List.class, "get", int.class);
        assertEquals("a", ReflectUtil.invoke(holder, method, 0));
    }

}
