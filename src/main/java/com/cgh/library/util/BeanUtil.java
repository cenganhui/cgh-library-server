package com.cgh.library.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author cenganhui
 */
public abstract class BeanUtil extends BeanUtils {

    public static String[] getNullPropertyNames(Object source, String... ignoreProperties) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>(Arrays.asList(ignoreProperties));
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 使用此方法进行忽略 null 值的 Bean 的 copy
     *
     * @param src              源对象
     * @param target           目标对象
     * @param ignoreProperties 忽略的属性
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target, String... ignoreProperties) {
        copyProperties(src, target, getNullPropertyNames(src, ignoreProperties));
    }

}