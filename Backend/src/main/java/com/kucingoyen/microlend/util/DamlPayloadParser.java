package com.kucingoyen.microlend.util;

import java.math.BigDecimal;

/**
 * Utility class for safely parsing values from DAML JSON API payloads.
 * DAML JSON API may return numeric values as either String or Number types.
 */
public class DamlPayloadParser {

    /**
     * Safely parse integer from DAML payload value.
     * Handles both Number and String types.
     *
     * @param value The value from DAML payload
     * @return Parsed integer, or 0 if value is null
     * @throws IllegalArgumentException if value cannot be parsed
     */
    public static Integer parseInteger(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Cannot parse integer from string: " + value, e);
            }
        }
        throw new IllegalArgumentException("Cannot parse integer from type: " + value.getClass().getName());
    }

    /**
     * Safely parse long from DAML payload value.
     *
     * @param value The value from DAML payload
     * @return Parsed long, or 0L if value is null
     */
    public static Long parseLong(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Cannot parse long from string: " + value, e);
            }
        }
        throw new IllegalArgumentException("Cannot parse long from type: " + value.getClass().getName());
    }

    /**
     * Safely parse BigDecimal from DAML payload value.
     *
     * @param value The value from DAML payload
     * @return Parsed BigDecimal, or BigDecimal.ZERO if value is null
     */
    public static BigDecimal parseBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        if (value instanceof String) {
            try {
                return new BigDecimal((String) value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Cannot parse BigDecimal from string: " + value, e);
            }
        }
        throw new IllegalArgumentException("Cannot parse BigDecimal from type: " + value.getClass().getName());
    }

    /**
     * Safely get string from DAML payload value.
     *
     * @param value The value from DAML payload
     * @return String value, or empty string if null
     */
    public static String parseString(Object value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    /**
     * Safely parse boolean from DAML payload value.
     *
     * @param value The value from DAML payload
     * @return Parsed boolean, or false if null
     */
    public static Boolean parseBoolean(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        throw new IllegalArgumentException("Cannot parse boolean from type: " + value.getClass().getName());
    }
}
