package com.easyexpense.commons.utils;

/**
 * Utility class providing common operations dealing with strings.
 *
 * @author Manikanta
 */
public class StringUtils {

    /**
     * Hide the default constructor. Instantiating utility classes does not make sense.
     */
    private StringUtils() {

    }

    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    /**
     * Returns true if the given string is equal to
     * "1" or "true" or "yes" or "active" (ignoring case), else returns false.
     */
    public static boolean toBoolean(String stringRep) {
        if (StringUtils.isEmpty(stringRep)) {
            return false;
        } else {
            if ("true".equalsIgnoreCase(stringRep.trim()) ||
                    "yes".equalsIgnoreCase(stringRep.trim()) ||
                    "active".equalsIgnoreCase(stringRep.trim()) ||
                    "1".equalsIgnoreCase(stringRep.trim())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static String getSentenceCase(String inputString){
        if(isNotEmpty(inputString)){
            return inputString.substring(0,1).toUpperCase() + inputString.substring(1);
        }
        return null;
    }


    //method that removes <ul</ul> <ol><ol> tags , replaces <li> with html bullet code
    //and </li> with <br>
    public static String returnHtmlStringWithBasicTags(String advHtmlString){

        advHtmlString = advHtmlString.replaceAll("(\\Q<ul>\\E)|(\\Q</ul>\\E)|(\\Q<ol>\\E)|(\\Q</ol>\\E)","");
        advHtmlString = advHtmlString.replaceAll("(\\Q<li>\\E)","&#149;");
        return advHtmlString.replaceAll("(\\Q</li>\\E)","<br>");
    }

}
