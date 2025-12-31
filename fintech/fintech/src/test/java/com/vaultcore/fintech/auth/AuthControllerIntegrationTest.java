package com.vaultcore.fintech.auth;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void fullAuthFlow_register_login_refresh() throws Exception {
        // 1. Register
        Map<String, String> registerBody = Map.of(
                "username", "testuser",
                "password", "Secret123",
                "email", "testuser@example.com"
        );

        mockMvc.perform(post("/vaultcore/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"));

        // 2. Login
        Map<String, String> loginBody = Map.of(
                "username", "testuser",
                "password", "Secret123"
        );

        String loginResponse = mockMvc.perform(post("/vaultcore/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.refresh_token").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<?, ?> loginJson = mapper.readValue(loginResponse, Map.class);
        String refreshToken = (String) loginJson.get("refresh_token");

        // 3. Refresh
        Map<String, String> refreshBody = Map.of("refresh_token", refreshToken);

        mockMvc.perform(post("/vaultcore/api/auth/token/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(refreshBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.refresh_token").exists());
    }
}
