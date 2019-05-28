package com.baiyajin.util.u;

import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectUtil {


/*    public static Map<String,Object> objcetToMap( Object entity) throws IllegalAccessException, InstantiationException {
       Map<String, Object> map = new HashMap<>();
        if (entity == null) {
            return map;
        }
        Class clazz = entity.getClass();
//       T t = entity.newInstance();
        Field[] fs= entity.getDeclaredFields();
        for(Field f:fs){
            map.put(f.getName(), f.get(entity));
        }
    return map;
    }*/
    public static Map<String, Object> objcetToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) { return map; }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try { for (Field field : fields) {
            field.setAccessible(true); map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace(); }
        return map;
    }




    public static <T> T mapToObjcet(Map<String,Object> map, Class<T> entity) {
        T t = null;
        try {
            Method[] ms= entity.getDeclaredMethods();
            t = entity.newInstance();
            System.out.println(map);
            for (String key : map.keySet()) {
                String keym="set"+key.substring(0,1).toUpperCase()+key.substring(1);
                for(Method m:ms){
                    if(m.getName().equals(keym)){
                        m.invoke(t,map.get(key));
                    }
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }


    public static <T> List<T> mapToObjcet(List<Map<String,Object>> listMap , Class<T> entity){
        List<T> listObject = new ArrayList<T>();
        for(Map<String,Object> m:listMap){
            listObject.add(mapToObjcet(m,entity));
        }
        return listObject;
    }




}