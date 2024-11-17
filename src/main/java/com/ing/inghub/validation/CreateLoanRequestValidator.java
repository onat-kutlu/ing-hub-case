package com.ing.inghub.validation;

import com.ing.inghub.constants.constants.Constants;
import com.ing.inghub.constants.errorcodes.ErrorCodes;
import com.ing.inghub.dto.loan.CreateLoanRequest;
import com.ing.inghub.exception.IngException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CreateLoanRequestValidator {

    public static void validate(final CreateLoanRequest createLoanRequest) throws IngException {
        validateInstallmentCount(createLoanRequest.getInstallmentCount());
        validateInterestRate(createLoanRequest.getInterestRate());
    }

    private void validateInstallmentCount(Integer installmentCount) throws IngException {
        if (!Constants.AVAILABLE_INSTALLMENT_COUNT_LUST.contains(installmentCount)) {
            throw new IngException(ErrorCodes.E_INVALID_INSTALLMENT_COUNT);
        }
    }

    private void validateInterestRate(Double interestRate) throws IngException {
        if (interestRate < Constants.INTEREST_RATE_LOWER_LIMIT || interestRate > Constants.INTEREST_RATE_UPPER_LIMIT) {
            throw new IngException(ErrorCodes.E_INVALID_INTEREST_RATE);
        }
    }

}
