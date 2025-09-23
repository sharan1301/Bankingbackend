<<<<<<< HEAD
package org.example.Services;

import org.example.Model.Account;
import org.example.Model.Payee;
import org.example.Model.Transaction;
import org.example.Repository.AccountRepository;
import org.example.Repository.PayeeRepository;
import org.example.Repository.TransactionRepository;
//import org.example.Request.TransactionRequest;
=======
package com.example.BankingBackend.Service;


import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.Payee;
import com.example.BankingBackend.Model.Transaction;
import com.example.BankingBackend.Repository.AccountRepo;
import com.example.BankingBackend.Repository.PayeeRepo;
import com.example.BankingBackend.Repository.TransactionRepo;
>>>>>>> sharan
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
<<<<<<< HEAD
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PayeeRepository payeeRepository;
=======
    private TransactionRepo transactionRepository;

    @Autowired
    private AccountRepo accountRepository;

    @Autowired
    private PayeeRepo payeeRepository;
>>>>>>> sharan

    public Transaction saveBankTransfer(Transaction request) {
        Account sender = accountRepository.findByAccountNumber(request.getAccount().getAccountNumber());
        if (sender == null) {
            throw new RuntimeException("Sender account not found");
        }

        Account receiver = accountRepository.findByAccountNumber(request.getRecvAcc().getAccountNumber());
        if (receiver == null) {
            throw new RuntimeException("Receiver account not found");
        }

        Transaction transaction = new Transaction();
        transaction.setAccount(sender);
        transaction.setRecvAcc(receiver);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionType(Transaction.TransactionType.TRANSFER);

        transaction.setTransactionMode(request.getTransactionMode());

        if (request.getAmount() <= 0) {
            request.setStatus(Transaction.TransactionStatus.valueOf("FAILED"));
        } else if (transaction.getAccount().getBalance() < request.getAmount()) {
            request.setStatus(Transaction.TransactionStatus.valueOf("INSUFFICIENT_FUNDS"));
        } else {
            Double balance = transaction.getAccount().getBalance() - request.getAmount();
            transaction.getAccount().setBalance(balance);
            transaction.getRecvAcc().setBalance(transaction.getRecvAcc().getBalance() + request.getAmount());
            accountRepository.save(transaction.getAccount());
            accountRepository.save(transaction.getRecvAcc());
            transaction.setBalanceAfter(balance);
            transaction.setStatus(Transaction.TransactionStatus.SUCCESS);
        }
        request.setTransactionType(Transaction.TransactionType.valueOf("BANK"));
        request.setTransactionMode(request.getTransactionMode());
        return transactionRepository.save(transaction);
    }


    // -------------------- SELF TRANSFER --------------------
    public Transaction saveSelfTransfer(Transaction transfer) {
        Account fromAccount = accountRepository.findByAccountNumber(transfer.getAccount().getAccountNumber());
        Account toAccount = accountRepository.findByAccountNumber(transfer.getRecvAcc().getAccountNumber());

        if (transfer.getAccount().getUser().getEmail() != transfer.getRecvAcc().getUser().getEmail()|| transfer.getAmount() <= 0) {
            transfer.setStatus(Transaction.TransactionStatus.valueOf("FAILED"));
        } else if (fromAccount.getBalance() < transfer.getAmount()) {
            transfer.setStatus(Transaction.TransactionStatus.valueOf("INSUFFICIENT_FUNDS"));
        } else {
            Double balance = fromAccount.getBalance() - transfer.getAmount();
            fromAccount.setBalance(fromAccount.getBalance() - transfer.getAmount());
            toAccount.setBalance(toAccount.getBalance() + transfer.getAmount());
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            transfer.setBalanceAfter(balance);
            transfer.setStatus(Transaction.TransactionStatus.valueOf("SUCCESS"));
        }

        transfer.setTransactionType(Transaction.TransactionType.valueOf("SELF"));
        return transactionRepository.save(transfer);
    }

    // -------------------- PAYEE TRANSFER --------------------

<<<<<<< HEAD
public Transaction savePayeeTransfer(Transaction transfer) {
    // Fetch the sender and receiver accounts
    Account fromAccount = accountRepository.findByAccountNumber(transfer.getAccount().getAccountNumber());
    Account toAccount = payeeRepository.findByPayeeAccNo(transfer.getRecvAcc().getAccountNumber()).getAccount();

    if (fromAccount == null) {
        throw new RuntimeException("Sender account not found");
    }
    if (toAccount == null) {
        throw new RuntimeException("Receiver account not found");
    }

    // Check if the 'toAccount' is a registered payee of the 'fromAccount'
    boolean isRegisteredPayee = false;
    for (Payee payee : fromAccount.getPayees()) {
        if (payee.getAccount().equals(toAccount)) {
            isRegisteredPayee = true;
            break;
        }
    }

    // Create a new transaction object
    Transaction transaction = new Transaction();
    transaction.setAccount(fromAccount);
    transaction.setRecvAcc(toAccount);
    transaction.setAmount(transfer.getAmount());
    transaction.setTransactionDate(LocalDateTime.now());
    transaction.setTransactionType(Transaction.TransactionType.TRANSFER);
    transaction.setTransactionMode(transfer.getTransactionMode());

    // Check transaction conditions and update status accordingly
    if (!isRegisteredPayee) {
        transfer.setStatus(Transaction.TransactionStatus.FAILED);
        transaction.setStatus(Transaction.TransactionStatus.FAILED);
    } else if (transfer.getAmount() <= 0) {
        transfer.setStatus(Transaction.TransactionStatus.FAILED);
        transaction.setStatus(Transaction.TransactionStatus.FAILED);
    } else if (fromAccount.getBalance() < transfer.getAmount()) {
        transfer.setStatus(Transaction.TransactionStatus.INSUFFICIENT_FUNDS);
        transaction.setStatus(Transaction.TransactionStatus.INSUFFICIENT_FUNDS);
    } else {
        // Proceed with successful transaction
        Double newSenderBalance = fromAccount.getBalance() - transfer.getAmount();
        fromAccount.setBalance(newSenderBalance); // Update sender balance

        Double newReceiverBalance = toAccount.getBalance() + transfer.getAmount();
        toAccount.setBalance(newReceiverBalance); // Update receiver balance

        // Save the updated accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Set transaction status as successful
        transfer.setStatus(Transaction.TransactionStatus.SUCCESS);
        transaction.setStatus(Transaction.TransactionStatus.SUCCESS);

        // Set balance after transaction for the sender
        transaction.setBalanceAfter(newSenderBalance);
    }

    // Set the transaction type and save the transaction
    transfer.setTransactionType(Transaction.TransactionType.PAYEE);
    return transactionRepository.save(transaction);
}

