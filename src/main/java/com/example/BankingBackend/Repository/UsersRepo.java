package com.example.BankingBackend.Repository;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<Users,Integer> {
    Optional<Users> findByEmailIgnoreCase(String email);

    Optional<Users> findByPassword(String password);
    @Query(value = "SELECT * FROM users WHERE aadhaar_number = :aadhaarNumber AND pan_number = :panNumber", nativeQuery = true)
    List<Users> findAllByAadhaarNumberAndPanNumber(@Param("aadhaarNumber") String aadhaarNumber,
                                                @Param("panNumber") String panNumber);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByCustIdAndPassword(String userId, String password);

    List<Users> findByCustId(String custId);

    //ashok
    List<Users> findByAadhaarNumber(String aadhaarNumber);
}
