package com.easyexpense.commons.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Utility class providing common operations dealing with collections.
 *
 * @author Manikanta
 */
public class CollectionUtils {

    /**
     * Hide the default constructor. Instantiating utility classes does not make sense.
     */
    private CollectionUtils() {

    }

    public static boolean isNotEmpty(Collection input) {
        return input != null && !input.isEmpty();
    }

    public static boolean isEmpty(Collection input) {
        return input == null || input.isEmpty();
    }

    public static boolean isEmptyMap(Map<String,Object> input){
        return input==null || input.isEmpty();
    }

    public static boolean isNotEmptyMap(Map<String,Object> input){
        return input !=null && !input.isEmpty();
    }
}
