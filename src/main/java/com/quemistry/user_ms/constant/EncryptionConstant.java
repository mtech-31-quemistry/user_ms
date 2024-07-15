package com.quemistry.user_ms.constant;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EncryptionConstant {

    private EncryptionConstant() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
    public static final int TAG_LENGTH_BIT = 128;

    public static final Charset UTF_8 = StandardCharsets.UTF_8;
}
