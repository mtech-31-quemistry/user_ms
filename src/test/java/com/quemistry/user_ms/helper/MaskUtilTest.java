package com.quemistry.user_ms.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MaskUtilTest {

    @Test
    void testMaskUtil_AbleToMaskResponse() {
        String input = "{ \"firstName\": \"Test123\"}";

        String maskedValue = MaskUtil.applyMask(input);

        String expectedValue = "{\"firstName\":\"*******\"}";

        Assertions.assertEquals(expectedValue, maskedValue);
    }
}
