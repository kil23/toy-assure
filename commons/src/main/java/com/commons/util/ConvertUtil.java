package com.commons.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtil{

    public static <T,E> T convert(E source, Class<T> targetType) {
        if (Utility.isObjectNull(source)) return null;
        T target = null;
        try {
            target = targetType.newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return target;
    }

    public static <T,E> List<T> convert(List<E> source, Class<T> targetType) {
        if (Utility.isEmptyList(source)) return null;
        List<T> list = new ArrayList<T>();
        for(E obj : source){
            try {
                T target = targetType.newInstance();
                BeanUtils.copyProperties(obj, target);
                list.add(target);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
