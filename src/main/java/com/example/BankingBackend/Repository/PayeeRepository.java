package org.example.Repository;
import org.example.Model.Payee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayeeRepository extends JpaRepository<Payee, Integer> {

    Payee findByPayeeAccNo(Long payeeAccNo);
    @Query(value = "SELECT p.* FROM payees p LEFT JOIN accounts a ON p.account_id = a.account_id WHERE a.account_id = :accountId", nativeQuery = true)
    List<Payee> findByAccount_AccountId(Long accountId);  // Correct method, matching your entity structure

}

