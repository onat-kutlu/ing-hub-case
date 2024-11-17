package com.ing.inghub.service.loan;

import com.ing.inghub.constants.errorcodes.ErrorCodes;
import com.ing.inghub.dto.loan.*;
import com.ing.inghub.enums.UserRole;
import com.ing.inghub.exception.IngException;
import com.ing.inghub.model.Customer;
import com.ing.inghub.model.Loan;
import com.ing.inghub.model.LoanInstallment;
import com.ing.inghub.repository.LoanInstallmentRepository;
import com.ing.inghub.repository.LoanRepository;
import com.ing.inghub.service.customer.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class LoanServiceTests {
    @Autowired
    LoanService loanService;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    LoanInstallmentRepository loanInstallmentRepository;

    @MockBean
    CustomerService customerService;

    @BeforeEach
    void beforeEach() {
        loanRepository.deleteAll();
        loanInstallmentRepository.deleteAll();
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void createLoan_when_customerNotfound() {
        //given
        CreateLoanRequest createLoanRequest = new CreateLoanRequest();
        createLoanRequest.setCustomerId(1L);
        Mockito.doReturn(Optional.empty()).when(customerService).findByCustomerId(Mockito.anyLong());
        //when
        IngException exception = Assertions.assertThrows(IngException.class, () -> loanService.create(createLoanRequest));
        //then
        Assertions.assertEquals(ErrorCodes.E_CUSTOMER_NOT_FOUND, exception.getCode());
    }

    @Test
    void createLoan_when_customerHaveInsufficientLimit() {
        //given
        CreateLoanRequest createLoanRequest = new CreateLoanRequest();
        createLoanRequest.setCustomerId(1L);
        createLoanRequest.setLoanAmount(10000L);
        Mockito.doReturn(Optional.of(getCustomer().setCreditLimit(1L))).when(customerService).findByCustomerId(Mockito.anyLong());
        //when
        IngException exception = Assertions.assertThrows(IngException.class, () -> loanService.create(createLoanRequest));
        //then
        Assertions.assertEquals(ErrorCodes.E_NOT_ENOUGH_CUSTOMER_LIMIT, exception.getCode());
    }

    @Test
    void createLoan_test() throws IngException {
        //given
        CreateLoanRequest createLoanRequest = new CreateLoanRequest();
        createLoanRequest.setCustomerId(1L);
        createLoanRequest.setLoanAmount(10000L);
        createLoanRequest.setInterestRate(0.5);
        createLoanRequest.setInstallmentCount(6);
        Mockito.doReturn(Optional.of(getCustomer())).when(customerService).findByCustomerId(Mockito.anyLong());
        //when
        CreateLoanResponse response = loanService.create(createLoanRequest);
        //then
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getServiceResult());
        Assertions.assertEquals(ErrorCodes.SUCCESS_MESSAGE, response.getServiceResult().getMessage());
        Assertions.assertEquals(ErrorCodes.SUCCESS_CODE, response.getServiceResult().getCode());
        Assertions.assertNotNull(response.getLoan());
        Assertions.assertEquals(10000L, response.getLoan().getLoanAmount());
        Assertions.assertEquals(15000L, response.getLoan().getTotalAmount());
        Assertions.assertEquals(2500L, response.getLoan().getSingleInstallmentAmount());
        Assertions.assertEquals(6, response.getLoan().getInstallmentCount());
        Assertions.assertEquals(0.5, response.getLoan().getInterestRate());
        Assertions.assertTrue(response.getLoan().getNextInstallmentDueDate().isAfter(Instant.now()));
        Assertions.assertFalse(response.getLoan().isPaid());
        Loan loan = loanRepository.findById(response.getLoan().getId()).orElse(null);
        Assertions.assertNotNull(loan);
        Assertions.assertEquals(loan.getLoanAmount(), response.getLoan().getLoanAmount());
        Assertions.assertEquals(loan.getCustomerId(), response.getLoan().getCustomerId());
        Assertions.assertEquals(loan.getInterestRate(), response.getLoan().getInterestRate());
        Assertions.assertEquals(loan.getSingleInstallmentAmount(), response.getLoan().getSingleInstallmentAmount());
        Assertions.assertEquals(loan.getNumberOfInstallment(), response.getLoan().getInstallmentCount());
        Assertions.assertTrue(loan.getNextInstallmentDate().isAfter(Instant.now()));
        List<LoanInstallment> loanInstallments = loanInstallmentRepository.findAll();
        Assertions.assertNotNull(loanInstallments);
        Assertions.assertEquals(6, loanInstallments.size());
        Assertions.assertEquals(loan.getSingleInstallmentAmount(), loanInstallments.get(0).getAmount());
    }

    @Test
    void getLoanByCustomerId_when_userNotFound() {
        //given
        UserDetails customer = User.builder()
                .username("customer")
                .password("customerPassword")
                .roles(UserRole.CUSTOMER.name())
                .build();
        mockSecurityContext(customer);
        Mockito.doReturn(Optional.empty()).when(customerService).findByUsername(Mockito.anyString());
        //when
        IngException exception = Assertions.assertThrows(IngException.class, () -> loanService.getLoanByCustomerId(1L, 0, 10));
        //then
        Assertions.assertEquals(ErrorCodes.E_INVALID_ACCESS, exception.getCode());
    }

    @Test
    void getLoanByCustomerId_when_customerDontMatch() {
        //given
        UserDetails customer = User.builder()
                .username("customer")
                .password("customerPassword")
                .roles(UserRole.CUSTOMER.name())
                .build();
        mockSecurityContext(customer);
        Mockito.doReturn(Optional.of(getCustomer())).when(customerService).findByUsername(Mockito.anyString());
        //when
        IngException exception = Assertions.assertThrows(IngException.class, () -> loanService.getLoanByCustomerId(2L, 0, 10));
        //then
        Assertions.assertEquals(ErrorCodes.E_INVALID_ACCESS, exception.getCode());
    }

    @Test
    void getLoanByCustomerId_when_customerMatches() throws IngException {
        //given
        UserDetails customer = User.builder()
                .username("customer")
                .password("customerPassword")
                .roles(UserRole.CUSTOMER.name())
                .build();
        mockSecurityContext(customer);
        Mockito.doReturn(Optional.of(getCustomer())).when(customerService).findByUsername(Mockito.anyString());
        createLoan();
        //when
        ListLoanResponse response = loanService.getLoanByCustomerId(1L, 0, 10);
        //then
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getServiceResult());
        Assertions.assertEquals(ErrorCodes.SUCCESS_MESSAGE, response.getServiceResult().getMessage());
        Assertions.assertEquals(ErrorCodes.SUCCESS_CODE, response.getServiceResult().getCode());
        Assertions.assertNotNull(response.getLoans());
        Assertions.assertEquals(10000L, response.getLoans().get(0).getLoanAmount());
        Assertions.assertEquals(15000L, response.getLoans().get(0).getTotalAmount());
        Assertions.assertEquals(2500L, response.getLoans().get(0).getSingleInstallmentAmount());
        Assertions.assertEquals(6, response.getLoans().get(0).getInstallmentCount());
        Assertions.assertEquals(0.5, response.getLoans().get(0).getInterestRate());
        Assertions.assertTrue(response.getLoans().get(0).getNextInstallmentDueDate().isAfter(Instant.now()));
        Assertions.assertFalse(response.getLoans().get(0).isPaid());
    }

    @Test
    void getLoanByCustomerId_when_adminUser() throws IngException {
        //given
        UserDetails customer = User.builder()
                .username("admin")
                .password("admin")
                .roles(UserRole.ADMIN.name())
                .build();
        mockSecurityContext(customer);
        Mockito.doReturn(Optional.of(getCustomer())).when(customerService).findByUsername(Mockito.anyString());
        createLoan();
        //when
        ListLoanResponse response = loanService.getLoanByCustomerId(1L, 0, 10);
        //then
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getServiceResult());
        Assertions.assertEquals(ErrorCodes.SUCCESS_MESSAGE, response.getServiceResult().getMessage());
        Assertions.assertEquals(ErrorCodes.SUCCESS_CODE, response.getServiceResult().getCode());
        Assertions.assertNotNull(response.getLoans());
        Assertions.assertEquals(10000L, response.getLoans().get(0).getLoanAmount());
        Assertions.assertEquals(15000L, response.getLoans().get(0).getTotalAmount());
        Assertions.assertEquals(2500L, response.getLoans().get(0).getSingleInstallmentAmount());
        Assertions.assertEquals(6, response.getLoans().get(0).getInstallmentCount());
        Assertions.assertEquals(0.5, response.getLoans().get(0).getInterestRate());
        Assertions.assertTrue(response.getLoans().get(0).getNextInstallmentDueDate().isAfter(Instant.now()));
        Assertions.assertFalse(response.getLoans().get(0).isPaid());
    }

    @Test
    void getInstallmentByLoanId_when_loanNotFound() throws IngException {
        //given && when
        IngException exception = Assertions.assertThrows(IngException.class, () -> loanService.getInstallmentByLoanId(1L, 0, 10));
        //then
        Assertions.assertEquals(ErrorCodes.E_LOAN_NOT_FOUND, exception.getCode());
    }

    @Test
    void getInstallmentByLoanId_when_userNotFound() throws IngException {
        //given
        UserDetails customer = User.builder()
                .username("customer")
                .password("customerPassword")
                .roles(UserRole.CUSTOMER.name())
                .build();
        mockSecurityContext(customer);
        Mockito.doReturn(Optional.empty()).when(customerService).findByUsername(Mockito.anyString());
        createLoan();
        //when
        IngException exception = Assertions.assertThrows(IngException.class, () -> loanService.getInstallmentByLoanId(loanRepository.findAll().get(0).getId(), 0, 10));
        //then
        Assertions.assertEquals(ErrorCodes.E_INVALID_ACCESS, exception.getCode());
    }

    @Test
    void getInstallmentByLoanId_when_customerDontMatch() throws IngException {
        //given
        UserDetails customer = User.builder()
                .username("customer")
                .password("customerPassword")
                .roles(UserRole.CUSTOMER.name())
                .build();
        mockSecurityContext(customer);
        Mockito.doReturn(Optional.of(getCustomer().setId(2L))).when(customerService).findByUsername(Mockito.anyString());
        createLoan();
        //when
        IngException exception = Assertions.assertThrows(IngException.class, () -> loanService.getInstallmentByLoanId(loanRepository.findAll().get(0).getId(), 0, 10));
        //then
        Assertions.assertEquals(ErrorCodes.E_INVALID_ACCESS, exception.getCode());
    }

    @Test
    void getInstallmentByLoanId_when_customerMatches() throws IngException {
        //given
        UserDetails customer = User.builder()
                .username("customer")
                .password("customerPassword")
                .roles(UserRole.CUSTOMER.name())
                .build();
        mockSecurityContext(customer);
        Mockito.doReturn(Optional.of(getCustomer())).when(customerService).findByUsername(Mockito.anyString());
        createLoan();
        //when
        ListInstallmentResponse response = loanService.getInstallmentByLoanId(loanRepository.findAll().get(0).getId(), 0, 10);
        //then
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getServiceResult());
        Assertions.assertEquals(ErrorCodes.SUCCESS_MESSAGE, response.getServiceResult().getMessage());
        Assertions.assertEquals(ErrorCodes.SUCCESS_CODE, response.getServiceResult().getCode());
        Assertions.assertNotNull(response.getInstallments());
        Assertions.assertEquals(2500L, response.getInstallments().getFirst().getAmount());
        Assertions.assertEquals(1, response.getInstallments().getFirst().getOrder());
        Assertions.assertNull(response.getInstallments().getFirst().getPaymentDate());
        Assertions.assertFalse(response.getInstallments().getFirst().isPaid());
        Assertions.assertTrue(response.getInstallments().getFirst().getDueDate().isAfter(Instant.now()));

    }

    @Test
    void getInstallmentByLoanId_when_adminUser() throws IngException {
        //given
        UserDetails customer = User.builder()
                .username("admin")
                .password("admin")
                .roles(UserRole.ADMIN.name())
                .build();
        mockSecurityContext(customer);
        Mockito.doReturn(Optional.of(getCustomer().setId(2L))).when(customerService).findByUsername(Mockito.anyString());
        createLoan();
        //when
        ListInstallmentResponse response = loanService.getInstallmentByLoanId(loanRepository.findAll().get(0).getId(), 0, 10);
        //then
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getServiceResult());
        Assertions.assertEquals(ErrorCodes.SUCCESS_MESSAGE, response.getServiceResult().getMessage());
        Assertions.assertEquals(ErrorCodes.SUCCESS_CODE, response.getServiceResult().getCode());
        Assertions.assertNotNull(response.getInstallments());
        Assertions.assertEquals(2500L, response.getInstallments().getFirst().getAmount());
        Assertions.assertEquals(1, response.getInstallments().getFirst().getOrder());
        Assertions.assertNull(response.getInstallments().getFirst().getPaymentDate());
        Assertions.assertFalse(response.getInstallments().getFirst().isPaid());
        Assertions.assertTrue(response.getInstallments().getFirst().getDueDate().isAfter(Instant.now()));

    }

    @Test
    void payment_when_loanNotFound() {
        //given
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setLoanId(1L);
        //when
        IngException exception = Assertions.assertThrows(IngException.class, () -> loanService.payment(paymentRequest));
        //then
        Assertions.assertEquals(ErrorCodes.E_LOAN_NOT_FOUND, exception.getCode());
    }

    @Test
    void payment_when_customerDontMatch() throws IngException {
        //given
        UserDetails customer = User.builder()
                .username("customer")
                .password("customerPassword")
                .roles(UserRole.CUSTOMER.name())
                .build();
        mockSecurityContext(customer);
        Mockito.doReturn(Optional.of(getCustomer().setId(2L))).when(customerService).findByUsername(Mockito.anyString());
        createLoan();
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setLoanId(loanRepository.findAll().get(0).getId());
        //when
        IngException exception = Assertions.assertThrows(IngException.class, () -> loanService.payment(paymentRequest));
        //then
        Assertions.assertEquals(ErrorCodes.E_INVALID_ACCESS, exception.getCode());
    }

    @Test
    void payment_when_customerMatch() throws IngException {
        //given

        UserDetails customer = User.builder()
                .username("customer")
                .password("customerPassword")
                .roles(UserRole.CUSTOMER.name())
                .build();
        mockSecurityContext(customer);
        Mockito.doReturn(Optional.of(getCustomer())).when(customerService).findByUsername(Mockito.anyString());
        Mockito.doReturn(Optional.of(getCustomer())).when(customerService).findByCustomerId(Mockito.anyLong());
        createLoan();
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setLoanId(loanRepository.findAll().get(0).getId());
        paymentRequest.setPaymentAmount(2500L);
        //when
        PaymentResponse paymentResponse = loanService.payment(paymentRequest);
        //then
        Assertions.assertNotNull(paymentResponse);
        Assertions.assertEquals(ErrorCodes.SUCCESS_MESSAGE, paymentResponse.getServiceResult().getMessage());
        Assertions.assertEquals(ErrorCodes.SUCCESS_CODE, paymentResponse.getServiceResult().getCode());
        Assertions.assertNotNull(paymentResponse.getPaymentDto());
        Assertions.assertEquals(paymentRequest.getLoanId(), paymentResponse.getPaymentDto().getLoanId());
        Assertions.assertEquals(1, paymentResponse.getPaymentDto().getPaidInstallmentCount());
        Assertions.assertEquals(5, paymentResponse.getPaymentDto().getRemainingInstallmentCount());
        Assertions.assertEquals(2500L, paymentResponse.getPaymentDto().getAmountSpent());
    }

    @Test
    void paymentMultipleInstallments_when_customerMatch() throws IngException {
        //given
        UserDetails customer = User.builder()
                .username("customer")
                .password("customerPassword")
                .roles(UserRole.CUSTOMER.name())
                .build();
        mockSecurityContext(customer);
        Mockito.doReturn(Optional.of(getCustomer())).when(customerService).findByUsername(Mockito.anyString());
        Mockito.doReturn(Optional.of(getCustomer())).when(customerService).findByCustomerId(Mockito.anyLong());
        createLoan();
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setLoanId(loanRepository.findAll().get(0).getId());
        paymentRequest.setPaymentAmount(7650L);
        //when
        PaymentResponse paymentResponse = loanService.payment(paymentRequest);
        //then
        Assertions.assertNotNull(paymentResponse);
        Assertions.assertEquals(ErrorCodes.SUCCESS_MESSAGE, paymentResponse.getServiceResult().getMessage());
        Assertions.assertEquals(ErrorCodes.SUCCESS_CODE, paymentResponse.getServiceResult().getCode());
        Assertions.assertNotNull(paymentResponse.getPaymentDto());
        Assertions.assertEquals(paymentRequest.getLoanId(), paymentResponse.getPaymentDto().getLoanId());
        Assertions.assertEquals(3, paymentResponse.getPaymentDto().getPaidInstallmentCount());
        Assertions.assertEquals(3, paymentResponse.getPaymentDto().getRemainingInstallmentCount());
        Assertions.assertEquals(7500L, paymentResponse.getPaymentDto().getAmountSpent());
    }
    

    private void createLoan() throws IngException {
        CreateLoanRequest createLoanRequest = new CreateLoanRequest();
        createLoanRequest.setCustomerId(1L);
        createLoanRequest.setLoanAmount(10000L);
        createLoanRequest.setInterestRate(0.5);
        createLoanRequest.setInstallmentCount(6);
        Mockito.doReturn(Optional.of(getCustomer())).when(customerService).findByCustomerId(Mockito.anyLong());
        loanService.create(createLoanRequest);
    }

    private void mockSecurityContext(UserDetails userDetails) {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private Customer getCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCreditLimit(100000L);
        customer.setName("onat");
        customer.setSurname("kutlu");
        customer.setPassword("password");
        customer.setUsedCreditLimit(0L);
        customer.setRole(UserRole.CUSTOMER.name());
        return customer;
    }
}
