package com.ing.inghub.service.loan.impl;

import com.ing.inghub.constants.errorcodes.ErrorCodes;
import com.ing.inghub.dto.loan.CreateLoanRequest;
import com.ing.inghub.dto.loan.CreateLoanResponse;
import com.ing.inghub.dto.loan.LoanDto;
import com.ing.inghub.exception.IngException;
import com.ing.inghub.mapper.LoanMapper;
import com.ing.inghub.model.Customer;
import com.ing.inghub.model.Loan;
import com.ing.inghub.model.LoanInstallment;
import com.ing.inghub.repository.LoanInstallmentRepository;
import com.ing.inghub.repository.LoanRepository;
import com.ing.inghub.service.customer.CustomerService;
import com.ing.inghub.service.loan.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final CustomerService customerService;
    private final LoanRepository loanRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;

    @Override
    public CreateLoanResponse create(CreateLoanRequest createLoanRequest) throws IngException {
        Customer customer = customerService.findByCustomerId(createLoanRequest.getCustomerId()).orElseThrow(() -> new IngException(ErrorCodes.E_CUSTOMER_NOT_FOUND));
        checkCustomerLimits(customer, createLoanRequest.getLoanAmount());
        CreateLoanResponse createLoanResponse = new CreateLoanResponse();
        createLoanResponse.setLoan(createLoan(createLoanRequest));
        return createLoanResponse;
    }

    private void checkCustomerLimits(Customer customer, Long loanAmount) throws IngException {
        if (customer.getCreditLimit().compareTo(loanAmount) < 0) {
            throw new IngException(ErrorCodes.E_NOT_ENOUGH_CUSTOMER_LIMIT);
        }
    }

    private LoanDto createLoan(CreateLoanRequest createLoanRequest) {
        Loan loan = new Loan();
        loan.setCustomerId(createLoanRequest.getCustomerId());
        loan.setLoanAmount(createLoanRequest.getLoanAmount());
        loan.setNumberOfInstallment(createLoanRequest.getInstallmentCount());
        loan.setInterestRate(createLoanRequest.getInterestRate());
        loan.setCreateDate(Instant.now());
        loan.setLoanAmountWithInterest(calculateLoanAmountWithInterest(loan.getLoanAmount(), loan.getInterestRate()));
        loan.setPaid(false);
        loan = loanRepository.save(loan);
        createInstallments(loan);
        return LoanMapper.from(loan);
    }

    private Long calculateLoanAmountWithInterest(Long loanAmount, Double interestRate) {
        return (long) (loanAmount * (1 + interestRate));
    }

    private void createInstallments(Loan loan) {
        List<LoanInstallment> installmentList = new ArrayList<>();
        for (int installmentOrder = 0; installmentOrder < loan.getNumberOfInstallment(); installmentOrder++) {
            LoanInstallment installment = new LoanInstallment();
            installment.setLoanId(loan.getId());
            installment.setAmount(loan.getLoanAmountWithInterest() / loan.getNumberOfInstallment());
            installment.setOrder(installmentOrder);
            installment.setPaid(false);
            installment.setPaidAmount(null);
            installment.setPaymentDate(null);
            installment.setDueDate(calculateDueDate(installmentOrder));
            installmentList.add(installment);
            if (installmentOrder == 0) {
                loan.setNextInstallmentDate(installment.getDueDate());
                loanRepository.save(loan);
            }
        }
        loanInstallmentRepository.saveAll(installmentList);
    }

    private Instant calculateDueDate(Integer installmentCount) {
        Instant firstInstallmentDueDate = LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        return addMonths(firstInstallmentDueDate, installmentCount);
    }

    private Instant addMonths(Instant instant, Integer months) {
        return instant.atZone(ZoneId.systemDefault()).plusMonths(months).toInstant();
    }

}
