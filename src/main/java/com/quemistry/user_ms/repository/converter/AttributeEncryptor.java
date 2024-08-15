package com.quemistry.user_ms.repository.converter;

import com.quemistry.user_ms.service.CryptoService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Converter
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private final CryptoService cryptoService;

    public AttributeEncryptor(
            CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @Override
    public String convertToDatabaseColumn(String plainText) {
        try {
            return this.cryptoService.encrypt(plainText);
        } catch (Exception e) {
            log.error("Error encryption: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String cipherTextWithIv) {
        try {
            return this.cryptoService.decrypt(cipherTextWithIv);
        } catch (Exception e) {
            log.error("Error decryption: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
