package com.example.mortgage_calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request for mortgage calculation")
public class MortgageCalculationRequest {

    @NotNull(message = "Principal amount is required")
    @DecimalMin(value = "0.01", message = "Principal must be greater than 0")
    @Schema(description = "The loan amount (principal)", example = "300000.00", minimum = "0.01")
    private BigDecimal principal;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.0", message = "Interest rate must be 0 or greater", inclusive = true)
    @Schema(description = "Annual interest rate as a percentage", example = "3.5", minimum = "0.0")
    private BigDecimal interestRate;

    @NotNull(message = "Duration is required")
    @DecimalMin(value = "1", message = "Duration must be at least 1 year")
    @Schema(description = "Loan duration in years", example = "30", minimum = "1")
    private Integer duration;
}