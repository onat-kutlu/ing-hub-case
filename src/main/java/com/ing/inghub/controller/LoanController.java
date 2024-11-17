package com.ing.inghub.controller;

import com.ing.inghub.dto.loan.CreateLoanRequest;
import com.ing.inghub.dto.loan.CreateLoanResponse;
import com.ing.inghub.exception.IngException;
import com.ing.inghub.service.loan.LoanService;
import com.ing.inghub.validation.CreateLoanRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/loan")
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/create")
    @Operation(method = "POST", summary = "Create Loan", description = "Create Loan")
    public ResponseEntity<CreateLoanResponse> create(@Valid @RequestBody final CreateLoanRequest request)
            throws IngException {
        CreateLoanRequestValidator.validate(request);
        CreateLoanResponse response = loanService.create(request);
        return ResponseEntity.ok().body(response);
    }
}
