package com.ing.inghub.dto.loan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class PaymentDto implements Serializable {
    private Long loanId;
    private Integer paidInstallmentCount;
    private Long amountSpent;
    private Integer remainingInstallmentCount;
    private boolean fullyPaid;
}
