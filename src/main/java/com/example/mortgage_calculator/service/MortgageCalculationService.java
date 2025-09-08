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
    private static final BigDecimal BORROWING_MULTIPLIER = new BigDecimal("4.5");
    private static final BigDecimal ENERGY_EFFICIENCY_BONUS = new BigDecimal("1.05"); // 5% bonus for A/B labels
    
    public MortgageCalculationResponse calculateMortgage(MortgageCalculationRequest request) {
        BigDecimal loanAmount = request.getLoanAmount();
        BigDecimal annualInterestRate = request.getInterestRate();
        Integer loanTermYears = request.getLoanTermYears();
        
        // Calculate monthly payment
        BigDecimal monthlyPayment = calculateMonthlyPayment(loanAmount, annualInterestRate, loanTermYears);
        
        // Calculate total interest
        BigDecimal totalInterest = calculateTotalInterest(monthlyPayment, loanAmount, loanTermYears);
        
        // Calculate maximum borrowing capacity
        BigDecimal maxBorrowing = calculateMaxBorrowing(request);
        
        // Generate advice
        String advice = generateAdvice(request, loanAmount, maxBorrowing, monthlyPayment);
        
        MortgageCalculationResponse response = new MortgageCalculationResponse();
        response.setMonthlyPayment(monthlyPayment);
        response.setTotalInterest(totalInterest);
        response.setMaxBorrowing(maxBorrowing);
        response.setAdvice(advice);
        
        return response;
    }
    
    private BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal annualInterestRate, Integer durationYears) {
        // Calculate total number of payments (months)
        int totalPayments = durationYears * 12;
        
        // Handle zero interest rate case
        if (annualInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(new BigDecimal(totalPayments), 2, RoundingMode.HALF_UP);
        }
        
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
        
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateTotalInterest(BigDecimal monthlyPayment, BigDecimal principal, Integer durationYears) {
        int totalPayments = durationYears * 12;
        BigDecimal totalAmountPaid = monthlyPayment.multiply(new BigDecimal(totalPayments));
        return totalAmountPaid.subtract(principal);
    }
    
    private BigDecimal calculateMaxBorrowing(MortgageCalculationRequest request) {
        BigDecimal totalIncome;
        
        // Calculate total household income based on living situation
        if ("together".equals(request.getLivingSituation())) {
            BigDecimal partnerIncome = request.getPartnerIncome() != null ? request.getPartnerIncome() : BigDecimal.ZERO;
            totalIncome = request.getMainIncome().add(partnerIncome);
        } else {
            totalIncome = request.getMainIncome();
        }
        
        // Base borrowing capacity (income * 4.5)
        BigDecimal baseBorrowing = totalIncome.multiply(BORROWING_MULTIPLIER);
        
        // Apply energy efficiency bonus for A/B labels
        if ("A".equals(request.getEnergyLabel()) || "B".equals(request.getEnergyLabel())) {
            baseBorrowing = baseBorrowing.multiply(ENERGY_EFFICIENCY_BONUS);
        }
        
        return baseBorrowing.setScale(2, RoundingMode.HALF_UP);
    }
    
    private String generateAdvice(MortgageCalculationRequest request, BigDecimal loanAmount, BigDecimal maxBorrowing, BigDecimal monthlyPayment) {
        StringBuilder advice = new StringBuilder();
        
        // Check if loan amount is within borrowing capacity
        if (loanAmount.compareTo(maxBorrowing) <= 0) {
            advice.append("Your requested loan amount is within your borrowing capacity. ");
        } else {
            BigDecimal excess = loanAmount.subtract(maxBorrowing);
            advice.append(String.format("Your requested loan amount exceeds your borrowing capacity by €%.2f. Consider reducing the loan amount. ", excess));
        }
        
        // Provide energy efficiency advice
        if ("A".equals(request.getEnergyLabel()) || "B".equals(request.getEnergyLabel())) {
            advice.append("Excellent choice on the energy-efficient property! This has increased your borrowing capacity by 5%. ");
        } else if ("F".equals(request.getEnergyLabel()) || "G".equals(request.getEnergyLabel())) {
            advice.append("Consider improving the property's energy efficiency - better ratings (A or B) can increase your borrowing capacity. ");
        }
        
        // Provide interest rate period advice
        if (request.getFixedInterestPeriod() < 10) {
            advice.append("You've chosen a shorter fixed interest period. Consider the risk of interest rate changes after this period. ");
        } else if (request.getFixedInterestPeriod() >= 20) {
            advice.append("Good choice on a longer fixed interest period - this provides stability against interest rate fluctuations. ");
        }
        
        // Income-to-payment ratio advice
        BigDecimal totalIncome = "together".equals(request.getLivingSituation()) 
            ? request.getMainIncome().add(request.getPartnerIncome() != null ? request.getPartnerIncome() : BigDecimal.ZERO)
            : request.getMainIncome();
        BigDecimal monthlyIncome = totalIncome.divide(new BigDecimal(12), 2, RoundingMode.HALF_UP);
        BigDecimal paymentRatio = monthlyPayment.divide(monthlyIncome, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        
        if (paymentRatio.compareTo(new BigDecimal(30)) > 0) {
            advice.append("Your monthly payment represents more than 30% of your monthly income. Consider a longer loan term or smaller amount.");
        } else if (paymentRatio.compareTo(new BigDecimal(25)) <= 0) {
            advice.append("Your monthly payment to income ratio is very comfortable.");
        }
        
        return advice.toString().trim();
    }
}