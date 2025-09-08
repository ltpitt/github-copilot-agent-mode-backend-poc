package com.example.mortgage_calculator.service;

import com.example.mortgage_calculator.dto.MortgageCalculationRequest;
import com.example.mortgage_calculator.dto.MortgageCalculationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MortgageCalculationServiceTest {
    
    private MortgageCalculationService service;
    
    @BeforeEach
    void setUp() {
        service = new MortgageCalculationService();
    }
    
    @Test
    void testCalculateMortgage_withInterest() {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(new BigDecimal("300000.00"));
        request.setInterestRate(new BigDecimal("3.5"));
        request.setDurationYears(30);
        
        // When
        MortgageCalculationResponse response = service.calculateMortgage(request);
        
        // Then
        // Monthly payment should be approximately $1347.13
        assertEquals(0, response.getMonthlyPayment().compareTo(new BigDecimal("1347.13")));
        // Total interest should be approximately $184967 (360 payments * 1347.13 - 300000)
        assertEquals(0, response.getTotalInterest().compareTo(new BigDecimal("184966.80")));
    }
    
    @Test
    void testCalculateMortgage_zeroInterest() {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(new BigDecimal("240000.00"));
        request.setInterestRate(new BigDecimal("0.0"));
        request.setDurationYears(20);
        
        // When
        MortgageCalculationResponse response = service.calculateMortgage(request);
        
        // Then
        // Monthly payment should be 240000 / (20 * 12) = 1000
        assertEquals(0, response.getMonthlyPayment().compareTo(new BigDecimal("1000.00")));
        // Total interest should be 0 for zero interest
        assertEquals(0, response.getTotalInterest().compareTo(new BigDecimal("0.00")));
    }
    
    @Test
    void testCalculateMortgage_shortTerm() {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(new BigDecimal("100000.00"));
        request.setInterestRate(new BigDecimal("4.0"));
        request.setDurationYears(5);
        
        // When
        MortgageCalculationResponse response = service.calculateMortgage(request);
        
        // Then
        // For 5-year term, monthly payment should be approximately $1841.65
        assertEquals(0, response.getMonthlyPayment().compareTo(new BigDecimal("1841.65")));
        // Total interest should be approximately $10499
        assertEquals(0, response.getTotalInterest().compareTo(new BigDecimal("10499.00")));
    }
    
    @Test
    void testCalculateMortgage_highInterest() {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(new BigDecimal("200000.00"));
        request.setInterestRate(new BigDecimal("8.0"));
        request.setDurationYears(25);
        
        // When
        MortgageCalculationResponse response = service.calculateMortgage(request);
        
        // Then
        // Monthly payment should be approximately $1543.63
        assertEquals(0, response.getMonthlyPayment().compareTo(new BigDecimal("1543.63")));
        // Total interest should be approximately $263089
        assertEquals(0, response.getTotalInterest().compareTo(new BigDecimal("263089.00")));
    }
}