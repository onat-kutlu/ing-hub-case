package com.ing.inghub.repository;

import com.ing.inghub.model.Loan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findAllByCustomerId(Long customerId, Pageable pageable);

    Optional<Loan> findById(Long id);
}
