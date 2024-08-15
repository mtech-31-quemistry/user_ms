package com.quemistry.user_ms.constant;

public class UserConstant {

    public static final String STATUS_CODE_SUCCESS = "00";
    public static final String STATUS_CODE_VALIDATION_ERROR = "01";
    public static final String STATUS_CODE_BUSINESS_ERROR = "02";
    public static final String STATUS_CODE_GENERIC_ERROR = "99";
    public static final String HEADER_KEY_USER_ID = "X-USER-ID";
    public static final String HEADER_KEY_USER_EMAIL = "x-user-email";

    public static final String STATUS_ACTIVE = "ACTIVE";

    public static final int USER_TYPE_TUTOR = 1;
    public static final int USER_TYPE_STUDENT = 2;

    private UserConstant() {
        throw new IllegalStateException("Utility class");
    }
}
