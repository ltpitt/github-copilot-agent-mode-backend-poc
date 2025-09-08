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
    void calculateMortgage_withStandardLoan_returnsCorrectPayment() {
        // Arrange
        MortgageCalculationRequest request = new MortgageCalculationRequest(
            new BigDecimal("300000"), // principal
            new BigDecimal("3.5"),    // interest rate 3.5%
            30                        // 30 years
        );

        // Act
        MortgageCalculationResponse response = service.calculateMortgage(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getMonthlyPayment());
        assertNotNull(response.getTotalInterest());

        // Expected monthly payment for $300,000 at 3.5% for 30 years is approximately $1347.13
        BigDecimal expectedMonthlyPayment = new BigDecimal("1347.13");
        assertEquals(0, expectedMonthlyPayment.compareTo(response.getMonthlyPayment()));

        // Total interest should be positive
        assertTrue(response.getTotalInterest().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void calculateMortgage_withZeroInterestRate_returnsCorrectPayment() {
        // Arrange
        MortgageCalculationRequest request = new MortgageCalculationRequest(
            new BigDecimal("120000"), // principal
            BigDecimal.ZERO,          // 0% interest rate
            10                        // 10 years
        );

        // Act
        MortgageCalculationResponse response = service.calculateMortgage(request);

        // Assert
        assertNotNull(response);
        
        // With 0% interest, monthly payment should be principal / (years * 12)
        BigDecimal expectedMonthlyPayment = new BigDecimal("1000.00"); // 120000 / (10 * 12) = 1000
        assertEquals(0, expectedMonthlyPayment.compareTo(response.getMonthlyPayment()));

        // Total interest should be zero
        assertEquals(0, BigDecimal.ZERO.compareTo(response.getTotalInterest()));
    }

    @Test
    void calculateMortgage_withHighInterestRate_returnsCorrectPayment() {
        // Arrange
        MortgageCalculationRequest request = new MortgageCalculationRequest(
            new BigDecimal("200000"), // principal
            new BigDecimal("8.0"),    // 8% interest rate
            15                        // 15 years
        );

        // Act
        MortgageCalculationResponse response = service.calculateMortgage(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getMonthlyPayment());
        assertNotNull(response.getTotalInterest());

        // Monthly payment should be higher than with lower interest rate
        assertTrue(response.getMonthlyPayment().compareTo(new BigDecimal("1500")) > 0);
        
        // Total interest should be positive
        assertTrue(response.getTotalInterest().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void calculateMortgage_withShortTerm_returnsCorrectPayment() {
        // Arrange
        MortgageCalculationRequest request = new MortgageCalculationRequest(
            new BigDecimal("100000"), // principal
            new BigDecimal("4.5"),    // 4.5% interest rate
            5                         // 5 years
        );

        // Act
        MortgageCalculationResponse response = service.calculateMortgage(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getMonthlyPayment());
        assertNotNull(response.getTotalInterest());

        // Monthly payment should be higher due to shorter term
        assertTrue(response.getMonthlyPayment().compareTo(new BigDecimal("1500")) > 0);
        
        // Total interest should be lower due to shorter term
        assertTrue(response.getTotalInterest().compareTo(new BigDecimal("30000")) < 0);
    }

    @Test
    void calculateMortgage_withSmallPrincipal_returnsCorrectPayment() {
        // Arrange
        MortgageCalculationRequest request = new MortgageCalculationRequest(
            new BigDecimal("50000"),  // principal
            new BigDecimal("2.5"),    // 2.5% interest rate
            20                        // 20 years
        );

        // Act
        MortgageCalculationResponse response = service.calculateMortgage(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getMonthlyPayment());
        assertNotNull(response.getTotalInterest());

        // Monthly payment should be reasonable for smaller loan
        assertTrue(response.getMonthlyPayment().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(response.getMonthlyPayment().compareTo(new BigDecimal("1000")) < 0);
        
        // Total interest should be positive but reasonable
        assertTrue(response.getTotalInterest().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void calculateMortgage_verifyTotalCalculation() {
        // Arrange
        MortgageCalculationRequest request = new MortgageCalculationRequest(
            new BigDecimal("100000"), // principal
            new BigDecimal("5.0"),    // 5% interest rate
            10                        // 10 years
        );

        // Act
        MortgageCalculationResponse response = service.calculateMortgage(request);

        // Assert
        assertNotNull(response);
        
        // Verify that total interest = (monthly payment * number of payments) - principal
        BigDecimal totalPaid = response.getMonthlyPayment().multiply(new BigDecimal("120")); // 10 years * 12 months
        BigDecimal calculatedTotalInterest = totalPaid.subtract(new BigDecimal("100000"));
        
        assertEquals(0, calculatedTotalInterest.compareTo(response.getTotalInterest()));
    }
}