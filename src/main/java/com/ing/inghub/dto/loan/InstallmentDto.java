package com.ing.inghub.dto.loan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class InstallmentDto {
    private Long id;
    private Long amount;
    private Long paidAmount;
    private Instant dueDate;
    private Instant paymentDate;
    private boolean paid;
    private Integer order;
}
