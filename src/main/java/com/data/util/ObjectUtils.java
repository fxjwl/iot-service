package com.data.util;

import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtils {

    public static Object getValueOfGet(Object object, String fieldName) throws Exception {
        if (object == null) {
            return null;
        }
        if (StringUtils.isEmpty(fieldName)) {
            return null;
        }
        Field field = null;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);

                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                //获得get方法
                Method getMethod = pd.getReadMethod();
                //执行get方法返回一个Object
                return getMethod.invoke(object);
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }

        return "";
    }

    public static Object getValueOfGetIncludeObjectFeild(Object object, String fieldName)
            throws Exception {

        if (object == null) {
            return null;
        }
        if (StringUtils.isEmpty(fieldName)) {
            return null;
        }

        if(HashMap.class.equals(object.getClass())){
            return ((Map)object).get(fieldName);
        }

        Field field = null;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                if (fieldName.contains(".")) {
                    // 如：operatorUser.name、operatorUser.org.name，递归调用
                    String[] splitFiledName = fieldName.split("\\.");
                    return getValueOfGetIncludeObjectFeild(
                            getValueOfGetIncludeObjectFeild(object, splitFiledName[0]),
                            splitFiledName[1]);
                }
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);

                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                //获得get方法
                Method getMethod = pd.getReadMethod();
                //执行get方法返回一个Object
                return getMethod.invoke(object);
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }

        return null;
    }

}
