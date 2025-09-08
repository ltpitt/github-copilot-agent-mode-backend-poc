package com.example.mortgage_calculator.service;

import com.example.mortgage_calculator.dto.MortgageCalculationRequest;
import com.example.mortgage_calculator.dto.MortgageCalculationResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class MortgageCalculationService {

    /**
     * Calculate mortgage payment and total interest based on the given parameters.
     * Uses the formula: M = P * (r * (1 + r)^n) / ((1 + r)^n - 1)
     * Where:
     * M = Monthly payment
     * P = Principal (loan amount)
     * r = Monthly interest rate (annual rate / 12 / 100)
     * n = Total payments (years * 12)
     * 
     * If interest rate is 0, M = P / n
     */
    public MortgageCalculationResponse calculateMortgage(MortgageCalculationRequest request) {
        BigDecimal principal = request.getPrincipal();
        BigDecimal annualInterestRate = request.getInterestRate();
        Integer durationYears = request.getDurationYears();
        
        int totalPayments = durationYears * 12; // n
        BigDecimal monthlyPayment;
        
        // Handle zero interest rate case
        if (annualInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            monthlyPayment = principal.divide(BigDecimal.valueOf(totalPayments), 2, RoundingMode.HALF_UP);
        } else {
            // Convert annual interest rate to monthly decimal rate
            BigDecimal monthlyInterestRate = annualInterestRate
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP); // r
            
            // Calculate (1 + r)^n
            BigDecimal onePlusRatePowerN = BigDecimal.ONE.add(monthlyInterestRate).pow(totalPayments);
            
            // Calculate numerator: P * r * (1 + r)^n
            BigDecimal numerator = principal.multiply(monthlyInterestRate).multiply(onePlusRatePowerN);
            
            // Calculate denominator: (1 + r)^n - 1
            BigDecimal denominator = onePlusRatePowerN.subtract(BigDecimal.ONE);
            
            // Calculate monthly payment: M = numerator / denominator
            monthlyPayment = numerator.divide(denominator, 2, RoundingMode.HALF_UP);
        }
        
        // Calculate total amount paid and total interest
        BigDecimal totalAmountPaid = monthlyPayment.multiply(BigDecimal.valueOf(totalPayments));
        BigDecimal totalInterest = totalAmountPaid.subtract(principal);
        
        return new MortgageCalculationResponse(monthlyPayment, totalInterest);
    }
}