package com.ing.inghub.validation;


import com.ing.inghub.constants.errorcodes.ErrorCodes;
import com.ing.inghub.dto.loan.CreateLoanRequest;
import com.ing.inghub.exception.IngException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateLoanRequestValidatorTests {


    @Test
    public void validateRequest_when_success() {
        //given
        CreateLoanRequest request = new CreateLoanRequest();
        request.setInstallmentCount(12);
        request.setInterestRate(0.2);

        //when & then
        assertDoesNotThrow(() -> CreateLoanRequestValidator.validate(request));
    }

    @Test
    public void validateRequest_when_invalidInstallmentCount() {
        // given
        CreateLoanRequest request = new CreateLoanRequest();
        request.setInstallmentCount(15);
        request.setInterestRate(0.2);

        //when
        IngException exception = assertThrows(IngException.class, () ->
                CreateLoanRequestValidator.validate(request));
        //then
        assertEquals(ErrorCodes.E_INVALID_INSTALLMENT_COUNT, exception.getCode());
    }

    @Test
    public void validateRequest_when_lowInterestRate() {
        //given
        CreateLoanRequest request = new CreateLoanRequest();
        request.setInstallmentCount(12);
        request.setInterestRate(0.0);

        //when
        IngException exception = assertThrows(IngException.class, () ->
                CreateLoanRequestValidator.validate(request));
        //then
        assertEquals(ErrorCodes.E_INVALID_INTEREST_RATE, exception.getCode());
    }

    @Test
    public void validateRequest_when_highInterestRate() {
        //given
        CreateLoanRequest request = new CreateLoanRequest();
        request.setInstallmentCount(12);
        request.setInterestRate(10.0);

        //when
        IngException exception = assertThrows(IngException.class, () ->
                CreateLoanRequestValidator.validate(request));
        //then
        assertEquals(ErrorCodes.E_INVALID_INTEREST_RATE, exception.getCode());
    }

}
