package com.quemistry.user_ms.repository.converter;

import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private static final String AES = "AES";

    private final String secret;

    private final Key key;
    private final Cipher cipher;

    public AttributeEncryptor(@Value("${quemistry.user.cipher.key}") String secret) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.secret = secret;

        log.info("Secret: {}", this.secret);

        key = new SecretKeySpec(this.secret.getBytes(), AES);
        cipher = Cipher.getInstance(AES);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }
}
