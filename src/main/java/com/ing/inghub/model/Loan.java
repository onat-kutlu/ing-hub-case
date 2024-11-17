package com.ing.inghub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "LOAN")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "LOAN_AMOUNT")
    private Long loanAmount;

    @Column(name = "INTEREST_RATE")
    private Double interestRate;

    @Column(name = "LOAN_AMOUNT_WITH_INTEREST")
    private Long loanAmountWithInterest;

    @Column(name = "INSTALLMENT_COUNT")
    private Integer numberOfInstallment;

    @Column(name = "CREATE_DATE")
    private Instant createDate;

    @Column(name = "NEXT_INSTALLMENT_DATE")
    private Instant nextInstallmentDate;

    @Column(name = "IS_PAID")
    private boolean paid = false;
}
