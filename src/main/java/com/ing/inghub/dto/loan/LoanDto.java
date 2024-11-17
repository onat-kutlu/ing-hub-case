package com.ing.inghub.dto.loan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class LoanDto implements Serializable {
    private Long id;
    private Long loanAmount;
    private Long totalAmount;
    private Long customerId;
    private Long singleInstallmentAmount;
    private Integer installmentCount;
    private Double interestRate;
    private Instant createdAt;
    private Instant nextInstallmentDueDate;
    private boolean paid;
}
