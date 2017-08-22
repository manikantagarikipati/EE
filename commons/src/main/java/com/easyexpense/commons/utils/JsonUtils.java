package com.easyexpense.commons.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

/**
 * Created by Manikanta on 1/13/2016.
 */
public final class JsonUtils {

    /**
     * Jackson object mapper for serializing & de-serializing JSON data.
     */
    private static final ObjectMapper JACKSON_MAPPER = new ObjectMapper();

    /**
     * Configure the Jackson object mapper .
     */
    static {
        /**
         * Serialization configuration.
         */
        JACKSON_MAPPER.configure(SerializationFeature.CLOSE_CLOSEABLE, true);
        JACKSON_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
        JACKSON_MAPPER.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true);
        JACKSON_MAPPER.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        JACKSON_MAPPER.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
        JACKSON_MAPPER.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, false);
        JACKSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        /**
         * De-serialization configuration.
         */

        JACKSON_MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        JACKSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Hide the default constructor. Instantiating utility classes does not make sense.
     */
    private JsonUtils() {

    }

    /**
     * Parse the JSON string into the given Java type.
     */
    public static <T> T parseJson(String json, TypeReference<T> typeReference) {
        try {
            if (StringUtils.isNotEmpty(json)) {
                return JACKSON_MAPPER.readValue(json, typeReference);
            } else {
                return null;
            }
        } catch (IOException ie) {
            return null;
        }
    }

    /**
     * Serialize the given object to a JSON string.
     */
    public static <T> String toJson(T object) {
        try {
            if (object != null) {
                return JACKSON_MAPPER.writeValueAsString(object);
            } else {
                return null;
            }
        } catch (JsonProcessingException jpe) {
            return null;
        }
    }
}
