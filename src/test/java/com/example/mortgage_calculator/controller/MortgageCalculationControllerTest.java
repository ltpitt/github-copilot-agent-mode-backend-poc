package com.example.mortgage_calculator.controller;

import com.example.mortgage_calculator.dto.MortgageCalculationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MortgageCalculationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return 200 and calculate mortgage with valid input")
    void shouldReturn200AndCalculateMortgageWithValidInput() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(BigDecimal.valueOf(300000));
        request.setInterestRate(BigDecimal.valueOf(3.5));
        request.setDuration(30);

        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.monthlyPayment", notNullValue()))
                .andExpect(jsonPath("$.totalInterest", notNullValue()))
                .andExpect(jsonPath("$.monthlyPayment").exists())
                .andExpect(jsonPath("$.totalInterest").exists());
    }

    @Test
    @DisplayName("Should return 200 and calculate mortgage with zero interest rate")
    void shouldReturn200AndCalculateMortgageWithZeroInterestRate() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(BigDecimal.valueOf(240000));
        request.setInterestRate(BigDecimal.ZERO);
        request.setDuration(20);

        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.monthlyPayment", is(1000.00)))
                .andExpect(jsonPath("$.totalInterest", is(0.00)));
    }

    @Test
    @DisplayName("Should return 400 when principal is missing")
    void shouldReturn400WhenPrincipalIsMissing() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        // Principal is not set (null)
        request.setInterestRate(BigDecimal.valueOf(3.5));
        request.setDuration(30);

        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when principal is negative")
    void shouldReturn400WhenPrincipalIsNegative() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(BigDecimal.valueOf(-1000));
        request.setInterestRate(BigDecimal.valueOf(3.5));
        request.setDuration(30);

        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when principal is zero")
    void shouldReturn400WhenPrincipalIsZero() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(BigDecimal.ZERO);
        request.setInterestRate(BigDecimal.valueOf(3.5));
        request.setDuration(30);

        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when interest rate is missing")
    void shouldReturn400WhenInterestRateIsMissing() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(BigDecimal.valueOf(300000));
        // Interest rate is not set (null)
        request.setDuration(30);

        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when interest rate is negative")
    void shouldReturn400WhenInterestRateIsNegative() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(BigDecimal.valueOf(300000));
        request.setInterestRate(BigDecimal.valueOf(-1.0));
        request.setDuration(30);

        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when duration is missing")
    void shouldReturn400WhenDurationIsMissing() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(BigDecimal.valueOf(300000));
        request.setInterestRate(BigDecimal.valueOf(3.5));
        // Duration is not set (null)

        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when duration is zero or negative")
    void shouldReturn400WhenDurationIsZeroOrNegative() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(BigDecimal.valueOf(300000));
        request.setInterestRate(BigDecimal.valueOf(3.5));
        request.setDuration(0);

        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when request body is malformed JSON")
    void shouldReturn400WhenRequestBodyIsMalformedJson() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid-json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when request body is empty")
    void shouldReturn400WhenRequestBodyIsEmpty() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
}