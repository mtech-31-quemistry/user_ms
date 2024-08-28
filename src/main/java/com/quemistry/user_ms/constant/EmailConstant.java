package com.quemistry.user_ms.constant;

public class EmailConstant {
    public static final String STUDENT_INVITATION_TEMPLATE = "/email/student/invitation";

    public static final String STATUS_SENT = "SENT";

    private EmailConstant() {
        throw new IllegalStateException("Utility class");
    }
}
