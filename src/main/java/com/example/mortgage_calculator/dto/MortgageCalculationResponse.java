package com.example.mortgage_calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Response from mortgage calculation")
public class MortgageCalculationResponse {

    @Schema(description = "Monthly payment amount", example = "1347.13")
    private BigDecimal monthlyPayment;

    @Schema(description = "Total interest paid over the life of the loan", example = "184,968.00")
    private BigDecimal totalInterest;

    public MortgageCalculationResponse() {}

    public MortgageCalculationResponse(BigDecimal monthlyPayment, BigDecimal totalInterest) {
        this.monthlyPayment = monthlyPayment;
        this.totalInterest = totalInterest;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }
}