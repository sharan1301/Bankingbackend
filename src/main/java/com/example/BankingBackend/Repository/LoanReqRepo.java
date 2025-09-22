package com.example.BankingBackend.Repository;

import com.example.BankingBackend.Model.LoanRequests;
import com.example.BankingBackend.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanReqRepo extends JpaRepository<LoanRequests,Long> {
    List<LoanRequests> findByStatus(LoanRequests.RequestStatus status);
    boolean existsByUserAndStatus(Users user, LoanRequests.RequestStatus status);
    List<LoanRequests> findByUserUserIdAndStatus(int userId, LoanRequests.RequestStatus status);
    LoanRequests findByAccountAccountNumberAndStatus(Long accountNumber, LoanRequests.RequestStatus status);
//    LoanRequests findByUserUserIdAndAccountAccountIdAndStatus(
//            int userId,
//            Long accountId,
//            LoanRequests.RequestStatus status
//    );
@Query(value = "SELECT * FROM LOAN_REQUEST WHERE USER_ID = :userId AND STATUS = :status", nativeQuery = true)
        LoanRequests findByUserIdAndStatusNative(
        @Param("userId") int userId,

        @Param("status") String status
);
}
