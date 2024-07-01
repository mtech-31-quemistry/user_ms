package com.quemistry.user_ms.repository.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@Slf4j
@Converter
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
    private final int TAG_LENGTH_BIT = 128;

    private final Charset UTF_8 = StandardCharsets.UTF_8;
    private final Cipher cipher;
    private final SecretKey secretKey;
    private final int ivKeySize;

    public AttributeEncryptor(
            @Value("${quemistry.user.cipher.key}") String secret,
            @Value("${quemistry.user.cipher.size}") int ivKeySize) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.ivKeySize = ivKeySize;
        secretKey = new SecretKeySpec(secret.getBytes(), "AES");
        cipher = Cipher.getInstance(ENCRYPT_ALGO);
    }

    @Override
    public String convertToDatabaseColumn(String plainText) {

        byte[] cipherTextWithIv;
        try {
            cipherTextWithIv = this.encryptWithPrefixIV(plainText.getBytes(UTF_8), this.getRandomNonce(this.ivKeySize));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Base64.getEncoder().encodeToString(cipherTextWithIv);
    }

    @Override
    public String convertToEntityAttribute(String cipherTextWithIv) {
        try {
            byte[] cipherTextWithIvBytes = Base64.getDecoder().decode(cipherTextWithIv);
            return this.decryptWithPrefixIV(cipherTextWithIvBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // prefix IV length + IV bytes to cipher text
    public byte[] encryptWithPrefixIV(byte[] plainTextBytes, byte[] iv) throws Exception {

        byte[] cipherText = encrypt(plainTextBytes, this.secretKey, iv);

        byte[] cipherTextWithIv = ByteBuffer.allocate(iv.length + cipherText.length)
                .put(iv)
                .put(cipherText)
                .array();
        return cipherTextWithIv;
    }

    // AES-GCM needs GCMParameterSpec
    private byte[] encrypt(byte[] plainTextBytes, SecretKey secret, byte[] iv) throws Exception {

        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        return cipher.doFinal(plainTextBytes);
    }

    public String decryptWithPrefixIV(byte[] cipherTextWithIv) throws Exception {

        ByteBuffer bb = ByteBuffer.wrap(cipherTextWithIv);

        byte[] iv = new byte[this.ivKeySize];
        bb.get(iv);

        byte[] cipherText = new byte[bb.remaining()];
        bb.get(cipherText);

        return decrypt(cipherText, this.secretKey, iv);

    }

    public String decrypt(byte[] cText, SecretKey secret, byte[] iv) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secret, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        byte[] plainText = cipher.doFinal(cText);
        return new String(plainText, UTF_8);

    }

    public byte[] getRandomNonce(int numBytes) {
        byte[] nonce = new byte[numBytes];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }
}
