package com.quemistry.user_ms.repository;

import com.quemistry.user_ms.repository.converter.AttributeEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

public class AttributeEncryptorTest {

    private AttributeEncryptor attributeEncryptor;

    public void init() throws NoSuchPaddingException, NoSuchAlgorithmException {
        attributeEncryptor = new AttributeEncryptor("12345678910111213141516171819202", 10);
    }

    @Test
    public void testEncrypt() throws NoSuchPaddingException, NoSuchAlgorithmException {
        init();
        String encryptedText = attributeEncryptor.convertToDatabaseColumn("plaintext");
        Assertions.assertFalse(encryptedText.isEmpty());
        Assertions.assertEquals(48, encryptedText.length());
    }

    @Test
    public void testDecrypt() throws NoSuchPaddingException, NoSuchAlgorithmException {
        init();

        String expectedDecryptedText = "plaintext";
        String encryptedText = attributeEncryptor.convertToEntityAttribute("+p+bzShqUgMPJwk42HgmqFGaa0rtGAnuhHPbmYbRRvKM0U8=");
        Assertions.assertEquals(expectedDecryptedText, encryptedText);
    }
}
