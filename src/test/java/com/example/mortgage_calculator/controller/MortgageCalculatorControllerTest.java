package com.example.mortgage_calculator.controller;

import com.example.mortgage_calculator.dto.MortgageCalculationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MortgageCalculatorControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testCalculateMortgage_success() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(new BigDecimal("300000.00"));
        request.setInterestRate(new BigDecimal("3.5"));
        request.setDurationYears(30);
        
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.monthlyPayment").value(1347.13))
                .andExpect(jsonPath("$.totalInterest").value(184966.80));
    }
    
    @Test
    void testCalculateMortgage_zeroInterest() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(new BigDecimal("240000.00"));
        request.setInterestRate(new BigDecimal("0.0"));
        request.setDurationYears(20);
        
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.monthlyPayment").value(1000.00))
                .andExpect(jsonPath("$.totalInterest").value(0.00));
    }
    
    @Test
    void testCalculateMortgage_invalidInput_missingPrincipal() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setInterestRate(new BigDecimal("3.5"));
        request.setDurationYears(30);
        // Principal is null
        
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCalculateMortgage_invalidInput_negativePrincipal() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(new BigDecimal("-100000.00"));
        request.setInterestRate(new BigDecimal("3.5"));
        request.setDurationYears(30);
        
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCalculateMortgage_invalidInput_negativeInterestRate() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(new BigDecimal("300000.00"));
        request.setInterestRate(new BigDecimal("-1.0"));
        request.setDurationYears(30);
        
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCalculateMortgage_invalidInput_zeroDuration() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(new BigDecimal("300000.00"));
        request.setInterestRate(new BigDecimal("3.5"));
        request.setDurationYears(0);
        
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}