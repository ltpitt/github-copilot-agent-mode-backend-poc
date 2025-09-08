package com.example.mortgage_calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request object for mortgage calculation")
public class MortgageCalculationRequest {
    
    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "0.01", message = "Loan amount must be positive")
    @Schema(description = "Principal loan amount", example = "300000.00", required = true)
    private BigDecimal loanAmount;
    
    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.0", message = "Interest rate must be non-negative")
    @Schema(description = "Annual interest rate as percentage", example = "3.5", required = true)
    private BigDecimal interestRate;
    
    @NotNull(message = "Loan term in years is required")
    @Min(value = 1, message = "Loan term must be at least 1 year")
    @Schema(description = "Loan term in years", example = "30", required = true)
    private Integer loanTermYears;
    
    @NotBlank(message = "Living situation is required")
    @Pattern(regexp = "^(alone|together)$", message = "Living situation must be 'alone' or 'together'")
    @Schema(description = "Living situation", allowableValues = {"alone", "together"}, example = "together", required = true)
    private String livingSituation;
    
    @NotNull(message = "Main income is required")
    @DecimalMin(value = "0.01", message = "Main income must be positive")
    @Schema(description = "Gross annual main income", example = "75000.00", required = true)
    private BigDecimal mainIncome;
    
    @DecimalMin(value = "0.0", message = "Partner income must be non-negative")
    @Schema(description = "Gross annual partner income (optional)", example = "45000.00", required = false)
    private BigDecimal partnerIncome;
    
    @NotBlank(message = "Energy label is required")
    @Pattern(regexp = "^[A-G]$", message = "Energy label must be one of: A, B, C, D, E, F, G")
    @Schema(description = "Property energy efficiency label", allowableValues = {"A", "B", "C", "D", "E", "F", "G"}, example = "B", required = true)
    private String energyLabel;
    
    @NotNull(message = "Fixed interest period is required")
    @Min(value = 1, message = "Fixed interest period must be at least 1 year")
    @Schema(description = "Fixed interest rate period in years", example = "10", required = true)
    private Integer fixedInterestPeriod;
}