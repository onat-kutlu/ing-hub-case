package com.ing.inghub.mapper;

import com.ing.inghub.dto.loan.InstallmentDto;
import com.ing.inghub.dto.loan.LoanDto;
import com.ing.inghub.model.Loan;
import com.ing.inghub.model.LoanInstallment;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LoanMapper {

    public static LoanDto from(final Loan loan) {
        return new LoanDto()
                .setId(loan.getId())
                .setCreatedAt(loan.getCreateDate())
                .setCustomerId(loan.getCustomerId())
                .setLoanAmount(loan.getLoanAmount())
                .setTotalAmount(loan.getLoanAmountWithInterest())
                .setSingleInstallmentAmount(loan.getSingleInstallmentAmount())
                .setInstallmentCount(loan.getNumberOfInstallment())
                .setInterestRate(loan.getInterestRate())
                .setNextInstallmentDueDate(loan.getNextInstallmentDate())
                .setPaid(loan.isPaid());
    }

    public static InstallmentDto from(final LoanInstallment loanInstallment) {
        return new InstallmentDto()
                .setId(loanInstallment.getId())
                .setAmount(loanInstallment.getAmount())
                .setPaidAmount(loanInstallment.getPaidAmount())
                .setDueDate(loanInstallment.getDueDate())
                .setPaymentDate(loanInstallment.getPaymentDate())
                .setOrder(loanInstallment.getOrder())
                .setPaid(loanInstallment.isPaid());
    }
}
