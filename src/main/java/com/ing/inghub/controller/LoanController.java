package com.ing.inghub.controller;

import com.ing.inghub.dto.loan.*;
import com.ing.inghub.exception.IngException;
import com.ing.inghub.service.loan.LoanService;
import com.ing.inghub.validation.CreateLoanRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/loans")
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

    @GetMapping("/byCustomer/{customerId}")
    @Operation(method = "GET", summary = "List Customer Loans", description = "List Customer Loans")
    public ResponseEntity<ListLoanResponse> getLoanByCustomerId(@PathVariable("customerId") long customerId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) throws IngException {
        ListLoanResponse response = loanService.getLoanByCustomerId(customerId, page, size);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/installmentsByLoanId/{loanId}")
    @Operation(method = "GET", summary = "List Installments For Given  Loan", description = "List Installments For Given  Loan")
    public ResponseEntity<ListInstallmentResponse> getInstallmentByLoanId(@PathVariable("loanId") long loanId,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "15") int size) throws IngException {
        ListInstallmentResponse response = loanService.getInstallmentByLoanId(loanId, page, size);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/payment")
    @Operation(method = "POST", summary = "Pay Loan", description = "Pay Loan")
    public ResponseEntity<PaymentResponse> payment(@Valid @RequestBody final PaymentRequest request)
            throws IngException {
        PaymentResponse response = loanService.payment(request);
        return ResponseEntity.ok().body(response);
    }
}
