package com.ing.inghub.service.loan;

import com.ing.inghub.dto.loan.*;
import com.ing.inghub.exception.IngException;

public interface LoanService {

    CreateLoanResponse create(CreateLoanRequest createLoanRequest) throws IngException;

    ListLoanResponse getLoanByCustomerId(long customerId, int page, int size) throws IngException;

    ListInstallmentResponse getInstallmentByLoanId(long loanId, int page, int size) throws IngException;

    PaymentResponse payment(PaymentRequest createLoanRequest) throws IngException;
}
