package com.example.mortgage_calculator.controller;

import com.example.mortgage_calculator.dto.MortgageCalculationRequest;
import com.example.mortgage_calculator.dto.MortgageCalculationResponse;
import com.example.mortgage_calculator.service.MortgageCalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mortgage")
@Validated
@Tag(name = "Mortgage Calculator", description = "API for mortgage calculations")
public class MortgageCalculationController {

    private final MortgageCalculationService mortgageCalculationService;

    @Autowired
    public MortgageCalculationController(MortgageCalculationService mortgageCalculationService) {
        this.mortgageCalculationService = mortgageCalculationService;
    }

    @PostMapping("/calculate")
    @Operation(
        summary = "Calculate mortgage payment",
        description = "Calculate monthly payment and total interest for a mortgage loan based on principal amount, interest rate, and duration"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mortgage calculation successful",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = MortgageCalculationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters",
                content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<MortgageCalculationResponse> calculateMortgage(
            @Valid @RequestBody MortgageCalculationRequest request) {
        
        MortgageCalculationResponse response = mortgageCalculationService.calculateMortgage(request);
        return ResponseEntity.ok(response);
    }
}