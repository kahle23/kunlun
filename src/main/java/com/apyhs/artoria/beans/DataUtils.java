package com.apyhs.artoria.beans;

import com.apyhs.artoria.exception.UncheckedException;
import com.apyhs.artoria.util.Assert;
import com.apyhs.artoria.util.CollectionUtils;
import com.apyhs.artoria.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

import static com.apyhs.artoria.util.Const.*;

/**
 * Data handle tools.
 * @author Kahle
 */
@Deprecated
// TODO: Not finished.
public class DataUtils {

    public static <T> List<T> multilevelList(List<T> data, String sonList, String sign, String parentSign, String... relationProperties) {
        try {
            if (CollectionUtils.isEmpty(data)) { return null; }
            Class<?> clazz = data.get(0).getClass();
            Method signMethod = clazz.getMethod(GET + StringUtils.capitalize(sign));
            Method parentMethod = clazz.getMethod(GET + StringUtils.capitalize(parentSign));
            Method sonListMethod = clazz.getMethod(SET + StringUtils.capitalize(sonList), List.class);
            List<Method> relations = new ArrayList<Method>();
            for (String relationProperty : relationProperties) {
                Method method = clazz.getMethod(GET + StringUtils.capitalize(relationProperty));
                relations.add(method);
            }
            boolean hasRelation = relations.size() > 0;

            // key = Parent + relation， value = son list
            Map<String, List<T>> map = new HashMap<String, List<T>>(data.size() + 1);
            map.put(ROOT, new ArrayList<T>());
            for (T obj : data) {
                Object parent = parentMethod.invoke(obj);
                Object signObj = signMethod.invoke(obj);
                StringBuilder relation = new StringBuilder();
                if (hasRelation) {
                    for (Method method : relations) {
                        relation.append(method.invoke(obj));
                    }
                }
                String parentKey = parent == null ? "" : parent + "";
                // when parent is blank, obj is root element
                if (StringUtils.isBlank(parentKey)) { map.get(ROOT).add(obj); }
                parentKey = hasRelation ? parentKey + relation : parentKey;

                // create parent list
                List<T> list = map.get(parentKey);
                if (list == null) {
                    list = new ArrayList<T>();
                    map.put(parentKey, list);
                }
                list.add(obj);

                // create sign list
                String signKey = signObj == null ? "" : signObj + "";
                Assert.notBlank(signKey, String.format("Sign \"%s\"'s value \"%s\" must not blank. ", sign, signObj));
                signKey = hasRelation ? signKey + relation : signKey;
                list = map.get(signKey);
                if (list == null) {
                    list = new ArrayList<T>();
                    map.put(signKey, list);
                }
                sonListMethod.invoke(obj, list);
            }

            return map.get(ROOT);
        }
        catch (Exception e) {
            throw new UncheckedException(e);
        }
    }

    public static <T> List<T> distinctList(List<T> data, String... referProperties) {
        try {
            if (CollectionUtils.isEmpty(data)) { return null; }
            Class<?> clazz = data.get(0).getClass();
            Assert.notEmpty(referProperties, "Parameter \"referProperties\" must not empty. ");
            List<Method> referMethods = new ArrayList<Method>();
            for (String relationProperty : referProperties) {
                Method method = clazz.getMethod(GET + StringUtils.capitalize(relationProperty));
                referMethods.add(method);
            }

            LinkedHashMap<String, T> map = new LinkedHashMap<String, T>();
            StringBuilder key = new StringBuilder();
            for (T t : data) {
                key.setLength(0);
                for (Method m : referMethods) {
                    key.append(m.invoke(t));
                }
                map.put(key.toString(), t);
            }

            data = new ArrayList<T>();
            data.addAll(map.values());
            return data;
        }
        catch (Exception e) {
            throw new UncheckedException(e);
        }
    }

//    public static <T> void packetList(List<T> data, Fn<Void, List<T>> fn, int groupSize) {
//        Assert.notEmpty(data, "Parameter \"data\" must not empty. ");
//        Assert.notNull(fn, "Parameter \"fn\" must not empty. ");
//        for (int i = 0, c = data.size() / groupSize + 1; i < c; i++) {
//            List<T> groupData;
//            if (data.size() == 0) {
//                continue;
//            }
//            else if (data.size() >= groupSize) {
//                groupData = data.subList(0, groupSize);
//                data = data.subList(groupSize, data.size());
//            }
//            else {
//                groupData = data;
//            }
//            fn.call(groupData);
//        }
//    }

}
