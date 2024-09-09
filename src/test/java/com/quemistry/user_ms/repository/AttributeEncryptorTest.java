package com.quemistry.user_ms.repository;

import com.quemistry.user_ms.repository.converter.AttributeEncryptor;
import com.quemistry.user_ms.service.impl.CryptoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

class AttributeEncryptorTest {

    private AttributeEncryptor attributeEncryptor;

    void init() throws NoSuchPaddingException, NoSuchAlgorithmException {
        var cryptoService = new CryptoServiceImpl("JUDTixu0WmNfaPXEfnFgwZVmcGZ6yVUI", "test");
        attributeEncryptor = new AttributeEncryptor(cryptoService);
    }

    @Test
    void testEncrypt() throws NoSuchPaddingException, NoSuchAlgorithmException {
        init();
        String encryptedText = attributeEncryptor.convertToDatabaseColumn("plaintext");
        System.out.println(encryptedText);
        Assertions.assertFalse(encryptedText.isEmpty());
        Assertions.assertEquals(40, encryptedText.length());
    }

    @Test
    void testDecrypt() throws NoSuchPaddingException, NoSuchAlgorithmException {
        init();

        String expectedDecryptedText = "plaintext";
        String decryptedText = attributeEncryptor.convertToEntityAttribute("dGVzdJaGROD7z6oawEIAuDbZxK0Oq8gBTFl6A4A=");
        Assertions.assertEquals(expectedDecryptedText, decryptedText);
    }
}
