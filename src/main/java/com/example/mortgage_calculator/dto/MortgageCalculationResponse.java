package com.example.mortgage_calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing mortgage calculation results")
public class MortgageCalculationResponse {

    @Schema(description = "Monthly payment amount", example = "1432.25")
    private BigDecimal monthlyPayment;

    @Schema(description = "Total interest paid over the life of the loan", example = "215610.00")
    private BigDecimal totalInterest;
}