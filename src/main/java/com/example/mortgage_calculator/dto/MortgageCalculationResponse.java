package com.example.mortgage_calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Response object containing mortgage calculation results")
public class MortgageCalculationResponse {
    
    @Schema(description = "Monthly payment amount", example = "1432.25")
    private BigDecimal monthlyPayment;
    
    @Schema(description = "Total interest paid over the life of the loan", example = "215610.00")
    private BigDecimal totalInterest;
    
    @Schema(description = "Maximum borrowing capacity based on income and energy efficiency", example = "337500.00")
    private BigDecimal maxBorrowing;
    
    @Schema(description = "Calculated advice based on the mortgage parameters", example = "Your requested loan amount is within your borrowing capacity. Consider the energy efficiency benefits.")
    private String advice;
}