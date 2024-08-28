package com.quemistry.user_ms.repository;

import com.quemistry.user_ms.repository.converter.AttributeEncryptor;
import com.quemistry.user_ms.service.impl.CryptoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

public class AttributeEncryptorTest {

    private AttributeEncryptor attributeEncryptor;

    public void init() throws NoSuchPaddingException, NoSuchAlgorithmException {
        var cryptoService = new CryptoServiceImpl("770A8A65DA156D24EE2A093277530142", 20);
        attributeEncryptor = new AttributeEncryptor(cryptoService);
    }

    @Test
    public void testEncrypt() throws NoSuchPaddingException, NoSuchAlgorithmException {
        init();
        String encryptedText = attributeEncryptor.convertToDatabaseColumn("plaintext");
        System.out.println(encryptedText);
        Assertions.assertFalse(encryptedText.isEmpty());
        Assertions.assertEquals(60, encryptedText.length());
    }

    @Test
    public void testDecrypt() throws NoSuchPaddingException, NoSuchAlgorithmException {
        init();

        String expectedDecryptedText = "plaintext";
        String encryptedText = attributeEncryptor.convertToEntityAttribute("EZE0KNIfVk9Izr3FQPOA8Wn5cg69vy0i9frvpueLEXFlxRr93PL660KigDYz");
        Assertions.assertEquals(expectedDecryptedText, encryptedText);
    }
}
