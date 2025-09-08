package com.example.mortgage_calculator.service;

import com.example.mortgage_calculator.dto.MortgageCalculationRequest;
import com.example.mortgage_calculator.dto.MortgageCalculationResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class MortgageCalculationService {
    
    private static final MathContext MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_UP);
    
    public MortgageCalculationResponse calculateMortgage(MortgageCalculationRequest request) {
        BigDecimal principal = request.getPrincipal();
        BigDecimal annualInterestRate = request.getInterestRate();
        Integer durationYears = request.getDurationYears();
        
        // Calculate total number of payments (months)
        int totalPayments = durationYears * 12;
        
        BigDecimal monthlyPayment;
        
        // Handle zero interest rate case
        if (annualInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            monthlyPayment = principal.divide(new BigDecimal(totalPayments), 2, RoundingMode.HALF_UP);
        } else {
            // Convert annual interest rate to monthly rate (percentage to decimal then divide by 12)
            BigDecimal monthlyInterestRate = annualInterestRate
                .divide(new BigDecimal(100), MATH_CONTEXT)
                .divide(new BigDecimal(12), MATH_CONTEXT);
            
            // Calculate (1 + r)^n
            BigDecimal onePlusR = BigDecimal.ONE.add(monthlyInterestRate);
            BigDecimal onePlusRPowerN = onePlusR.pow(totalPayments, MATH_CONTEXT);
            
            // Calculate monthly payment using formula: M = P * (r * (1 + r)^n) / ((1 + r)^n - 1)
            BigDecimal numerator = principal.multiply(monthlyInterestRate.multiply(onePlusRPowerN, MATH_CONTEXT), MATH_CONTEXT);
            BigDecimal denominator = onePlusRPowerN.subtract(BigDecimal.ONE);
            
            monthlyPayment = numerator.divide(denominator, 2, RoundingMode.HALF_UP);
        }
        
        // Calculate total amount paid and total interest
        BigDecimal totalAmountPaid = monthlyPayment.multiply(new BigDecimal(totalPayments));
        BigDecimal totalInterest = totalAmountPaid.subtract(principal);
        
        MortgageCalculationResponse response = new MortgageCalculationResponse();
        response.setMonthlyPayment(monthlyPayment);
        response.setTotalInterest(totalInterest);
        
        return response;
    }
}