package com.quemistry.user_ms.repository;

import com.quemistry.user_ms.repository.converter.AttributeEncryptor;
import com.quemistry.user_ms.service.CryptoService;
import com.quemistry.user_ms.service.impl.CryptoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

class AttributeEncryptorTest {

    @Mock
    private CryptoService cryptoService;

    @InjectMocks
    private AttributeEncryptor attributeEncryptor;

    void init() throws NoSuchPaddingException, NoSuchAlgorithmException {
        MockitoAnnotations.openMocks(this);
        cryptoService = new CryptoServiceImpl("secret12345678910111234567890121", "key");
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
    void testEncryptThrowsException() throws Exception {
        MockitoAnnotations.openMocks(this);

        Mockito.doThrow(Exception.class).when(cryptoService).encrypt(Mockito.anyString());
        Assertions.assertThrows(Exception.class, () -> attributeEncryptor.convertToDatabaseColumn("hello"));
    }

    @Test
    void testDecrypt() throws NoSuchPaddingException, NoSuchAlgorithmException {
        init();

        String expectedDecryptedText = "plaintext";
        String decryptedText = attributeEncryptor.convertToEntityAttribute("a2V5GC7OOMcz0DkXtWKAR1IfS/xqc5sZF5UsfQ==");
        System.out.println(decryptedText);
        Assertions.assertEquals(expectedDecryptedText, decryptedText);
    }

    @Test
    void testDecryptThrowsException() throws Exception {
        MockitoAnnotations.openMocks(this);

        Mockito.doThrow(Exception.class).when(cryptoService).decrypt(Mockito.anyString());
        Assertions.assertThrows(Exception.class, () -> attributeEncryptor.convertToEntityAttribute("encryptedText"));
    }
}
