package com.quemistry.user_ms.service;

public interface CryptoService {

    String encrypt(String plainText) throws Exception;

    String decrypt(String cipherText) throws Exception;
}
