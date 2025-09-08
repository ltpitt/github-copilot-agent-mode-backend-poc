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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mortgage")
@RequiredArgsConstructor
@Tag(name = "Mortgage Calculator", description = "API for mortgage calculations")
public class MortgageCalculatorController {
    
    private final MortgageCalculationService mortgageCalculationService;
    
    @PostMapping("/calculate")
    @Operation(summary = "Calculate mortgage payment", description = "Calculate monthly payment and total interest for a mortgage loan")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful calculation",
                content = @Content(schema = @Schema(implementation = MortgageCalculationResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<MortgageCalculationResponse> calculateMortgage(
            @Valid @RequestBody MortgageCalculationRequest request) {
        
        MortgageCalculationResponse response = mortgageCalculationService.calculateMortgage(request);
        return ResponseEntity.ok(response);
    }
}