package com.example.mortgage_calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request object for mortgage calculation")
public class MortgageCalculationRequest {
    
    @NotNull(message = "Principal amount is required")
    @DecimalMin(value = "0.01", message = "Principal amount must be positive")
    @Schema(description = "Principal loan amount", example = "300000.00", required = true)
    private BigDecimal principal;
    
    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.0", message = "Interest rate must be non-negative")
    @Schema(description = "Annual interest rate as percentage", example = "3.5", required = true)
    private BigDecimal interestRate;
    
    @NotNull(message = "Duration in years is required")
    @DecimalMin(value = "1", message = "Duration must be at least 1 year")
    @Schema(description = "Loan duration in years", example = "30", required = true)
    private Integer durationYears;
}