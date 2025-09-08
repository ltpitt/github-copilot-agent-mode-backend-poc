package com.example.mortgage_calculator.service;

import com.example.mortgage_calculator.dto.MortgageCalculationRequest;
import com.example.mortgage_calculator.dto.MortgageCalculationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Should calculate mortgage with standard interest rate")
    void shouldCalculateMortgageWithStandardInterestRate() {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(BigDecimal.valueOf(300000));
        request.setInterestRate(BigDecimal.valueOf(3.5));
        request.setDuration(30);

        // When
        MortgageCalculationResponse response = service.calculateMortgage(request);

        // Then
        assertNotNull(response);
        assertNotNull(response.getMonthlyPayment());
        assertNotNull(response.getTotalInterest());

        // Verify monthly payment is approximately $1347.13 (known calculation result)
        BigDecimal expectedMonthlyPayment = BigDecimal.valueOf(1347.13);
        assertTrue(response.getMonthlyPayment().subtract(expectedMonthlyPayment).abs()
                .compareTo(BigDecimal.valueOf(0.01)) <= 0,
                "Monthly payment should be approximately " + expectedMonthlyPayment + 
                " but was " + response.getMonthlyPayment());

        // Verify total interest is positive and reasonable
        assertTrue(response.getTotalInterest().compareTo(BigDecimal.ZERO) > 0,
                "Total interest should be positive");
    }

    @Test
    @DisplayName("Should calculate mortgage with zero interest rate")
    void shouldCalculateMortgageWithZeroInterestRate() {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(BigDecimal.valueOf(240000));
        request.setInterestRate(BigDecimal.ZERO);
        request.setDuration(20);

        // When
        MortgageCalculationResponse response = service.calculateMortgage(request);

        // Then
        assertNotNull(response);
        assertNotNull(response.getMonthlyPayment());
        assertNotNull(response.getTotalInterest());

        // Monthly payment should be principal / total months: 240000 / (20 * 12) = 1000
        BigDecimal expectedMonthlyPayment = BigDecimal.valueOf(1000.00);
        assertEquals(0, expectedMonthlyPayment.compareTo(response.getMonthlyPayment()),
                "Monthly payment should be exactly " + expectedMonthlyPayment);

        // Total interest should be zero for 0% interest rate
        assertEquals(0, BigDecimal.ZERO.compareTo(response.getTotalInterest()),
                "Total interest should be zero for 0% interest rate");
    }

    @Test
    @DisplayName("Should calculate mortgage for short term loan")
    void shouldCalculateMortgageForShortTermLoan() {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(BigDecimal.valueOf(100000));
        request.setInterestRate(BigDecimal.valueOf(5.0));
        request.setDuration(5);

        // When
        MortgageCalculationResponse response = service.calculateMortgage(request);

        // Then
        assertNotNull(response);
        assertTrue(response.getMonthlyPayment().compareTo(BigDecimal.ZERO) > 0,
                "Monthly payment should be positive");
        assertTrue(response.getTotalInterest().compareTo(BigDecimal.ZERO) > 0,
                "Total interest should be positive");

        // For a shorter term, monthly payment should be higher
        assertTrue(response.getMonthlyPayment().compareTo(BigDecimal.valueOf(1500)) > 0,
                "Monthly payment for 5-year term should be relatively high");
    }

    @Test
    @DisplayName("Should calculate mortgage for high interest rate")
    void shouldCalculateMortgageForHighInterestRate() {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(BigDecimal.valueOf(200000));
        request.setInterestRate(BigDecimal.valueOf(15.0));
        request.setDuration(30);

        // When
        MortgageCalculationResponse response = service.calculateMortgage(request);

        // Then
        assertNotNull(response);
        assertTrue(response.getMonthlyPayment().compareTo(BigDecimal.ZERO) > 0,
                "Monthly payment should be positive");
        assertTrue(response.getTotalInterest().compareTo(BigDecimal.ZERO) > 0,
                "Total interest should be positive");

        // High interest rate should result in substantial total interest
        assertTrue(response.getTotalInterest().compareTo(response.getMonthlyPayment()) > 0,
                "Total interest should be significant for high interest rate");
    }

    @Test
    @DisplayName("Should handle small principal amounts")
    void shouldHandleSmallPrincipalAmounts() {
        // Given
        MortgageCalculationRequest request = new MortgageCalculationRequest();
        request.setPrincipal(BigDecimal.valueOf(1000));
        request.setInterestRate(BigDecimal.valueOf(4.0));
        request.setDuration(10);

        // When
        MortgageCalculationResponse response = service.calculateMortgage(request);

        // Then
        assertNotNull(response);
        assertTrue(response.getMonthlyPayment().compareTo(BigDecimal.ZERO) > 0,
                "Monthly payment should be positive for small principal");
        assertTrue(response.getTotalInterest().compareTo(BigDecimal.ZERO) >= 0,
                "Total interest should be non-negative");
    }
}