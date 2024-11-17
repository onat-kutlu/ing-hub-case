package com.ing.inghub.repository;

import com.ing.inghub.model.LoanInstallment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {

    List<LoanInstallment> findAllByLoanIdOrderByOrder(Long loanId, Pageable pageable);

    List<LoanInstallment> findAllByLoanIdAndPaidIsFalseOrderByOrder(Long loanId);

}