=======
    public Transaction savePayeeTransfer(Transaction transfer) {
        // Fetch the sender and receiver accounts
        Account fromAccount = accountRepository.findByAccountNumber(transfer.getAccount().getAccountNumber());
        Account toAccount = payeeRepository.findByPayeeAccNo(transfer.getRecvAcc().getAccountNumber()).getAccount();

        if (fromAccount == null) {
            throw new RuntimeException("Sender account not found");
        }
        if (toAccount == null) {
            throw new RuntimeException("Receiver account not found");
        }

        // Check if the 'toAccount' is a registered payee of the 'fromAccount'
        boolean isRegisteredPayee = false;
        for (Payee payee : fromAccount.getPayees()) {
            if (payee.getAccount().equals(toAccount)) {
                isRegisteredPayee = true;
                break;
            }
        }

        // Create a new transaction object
        Transaction transaction = new Transaction();
        transaction.setAccount(fromAccount);
        transaction.setRecvAcc(toAccount);
        transaction.setAmount(transfer.getAmount());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionType(Transaction.TransactionType.TRANSFER);
        transaction.setTransactionMode(transfer.getTransactionMode());

        // Check transaction conditions and update status accordingly
        if (!isRegisteredPayee) {
            transfer.setStatus(Transaction.TransactionStatus.FAILED);
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
        } else if (transfer.getAmount() <= 0) {
            transfer.setStatus(Transaction.TransactionStatus.FAILED);
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
        } else if (fromAccount.getBalance() < transfer.getAmount()) {
            transfer.setStatus(Transaction.TransactionStatus.INSUFFICIENT_FUNDS);
            transaction.setStatus(Transaction.TransactionStatus.INSUFFICIENT_FUNDS);
        } else {
            // Proceed with successful transaction
            Double newSenderBalance = fromAccount.getBalance() - transfer.getAmount();
            fromAccount.setBalance(newSenderBalance); // Update sender balance

            Double newReceiverBalance = toAccount.getBalance() + transfer.getAmount();
            toAccount.setBalance(newReceiverBalance); // Update receiver balance

            // Save the updated accounts
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            // Set transaction status as successful
            transfer.setStatus(Transaction.TransactionStatus.SUCCESS);
            transaction.setStatus(Transaction.TransactionStatus.SUCCESS);

            // Set balance after transaction for the sender
            transaction.setBalanceAfter(newSenderBalance);
        }

        // Set the transaction type and save the transaction
        transfer.setTransactionType(Transaction.TransactionType.PAYEE);
        return transactionRepository.save(transaction);
    }

>>>>>>> sharan


    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public
    List<Transaction> getTransfersByFromAccNo(int fromAccNo) {
        return transactionRepository.findByFromAccNo(fromAccNo);
    }

    public
    List<Transaction> getTransfersByToAccNo(int ToAccNo){
        return transactionRepository.findByToAccNo(ToAccNo);
    }

    public
    List<Transaction> getTransfers(){
        return transactionRepository.findAll();
    }


    public Account getAccountByAccNo(Long fromAccNo) {

        System.out.println("Fund transfer service......."+ fromAccNo);
        return accountRepository.findByAccountNumber(fromAccNo);
    }

    public void createTransaction(Long senderAccountNumber, Long receiverAccountNumber, double amount) {
        // Step 1: Find the sender and receiver accounts
        Account senderAccount = accountRepository.findByAccountNumber(senderAccountNumber);
        if (senderAccount == null) {
            throw new RuntimeException("Sender account not found");
        }

        Account receiverAccount = accountRepository.findByAccountNumber(receiverAccountNumber);
        if (receiverAccount == null) {
            throw new RuntimeException("Receiver account not found");
        }

        // Step 2: Create the transaction
        Transaction transaction = new Transaction();
        transaction.setAccount(senderAccount);   // Set sender account
        transaction.setRecvAcc(receiverAccount); // Set receiver account
        transaction.setAmount(amount);
        transaction.setTransactionType(Transaction.TransactionType.BANK);  // Adjust this based on your logic
        transaction.setStatus(Transaction.TransactionStatus.PENDING);

        // Step 3: Save the transaction
        transactionRepository.save(transaction);
    }

<<<<<<< HEAD
    }
=======
}
>>>>>>> sharan







