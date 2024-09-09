package com.quemistry.user_ms.controller;

import com.quemistry.user_ms.controller.base.BaseController;
import com.quemistry.user_ms.service.CryptoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CryptoControllerTest.class)
public class CryptoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoService cryptoService;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new CryptoController(cryptoService))
                .setControllerAdvice(new BaseController())
                .build();
    }

    @DisplayName("Should return status 200 when try to encrypt input text")
    @Test
    void givenCryptoController_whenEncrypt_thenStatus200() throws Exception {

        when(cryptoService.encrypt(any())).thenReturn("encrypted");

        mockMvc.perform(post("/v1/crypto/encrypt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "input": "plaintext"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.output").value("encrypted"));

        verify(cryptoService, times(1)).encrypt(any());
    }

    @DisplayName("Should return status 200 when try to encrypt input text")
    @Test
    void givenCryptoController_whenDecrypt_thenStatus200() throws Exception {

        when(cryptoService.decrypt(any())).thenReturn("decrypted");

        mockMvc.perform(post("/v1/crypto/decrypt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "input": "encrypted"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.output").value("decrypted"));

        verify(cryptoService, times(1)).decrypt(any());
    }
}
