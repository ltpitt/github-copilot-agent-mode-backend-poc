package com.example.mortgage_calculator.controller;

import com.example.mortgage_calculator.dto.MortgageCalculationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.mortgage_calculator.service.MortgageCalculationService;
import com.example.mortgage_calculator.dto.MortgageCalculationResponse;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MortgageCalculatorController.class)
class MortgageCalculatorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MortgageCalculationService mortgageCalculationService;

    @Test
    void calculateMortgage_withValidRequest_returnsOk() throws Exception {
        // Arrange
        MortgageCalculationRequest request = new MortgageCalculationRequest(
            new BigDecimal("300000"),
            new BigDecimal("3.5"),
            30
        );

        MortgageCalculationResponse mockResponse = new MortgageCalculationResponse(
            new BigDecimal("1347.13"),
            new BigDecimal("184968.00")
        );

        when(mortgageCalculationService.calculateMortgage(any(MortgageCalculationRequest.class)))
            .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.monthlyPayment").exists())
                .andExpect(jsonPath("$.totalInterest").exists())
                .andExpect(jsonPath("$.monthlyPayment").isNumber())
                .andExpect(jsonPath("$.totalInterest").isNumber());
    }

    @Test
    void calculateMortgage_withZeroInterestRate_returnsOk() throws Exception {
        // Arrange
        MortgageCalculationRequest request = new MortgageCalculationRequest(
            new BigDecimal("120000"),
            BigDecimal.ZERO,
            10
        );

        MortgageCalculationResponse mockResponse = new MortgageCalculationResponse(
            new BigDecimal("1000.00"),
            BigDecimal.ZERO
        );

        when(mortgageCalculationService.calculateMortgage(any(MortgageCalculationRequest.class)))
            .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.monthlyPayment").value(1000.00))
                .andExpect(jsonPath("$.totalInterest").value(0.00));
    }

    @Test
    void calculateMortgage_withNullPrincipal_returnsBadRequest() throws Exception {
        // Arrange
        MortgageCalculationRequest request = new MortgageCalculationRequest(
            null, // null principal
            new BigDecimal("3.5"),
            30
        );

        // Act & Assert
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateMortgage_withNegativePrincipal_returnsBadRequest() throws Exception {
        // Arrange
        MortgageCalculationRequest request = new MortgageCalculationRequest(
            new BigDecimal("-100000"), // negative principal
            new BigDecimal("3.5"),
            30
        );

        // Act & Assert
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateMortgage_withNullInterestRate_returnsBadRequest() throws Exception {
        // Arrange
        MortgageCalculationRequest request = new MortgageCalculationRequest(
            new BigDecimal("300000"),
            null, // null interest rate
            30
        );

        // Act & Assert
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateMortgage_withNegativeInterestRate_returnsBadRequest() throws Exception {
        // Arrange
        MortgageCalculationRequest request = new MortgageCalculationRequest(
            new BigDecimal("300000"),
            new BigDecimal("-1.5"), // negative interest rate
            30
        );

        // Act & Assert
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateMortgage_withNullDuration_returnsBadRequest() throws Exception {
        // Arrange
        MortgageCalculationRequest request = new MortgageCalculationRequest(
            new BigDecimal("300000"),
            new BigDecimal("3.5"),
            null // null duration
        );

        // Act & Assert
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateMortgage_withNegativeDuration_returnsBadRequest() throws Exception {
        // Arrange
        MortgageCalculationRequest request = new MortgageCalculationRequest(
            new BigDecimal("300000"),
            new BigDecimal("3.5"),
            -5 // negative duration
        );

        // Act & Assert
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateMortgage_withInvalidJson_returnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateMortgage_withEmptyBody_returnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }
}