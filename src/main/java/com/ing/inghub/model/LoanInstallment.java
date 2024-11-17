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
@Table(name = "LOAN_INSTALLMENT")
public class LoanInstallment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LOAN_ID")
    private Long loanId;

    @Column(name = "AMOUNT")
    private Long amount;

    @Column(name = "PAID_AMOUNT")
    private Long paidAmount;

    @Column(name = "DUE_DATE")
    private Instant dueDate;

    @Column(name = "PAYMENT_DATE")
    private Instant paymentDate;

    @Column(name = "IS_PAID")
    private boolean paid = false;

    @Column(name = "INSTALLMENT_ORDER")
    private Integer order;
}
