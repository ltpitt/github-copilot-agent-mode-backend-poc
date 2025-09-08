package com.example.mortgage_calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Request for mortgage calculation")
public class MortgageCalculationRequest {

    @Schema(description = "Principal amount (loan amount)", example = "300000")
    @NotNull(message = "Principal is required")
    @Positive(message = "Principal must be positive")
    private BigDecimal principal;

    @Schema(description = "Annual interest rate as percentage", example = "3.5")
    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.0", message = "Interest rate must be non-negative")
    private BigDecimal interestRate;

    @Schema(description = "Loan duration in years", example = "30")
    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer durationYears;

    public MortgageCalculationRequest() {}

    public MortgageCalculationRequest(BigDecimal principal, BigDecimal interestRate, Integer durationYears) {
        this.principal = principal;
        this.interestRate = interestRate;
        this.durationYears = durationYears;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(Integer durationYears) {
        this.durationYears = durationYears;
    }
}