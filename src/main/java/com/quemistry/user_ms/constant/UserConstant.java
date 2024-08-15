package com.quemistry.user_ms.constant;

public class UserConstant {

    public static final String STATUS_CODE_SUCCESS = "00";
    public static final String STATUS_CODE_VALIDATION_ERROR = "01";
    public static final String STATUS_CODE_BUSINESS_ERROR = "02";
    public static final String STATUS_CODE_GENERIC_ERROR = "99";
    public static final String USER_ID_HEADER_KEY = "X-USER-ID";

    public static final int USER_TYPE_TUTOR = 1;
    public static final int USER_TYPE_STUDENT = 2;

    private UserConstant() {
        throw new IllegalStateException("Utility class");
    }
}
