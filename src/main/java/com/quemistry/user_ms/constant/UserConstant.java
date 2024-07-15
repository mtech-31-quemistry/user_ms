package com.quemistry.user_ms.constant;

public class UserConstant {

    private UserConstant() {
        throw new IllegalStateException("Utility class");
    }

    public static final String STATUS_CODE_SUCCESS = "00";

    public static final String STATUS_CODE_VALIDATION_ERROR = "01";

    public static final String STATUS_CODE_BUSINESS_ERROR = "02";

    public static final String STATUS_CODE_GENERIC_ERROR = "99";
}
