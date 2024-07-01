package com.quemistry.user_ms.repository.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

@Slf4j
@Component
@Converter
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private final int IV_KEY_SIZE = 16;

    private final Key secretKeySpec;
    private final Cipher cipher;

    public AttributeEncryptor(@Value("${quemistry.user.cipher.key}") String secret) throws NoSuchPaddingException, NoSuchAlgorithmException {
        secretKeySpec = new SecretKeySpec(secret.getBytes(), "AES");
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

    @Override
    public String convertToDatabaseColumn(String plainText) {
        try {
            byte[] clean = plainText.getBytes();

            // Generating IV.
            byte[] iv = new byte[IV_KEY_SIZE];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // Encrypt.
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(clean);

            // Combine IV and encrypted part.
            int KEY_SIZE = 16;
            byte[] encryptedIVAndText = new byte[KEY_SIZE + encrypted.length];
            System.arraycopy(iv, 0, encryptedIVAndText, 0, KEY_SIZE);
            System.arraycopy(encrypted, 0, encryptedIVAndText, KEY_SIZE, encrypted.length);

            return Base64.getEncoder().encodeToString(cipher.doFinal(encryptedIVAndText));

        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String encryptedIVAndText) {
        try {

            byte[] encryptedIvTextBytes = Base64.getDecoder().decode(encryptedIVAndText);

            // Extract IV.
            byte[] iv = new byte[IV_KEY_SIZE];
            System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // Extract encrypted part.
            int encryptedSize = encryptedIvTextBytes.length - IV_KEY_SIZE;
            byte[] encryptedBytes = new byte[encryptedSize];
            System.arraycopy(encryptedIvTextBytes, IV_KEY_SIZE, encryptedBytes, 0, encryptedSize);

            // Decrypt.
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decrypted = cipher.doFinal(encryptedBytes);

            return new String(decrypted);

        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }
}
