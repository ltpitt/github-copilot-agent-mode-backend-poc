package com.example.mortgage_calculator.service;

import com.example.mortgage_calculator.dto.MortgageCalculationRequest;
import com.example.mortgage_calculator.dto.MortgageCalculationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MortgageCalculationServiceTest {
    
    private MortgageCalculationService service;
    
    @BeforeEach
    void setUp() {
        service = new MortgageCalculationService();
    }
    
    @Test
    void testCalculateMortgage_withInterest_livingTogether() {
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
        
        // When
        MortgageCalculationResponse response = service.calculateMortgage(request);
        
        // Then
        assertNotNull(response);
        assertNotNull(response.getMonthlyPayment());
        assertNotNull(response.getTotalInterest());
        assertNotNull(response.getMaxBorrowing());
        assertNotNull(response.getAdvice());
        
        // Verify monthly payment calculation (approximately 1347.13)
        assertTrue(response.getMonthlyPayment().compareTo(new BigDecimal("1340.00")) > 0);
        assertTrue(response.getMonthlyPayment().compareTo(new BigDecimal("1355.00")) < 0);
        
        // Verify max borrowing: (75000 + 45000) * 4.5 * 1.05 (B energy label) = 567000
        assertEquals(0, response.getMaxBorrowing().compareTo(new BigDecimal("567000.00")));
        
        // Verify advice contains relevant information
        assertFalse(response.getAdvice().isEmpty());
        assertTrue(response.getAdvice().contains("within your borrowing capacity"));
        assertTrue(response.getAdvice().contains("energy-efficient"));
    }
    
    @Test
    void testCalculateMortgage_withInterest_livingAlone() {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setLoanAmount(new BigDecimal("200000.00"));
        request.setInterestRate(new BigDecimal("4.0"));
        request.setLoanTermYears(25);
        request.setLivingSituation("alone");
        request.setMainIncome(new BigDecimal("60000.00"));
        request.setPartnerIncome(null);
        request.setEnergyLabel("C");
        request.setFixedInterestPeriod(15);
        
        // When
        MortgageCalculationResponse response = service.calculateMortgage(request);
        
        // Then
        assertNotNull(response);
        assertNotNull(response.getMonthlyPayment());
        assertNotNull(response.getTotalInterest());
        assertNotNull(response.getMaxBorrowing());
        assertNotNull(response.getAdvice());
        
        // Verify max borrowing: 60000 * 4.5 = 270000 (no energy bonus for C)
        assertEquals(0, response.getMaxBorrowing().compareTo(new BigDecimal("270000.00")));
        
        // Verify advice
        assertFalse(response.getAdvice().isEmpty());
        assertTrue(response.getAdvice().contains("within your borrowing capacity"));
    }
    
    @Test
    void testCalculateMortgage_withZeroInterest() {
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
        
        // When
        MortgageCalculationResponse response = service.calculateMortgage(request);
        
        // Then
        assertNotNull(response);
        // Monthly payment should be 120000 / 120 = 1000.00
        assertEquals(0, response.getMonthlyPayment().compareTo(new BigDecimal("1000.00")));
        assertEquals(0, response.getTotalInterest().compareTo(new BigDecimal("0.00")));
        
        // Max borrowing: 50000 * 4.5 * 1.05 (A energy label) = 236250.00
        assertEquals(0, response.getMaxBorrowing().compareTo(new BigDecimal("236250.00")));
        
        // Verify advice mentions energy efficiency
        assertTrue(response.getAdvice().contains("energy-efficient"));
    }
    
    @Test
    void testCalculateMortgage_exceedsMaxBorrowing() {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setLoanAmount(new BigDecimal("400000.00"));
        request.setInterestRate(new BigDecimal("3.0"));
        request.setLoanTermYears(30);
        request.setLivingSituation("alone");
        request.setMainIncome(new BigDecimal("60000.00"));
        request.setPartnerIncome(null);
        request.setEnergyLabel("G");
        request.setFixedInterestPeriod(5);
        
        // When
        MortgageCalculationResponse response = service.calculateMortgage(request);
        
        // Then
        assertNotNull(response);
        // Max borrowing: 60000 * 4.5 = 270000 (no bonus for G)
        assertEquals(0, response.getMaxBorrowing().compareTo(new BigDecimal("270000.00")));
        
        // Verify advice mentions exceeding capacity
        assertTrue(response.getAdvice().contains("exceeds your borrowing capacity"));
        assertTrue(response.getAdvice().contains("improving the property's energy efficiency"));
        assertTrue(response.getAdvice().contains("shorter fixed interest period"));
    }
}