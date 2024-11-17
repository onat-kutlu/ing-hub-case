package com.ing.inghub.service.loan.impl;

import com.ing.inghub.constants.constants.Constants;
import com.ing.inghub.constants.errorcodes.ErrorCodes;
import com.ing.inghub.dto.base.ServiceResult;
import com.ing.inghub.dto.loan.*;
import com.ing.inghub.enums.UserRole;
import com.ing.inghub.exception.IngException;
import com.ing.inghub.mapper.LoanMapper;
import com.ing.inghub.model.Customer;
import com.ing.inghub.model.Loan;
import com.ing.inghub.model.LoanInstallment;
import com.ing.inghub.repository.LoanInstallmentRepository;
import com.ing.inghub.repository.LoanRepository;
import com.ing.inghub.service.customer.CustomerService;
import com.ing.inghub.service.loan.LoanService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final CustomerService customerService;
    private final LoanRepository loanRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;

    @Override
    @Transactional
    public CreateLoanResponse create(CreateLoanRequest createLoanRequest) throws IngException {
        Customer customer = customerService.findByCustomerId(createLoanRequest.getCustomerId()).orElseThrow(() -> new IngException(ErrorCodes.E_CUSTOMER_NOT_FOUND));
        checkCustomerLimits(customer, createLoanRequest.getLoanAmount());
        CreateLoanResponse createLoanResponse = new CreateLoanResponse();
        createLoanResponse.setServiceResult(ServiceResult.getSuccessResult());
        createLoanResponse.setLoan(createLoan(createLoanRequest));
        return createLoanResponse;
    }

    @Override
    public ListLoanResponse getLoanByCustomerId(long customerId, int page, int size) throws IngException {
        validateUser(customerId);
        List<Loan> loanList = loanRepository.findAllByCustomerId(customerId, PageRequest.of(page, size));
        ListLoanResponse listLoanResponse = new ListLoanResponse();
        listLoanResponse.setServiceResult(ServiceResult.getSuccessResult());
        listLoanResponse.setLoans(loanList.stream().map(LoanMapper::from).collect(Collectors.toList()));
        return listLoanResponse;
    }


    @Override
    public ListInstallmentResponse getInstallmentByLoanId(long loanId, int page, int size) throws IngException {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new IngException(ErrorCodes.E_LOAN_NOT_FOUND));
        validateUser(loan.getCustomerId());
        List<LoanInstallment> loanInstallmentList = loanInstallmentRepository.findAllByLoanIdOrderByOrder(loanId, PageRequest.of(page, size));
        ListInstallmentResponse listInstallmentResponse = new ListInstallmentResponse();
        listInstallmentResponse.setServiceResult(ServiceResult.getSuccessResult());
        listInstallmentResponse.setInstallments(loanInstallmentList.stream().map(LoanMapper::from).collect(Collectors.toList()));
        return listInstallmentResponse;
    }

    @Override
    @Transactional
    public PaymentResponse payment(PaymentRequest paymentRequest) throws IngException {
        Loan loan = loanRepository.findById(paymentRequest.getLoanId()).orElseThrow(() -> new IngException(ErrorCodes.E_LOAN_NOT_FOUND));
        validateUser(loan.getCustomerId());
        Customer customer = customerService.findByCustomerId(loan.getCustomerId()).orElseThrow(() -> new IngException(ErrorCodes.E_CUSTOMER_NOT_FOUND));
        List<LoanInstallment> notPaidInstallmentList = loanInstallmentRepository.findAllByLoanIdAndPaidIsFalseOrderByOrder(loan.getId());
        PaymentDto paymentDto = payInstallments(notPaidInstallmentList, customer, paymentRequest.getPaymentAmount(), loan);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setServiceResult(ServiceResult.getSuccessResult());
        paymentResponse.setPaymentDto(paymentDto);
        return paymentResponse;
    }


    private PaymentDto payInstallments(List<LoanInstallment> notPaidInstallmentList, Customer customer, Long paymentAmount, Loan loan) {
        Instant payableDateUntil = calculateDueDate(Constants.MAX_INSTALLMENT_COUNT_FOR_SINGLE_PAYMENT);
        List<LoanInstallment> installmentsUntilPayableDate = notPaidInstallmentList.stream()
                .filter(installment -> installment.getDueDate().equals(payableDateUntil) || installment.getDueDate().isBefore(payableDateUntil)).toList();
        Integer paidInstallmentCount = 0;
        Long totalAmountSpent = 0L;
        Long amountLeft = paymentAmount;
        for (LoanInstallment installment : installmentsUntilPayableDate) {
            if (amountLeft.compareTo(installment.getAmount()) >= 0) {
                doPayment(installment, customer);
                totalAmountSpent += installment.getAmount();
                paidInstallmentCount++;
                amountLeft -= installment.getAmount();
            }
        }
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setAmountSpent(totalAmountSpent);
        paymentDto.setPaidInstallmentCount(paidInstallmentCount);
        updateLoan(loan, paymentDto);
        return paymentDto;
    }

    private void updateLoan(Loan loan, PaymentDto paymentDto) {
        List<LoanInstallment> loanInstallmentList = loanInstallmentRepository.findAllByLoanIdAndPaidIsFalseOrderByOrder(loan.getId());
        paymentDto.setRemainingInstallmentCount(loanInstallmentList.size());
        paymentDto.setLoanId(loan.getId());
        if (loanInstallmentList.isEmpty()) {
            loan.setPaid(true);
            paymentDto.setFullyPaid(true);
        } else {
            loan.setNextInstallmentDate(loanInstallmentList.getFirst().getDueDate());
            paymentDto.setFullyPaid(false);
        }
        loanRepository.save(loan);

    }

    private void doPayment(LoanInstallment installment, Customer customer) {
        updateInstallment(installment);
        updateCustomer(customer, installment.getAmount());
    }

    private void updateInstallment(LoanInstallment installment) {
        installment.setPaid(true);
        installment.setPaidAmount(installment.getAmount());
        installment.setPaymentDate(Instant.now());
        loanInstallmentRepository.save(installment);
    }

    private void updateCustomer(Customer customer, Long paidAmount) {
        customer.setCreditLimit(customer.getCreditLimit() + paidAmount);
        customer.setUsedCreditLimit(customer.getUsedCreditLimit() - paidAmount);
        customerService.save(customer);
    }


    private void validateUser(long customerId) throws IngException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerService.findByUsername(userDetails.getUsername()).orElseThrow(() -> new IngException(ErrorCodes.E_INVALID_ACCESS));
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + UserRole.ADMIN.name())) && !customer.getId().equals(customerId)) {
            throw new IngException(ErrorCodes.E_INVALID_ACCESS);
        }
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
        loan.setSingleInstallmentAmount(calculateSingleInstallmentAmount(loan.getLoanAmountWithInterest(), loan.getNumberOfInstallment()));
        loan.setPaid(false);
        loan = loanRepository.save(loan);
        createInstallments(loan);
        return LoanMapper.from(loan);
    }

    private Long calculateLoanAmountWithInterest(Long loanAmount, Double interestRate) {
        return (long) (loanAmount * (1 + interestRate));
    }

    private Long calculateSingleInstallmentAmount(Long loanAmountWithInterest, Integer installmentCount) {
        return loanAmountWithInterest / installmentCount;
    }

    private void createInstallments(Loan loan) {
        List<LoanInstallment> installmentList = new ArrayList<>();
        for (int installmentOrder = 1; installmentOrder <= loan.getNumberOfInstallment(); installmentOrder++) {
            LoanInstallment installment = new LoanInstallment();
            installment.setLoanId(loan.getId());
            installment.setAmount(loan.getLoanAmountWithInterest() / loan.getNumberOfInstallment());
            installment.setOrder(installmentOrder);
            installment.setPaid(false);
            installment.setPaidAmount(null);
            installment.setPaymentDate(null);
            installment.setDueDate(calculateDueDate(installmentOrder));
            installmentList.add(installment);
            if (installmentOrder == 1) {
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
