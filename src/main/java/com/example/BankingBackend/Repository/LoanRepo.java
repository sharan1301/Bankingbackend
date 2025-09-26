package com.example.BankingBackend.Repository;

import com.example.BankingBackend.Model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepo extends JpaRepository<Loan,Long> {
    @Query(value = "SELECT * FROM LOAN l WHERE l.USER_ID IN :userIds", nativeQuery = true)
    List<Loan> findLoansByUserIds(@Param("userIds") List<Integer> userIds);

    //List<Loan> findByUserUserIdIn(List<Integer> userIds);
}
