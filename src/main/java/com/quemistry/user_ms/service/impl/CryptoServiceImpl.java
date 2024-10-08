package com.quemistry.user_ms.service.impl;

import com.quemistry.user_ms.service.CryptoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.quemistry.user_ms.constant.EncryptionConstant.*;

@Service
public class CryptoServiceImpl implements CryptoService {

    private final Cipher cipher;
    private final SecretKey secretKey;
    private final String ivKey;

    public CryptoServiceImpl(
            @Value("${quemistry.user.cipher.key}") String secret,
            @Value("${quemistry.user.cipher.iv}") String ivKey) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.ivKey = ivKey;
        this.secretKey = new SecretKeySpec(secret.getBytes(), "AES");
        this.cipher = Cipher.getInstance(ENCRYPT_ALGO);
    }

    /**
     * @param plainText Plain Text
     * @return encrypted cipher text with iv
     */
    @Override
    public String encrypt(String plainText) throws Exception {
        if (plainText == null) {
            return null;
        }
        byte[] cipherTextWithIv;
        cipherTextWithIv = this.encryptWithPrefixIV(plainText.getBytes(UTF_8), this.getNonce());

        return Base64.getEncoder().encodeToString(cipherTextWithIv);
    }

    /**
     * @param cipherTextWithIv Encrypted text with IV
     * @return plain text
     */
    @Override
    public String decrypt(String cipherTextWithIv) throws Exception {
        if (cipherTextWithIv == null) {
            return null;
        }
        byte[] cipherTextWithIvBytes = Base64.getDecoder().decode(cipherTextWithIv);
        return this.decryptWithPrefixIV(cipherTextWithIvBytes);
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

        byte[] iv = new byte[this.ivKey.length()];
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

    public byte[] getNonce() {
        return this.ivKey.getBytes(UTF_8);
    }
}
