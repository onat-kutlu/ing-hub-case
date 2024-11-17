package com.ing.inghub.service.loan;

import com.ing.inghub.dto.loan.CreateLoanRequest;
import com.ing.inghub.dto.loan.CreateLoanResponse;
import com.ing.inghub.exception.IngException;

public interface LoanService {

    CreateLoanResponse create(CreateLoanRequest createLoanRequest) throws IngException;
}
