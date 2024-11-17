package com.ing.inghub.mapper;

import com.ing.inghub.dto.loan.LoanDto;
import com.ing.inghub.model.Loan;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LoanMapper {

    public static LoanDto from(final Loan loan) {
        return new LoanDto()
                .setId(loan.getId())
                .setCreatedAt(loan.getCreateDate())
                .setTotalAmount(loan.getLoanAmountWithInterest())
                .setInstallmentCount(loan.getNumberOfInstallment())
                .setInterestRate(loan.getInterestRate())
                .setNextInstallmentDueDate(loan.getNextInstallmentDate());
    }
}
