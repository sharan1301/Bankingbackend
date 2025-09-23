package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.Autopay;
import com.example.BankingBackend.Model.Transaction;
import com.example.BankingBackend.Repository.AccountRepo;
import com.example.BankingBackend.Repository.AutopayRepo;
import com.example.BankingBackend.Repository.PayeeRepo;
import com.example.BankingBackend.Repository.TransactionRepo;
import com.example.BankingBackend.Model.Payee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AutopayService {

    @Autowired
    private AutopayRepo autopayRepository;

    @Autowired
    private AccountRepo accountRepository;

    @Autowired
    private PayeeRepo payeeRepository;

    @Autowired
    private TransactionRepo transactionRepo;

    // Create Autopay
    @Transactional
    public Autopay createAutopay(Account sender, Payee receiver, double amount, long frequencySeconds,
                                 LocalDateTime startDate, LocalDateTime endDate) {
        Autopay autopay = new Autopay();
        autopay.setSenderAccount(sender);
        autopay.setReceiver(receiver);
        autopay.setAmount(amount);
        autopay.setFrequencySeconds(frequencySeconds);
        autopay.setStartDate(startDate);
        autopay.setEndDate(endDate);
        autopay.setActive(true);
        return autopayRepository.save(autopay);
    }

    // Cancel autopay
    @Transactional
    public void cancelAutopay(Long autopayId) {
        Autopay autopay = autopayRepository.findById(autopayId).orElseThrow(
                () -> new RuntimeException("Autopay not found")
        );
        autopay.setActive(false);
        autopayRepository.save(autopay);
    }

    // Scheduler: Runs every minute (can adjust cron)
    @Scheduled(cron = "*/30 * * * * *") // every 30 seconds
    @Transactional
    public void processAutopays() {
        LocalDateTime now = LocalDateTime.now();
        List<Autopay> autopays = autopayRepository.findByActiveTrue();

        for (Autopay autopay : autopays) {
            // check start/end date
            if ((autopay.getStartDate().isBefore(now) || autopay.getStartDate().isEqual(now)) &&
                    (autopay.getEndDate() == null || autopay.getEndDate().isAfter(now))) {

                Account sender = autopay.getSenderAccount();
                Payee receiver = autopay.getReceiver();
                Account receiverAccount = receiver.getAccount();

                // skip if not enough balance
                if (sender.getBalance() < autopay.getAmount()) {
                    System.out.println("Insufficient balance for sender: " + sender.getAccountNumber());
                    continue;
                }

                // Deduct sender
                double beforeSender = sender.getBalance();
                sender.setBalance(beforeSender - autopay.getAmount());
                accountRepository.save(sender);

                Transaction transaction = new Transaction();
                transaction.setAccount(sender);            // Sender account
                transaction.setRecvAcc(receiverAccount);   // Receiver account
                transaction.setAmount(autopay.getAmount());
                transaction.setTransactionDate(LocalDateTime.now());
                transaction.setTransactionType(Transaction.TransactionType.TRANSFER);
                transaction.setTransactionMode(Transaction.TransactionMode.valueOf("AUTOPAY")); // Example mode, adjust as needed
                transaction.setBalanceAfter(sender.getBalance());
                transaction.setStatus(Transaction.TransactionStatus.SUCCESS);

                // Save transaction to the database
                transactionRepo.save(transaction);


                System.out.println("Autopay executed: " + autopay.getAutopayId() +
                        " | Sender: " + sender.getAccountNumber() +
                        "balance: " + (beforeSender - autopay.getAmount())+
                        " | Receiver: " + receiverAccount.getAccountNumber() +
                        " | Amount: " + autopay.getAmount());
            }
        }
    }
}
