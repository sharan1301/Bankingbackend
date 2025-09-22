package org.example.Services;
import org.example.Model.Payee;
import java.util.List;
import java.util.Optional;

public interface PayeeService {
    List<Payee> findPayeesByAccountId(Long accountId);

    Payee addPayee(Payee payee);
    Payee updatePayee(Payee payee);
    void deletePayee(int payeeId);
    Optional<Payee> getPayeeById(int payeeId);
     List<Payee> getAllPayees();
    Payee addPayee(Payee payee, Long accountId);
    List<Payee> getPayeesByAccountId(Long accountId);


}
