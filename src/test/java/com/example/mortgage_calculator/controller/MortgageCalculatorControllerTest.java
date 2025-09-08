package com.example.mortgage_calculator.controller;

import com.example.mortgage_calculator.dto.MortgageCalculationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
        request.setLoanAmount(new BigDecimal("300000.00"));
        request.setInterestRate(new BigDecimal("3.5"));
        request.setLoanTermYears(30);
        request.setLivingSituation("together");
        request.setMainIncome(new BigDecimal("75000.00"));
        request.setPartnerIncome(new BigDecimal("45000.00"));
        request.setEnergyLabel("B");
        request.setFixedInterestPeriod(10);
        
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.monthlyPayment").exists())
                .andExpect(jsonPath("$.totalInterest").exists())
                .andExpect(jsonPath("$.maxBorrowing").value(567000.00))
                .andExpect(jsonPath("$.advice").exists());
    }
    
    @Test
    void testCalculateMortgage_zeroInterest() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setLoanAmount(new BigDecimal("120000.00"));
        request.setInterestRate(new BigDecimal("0.0"));
        request.setLoanTermYears(10);
        request.setLivingSituation("alone");
        request.setMainIncome(new BigDecimal("50000.00"));
        request.setPartnerIncome(null);
        request.setEnergyLabel("A");
        request.setFixedInterestPeriod(10);
        
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.monthlyPayment").value(1000.00))
                .andExpect(jsonPath("$.totalInterest").value(0.00))
                .andExpect(jsonPath("$.maxBorrowing").value(236250.00))
                .andExpect(jsonPath("$.advice").exists());
    }
    
    @Test
    void testCalculateMortgage_invalidInput_missingLoanAmount() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setInterestRate(new BigDecimal("3.5"));
        request.setLoanTermYears(30);
        request.setLivingSituation("together");
        request.setMainIncome(new BigDecimal("75000.00"));
        request.setPartnerIncome(new BigDecimal("45000.00"));
        request.setEnergyLabel("B");
        request.setFixedInterestPeriod(10);
        // loanAmount is null
        
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCalculateMortgage_invalidInput_negativeLoanAmount() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setLoanAmount(new BigDecimal("-100000.00"));
        request.setInterestRate(new BigDecimal("3.5"));
        request.setLoanTermYears(30);
        request.setLivingSituation("together");
        request.setMainIncome(new BigDecimal("75000.00"));
        request.setPartnerIncome(new BigDecimal("45000.00"));
        request.setEnergyLabel("B");
        request.setFixedInterestPeriod(10);
        
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCalculateMortgage_invalidInput_invalidLivingSituation() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setLoanAmount(new BigDecimal("300000.00"));
        request.setInterestRate(new BigDecimal("3.5"));
        request.setLoanTermYears(30);
        request.setLivingSituation("invalid");
        request.setMainIncome(new BigDecimal("75000.00"));
        request.setPartnerIncome(new BigDecimal("45000.00"));
        request.setEnergyLabel("B");
        request.setFixedInterestPeriod(10);
        
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCalculateMortgage_invalidInput_missingMainIncome() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setLoanAmount(new BigDecimal("300000.00"));
        request.setInterestRate(new BigDecimal("3.5"));
        request.setLoanTermYears(30);
        request.setLivingSituation("alone");
        // mainIncome is null
        request.setEnergyLabel("C");
        request.setFixedInterestPeriod(10);
        
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCalculateMortgage_invalidInput_invalidEnergyLabel() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setLoanAmount(new BigDecimal("300000.00"));
        request.setInterestRate(new BigDecimal("3.5"));
        request.setLoanTermYears(30);
        request.setLivingSituation("alone");
        request.setMainIncome(new BigDecimal("60000.00"));
        request.setEnergyLabel("Z");
        request.setFixedInterestPeriod(10);
        
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCalculateMortgage_invalidInput_negativePartnerIncome() throws Exception {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setLoanAmount(new BigDecimal("300000.00"));
        request.setInterestRate(new BigDecimal("3.5"));
        request.setLoanTermYears(30);
        request.setLivingSituation("together");
        request.setMainIncome(new BigDecimal("75000.00"));
        request.setPartnerIncome(new BigDecimal("-10000.00"));
        request.setEnergyLabel("B");
        request.setFixedInterestPeriod(10);
        
        // When & Then
        mockMvc.perform(post("/api/mortgage/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}