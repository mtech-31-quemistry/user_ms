package com.quemistry.user_ms.controller;

import com.quemistry.user_ms.model.CryptoDto;
import com.quemistry.user_ms.model.response.CryptoResponseDto;
import com.quemistry.user_ms.service.CryptoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/crypto")
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @PostMapping("/encrypt")
    public ResponseEntity<CryptoResponseDto> encrypt(@RequestBody CryptoDto cryptoDto) throws Exception {
        log.info("Encryption started and value: {}", cryptoDto.input());
        String encryptedText = this.cryptoService.encrypt(cryptoDto.input());

        log.info("Encryption ended");

        return ResponseEntity.ok(new CryptoResponseDto(encryptedText));
    }

    @PostMapping("/decrypt")
    public ResponseEntity<CryptoResponseDto> decrypt(@RequestBody CryptoDto cryptoDto) throws Exception {
        log.info("Decryption started and value: {}", cryptoDto.input());

        String decryptedText = this.cryptoService.decrypt(cryptoDto.input());

        log.info("Decryption ended");

        return ResponseEntity.ok(new CryptoResponseDto(decryptedText));
    }
}
