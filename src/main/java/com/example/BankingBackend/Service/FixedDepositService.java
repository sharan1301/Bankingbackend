package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.FixedDeposit;
import com.example.BankingBackend.Repository.AccountRepo;
import com.example.BankingBackend.Repository.FixedDepositRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class FixedDepositService {
    private static final Logger logger = LoggerFactory.getLogger(FixedDepositService.class);
    @Autowired
    FixedDepositRepo fdrepo;
    @Autowired
    AccountRepo accountRepository;

    public Iterable<FixedDeposit> fetchAllAccounts(LocalDate date) {

        for (FixedDeposit fd : fdrepo.findAll()) {
            if (date.isAfter(fd.getMaturityDate()) || date.isEqual(fd.getMaturityDate())) {
                fd.setStatus(FixedDeposit.DepositStatus.MATURED);
            } else if (date.isBefore(fd.getMaturityDate()) && (fd.getStatus() != FixedDeposit.DepositStatus.CLOSED &&
                    fd.getStatus() != FixedDeposit.DepositStatus.PREMATURE_CLOSURE)) {
                fd.setStatus(FixedDeposit.DepositStatus.ACTIVE);
            }
            fdrepo.save(fd);
        }
        return fdrepo.findAll();
    }

    public FixedDeposit AddingAcc(int userId, FixedDeposit request) {
        logger.info("Adding FD for account: {}", request.getAccount().getAccountId());

        // 1. Find the account by ID
        Account account = accountRepository.findById(request.getAccount().getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found with id " + request.getAccount().getAccountId()));

        // 2. Validate account belongs to the user
        if (account.getUser() == null || account.getUser().getUserId() != userId) {
            throw new RuntimeException("Account " + account.getAccountId() + " does not belong to user " + userId);
        }

        // 3. Validate account status
        if (account.getStatus()== Account.AccountStatus.FROZEN) {
            throw new RuntimeException("Account " + account.getAccountId() + " is not ACTIVE");
        }

        // 4. Validate sufficient balance
        if (request.getDepositAmount() > account.getBalance()) {
            throw new RuntimeException("Insufficient balance in account " + account.getAccountId());
        }

        // 5. Deduct deposit amount from account balance
        double newBalance = account.getBalance() - request.getDepositAmount();
        account.setBalance(newBalance);
        accountRepository.save(account);

        // 6. Create Fixed Deposit object
        FixedDeposit fd = new FixedDeposit();
        fd.setAccount(account);
        fd.setUser(account.getUser()); // set user as well
        fd.setDepositAmount(request.getDepositAmount());
        fd.setInterestRate(request.getInterestRate());
        fd.setStartDate(request.getStartDate());
        fd.setTenureMonths(request.getTenureMonths());
        fd.setMaturityDate(request.getStartDate().plusMonths(request.getTenureMonths()));
        fd.setStatus(FixedDeposit.DepositStatus.ACTIVE);

        // 7. Calculate maturity amount
        double maturity = calculateMaturityAmount(request.getDepositAmount(),
                request.getInterestRate(),
                request.getTenureMonths());
        double rounded = Math.round(maturity * 100.0) / 100.0;
        fd.setMaturityAmount(rounded);

        logger.info("FD created: {}", fd);
        return fdrepo.save(fd);
    }


    // Auto-calc maturity amount

    public double calculateMaturityAmount(double deposit, double rate, int tenureMonths) {
        //FD MATURITY CALCULATION LOGIC
        double years = tenureMonths / 12.0; // Convert months to years
        int n = 4; // Quarterly compounding (common in banks)
        return deposit * Math.pow(1 + (rate / (n * 100)), n * years);
    }

    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public String previewWithdrawal(Long fdId,LocalDate date) {
        FixedDeposit fd = fdrepo.findById(fdId)
                .orElseThrow(() -> new RuntimeException("FD not found"));

        LocalDate today = date;
        int monthsCompleted = Math.toIntExact(ChronoUnit.MONTHS.between(fd.getStartDate(), today));
        if (monthsCompleted < fd.getTenureMonths()) {
            double penaltyRate = 1.0; // 1%
            double effectiveRate = fd.getInterestRate() - penaltyRate;
            double amount = calculateMaturityAmount(fd.getDepositAmount(), effectiveRate, monthsCompleted);
            double rounded = Math.round(amount * 100.0) / 100.0;
            return "You are withdrawing before maturity!\n" +
                        "Withdrawal amount after penalty: " + rounded +
                        "\nDo you want to proceed?";
        } else {
            // After maturity
            double amount = fd.getMaturityAmount();
            double rounded = Math.round(amount * 100.0) / 100.0;
            return " Your withdrawal amount: " + rounded +
                    "\nNo penalty. Do you want to proceed?";
        }
    }

    public double withdrawFD(Long fdId,LocalDate date) {
        FixedDeposit fd = fdrepo.findById(fdId)
                .orElseThrow(() -> new RuntimeException("FD not found"));

        LocalDate today = date;
        long daysCompleted = ChronoUnit.DAYS.between(fd.getStartDate(), today);
        double monthsCompleted = daysCompleted / 30.0;
        double payout;
        double rounded;
        if (today.isBefore(fd.getMaturityDate())) {
            // Premature withdrawal
            double penaltyRate = 1.0;
            double years = monthsCompleted / 12.0;
            double effectiveRate = fd.getInterestRate() - penaltyRate;
            payout = calculateMaturityAmount(fd.getDepositAmount(), effectiveRate, (int)monthsCompleted);
            rounded = Math.round(payout * 100.0) / 100.0;
            fd.setStatus(FixedDeposit.DepositStatus.valueOf("PREMATURE_CLOSURE"));
        } else {
            // Normal withdrawal
            payout = fd.getMaturityAmount();
            rounded = Math.round(payout * 100.0) / 100.0;
            fd.setStatus(FixedDeposit.DepositStatus.valueOf("CLOSED"));
        }
        fd.setMaturityAmount(rounded);
        fdrepo.save(fd);
        Account account = fd.getAccount();
        double newBalance = account.getBalance() + rounded;
        account.setBalance(newBalance);
        accountRepository.save(account);
        return rounded;
    }
}
