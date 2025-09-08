package com.example.mortgage_calculator.service;

import com.example.mortgage_calculator.dto.MortgageCalculationRequest;
import com.example.mortgage_calculator.dto.MortgageCalculationResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class MortgageCalculationService {

    private static final int PRECISION = 10;
    private static final int SCALE = 2;
    private static final MathContext MATH_CONTEXT = new MathContext(PRECISION, RoundingMode.HALF_UP);

    public MortgageCalculationResponse calculateMortgage(MortgageCalculationRequest request) {
        BigDecimal principal = request.getPrincipal();
        BigDecimal annualInterestRate = request.getInterestRate();
        Integer durationYears = request.getDuration();

        // Convert to monthly values
        Integer totalPayments = durationYears * 12;
        BigDecimal monthlyInterestRate = annualInterestRate
                .divide(BigDecimal.valueOf(100), MATH_CONTEXT) // Convert percentage to decimal
                .divide(BigDecimal.valueOf(12), MATH_CONTEXT);   // Convert annual to monthly

        BigDecimal monthlyPayment;

        if (monthlyInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            // Special case: 0% interest rate
            monthlyPayment = principal.divide(BigDecimal.valueOf(totalPayments), SCALE, RoundingMode.HALF_UP);
        } else {
            // Standard mortgage formula: M = P * (r * (1 + r)^n) / ((1 + r)^n - 1)
            BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyInterestRate);
            BigDecimal onePlusRatePowerN = onePlusRate.pow(totalPayments, MATH_CONTEXT);
            
            BigDecimal numerator = principal.multiply(
                monthlyInterestRate.multiply(onePlusRatePowerN, MATH_CONTEXT), 
                MATH_CONTEXT
            );
            
            BigDecimal denominator = onePlusRatePowerN.subtract(BigDecimal.ONE);
            
            monthlyPayment = numerator.divide(denominator, SCALE, RoundingMode.HALF_UP);
        }

        // Calculate total interest
        BigDecimal totalPaid = monthlyPayment.multiply(BigDecimal.valueOf(totalPayments));
        BigDecimal totalInterest = totalPaid.subtract(principal);

        return new MortgageCalculationResponse(monthlyPayment, totalInterest);
    }
}