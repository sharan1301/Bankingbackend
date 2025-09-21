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
            }
            fdrepo.save(fd);
        }
        return fdrepo.findAll();
    }

    public FixedDeposit AddingAcc(FixedDeposit request) {
        logger.info("Adding FD for account: {}", request.getAccount().getAccountId());
        Account account = accountRepository.findById(request.getAccount().getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found with id " + request.getAccount().getAccountId()));

        FixedDeposit fd = new FixedDeposit();
        fd.setAccount(account); // Important: set managed entity
        fd.setDepositAmount(request.getDepositAmount());
        fd.setInterestRate(request.getInterestRate());
        fd.setStartDate(request.getStartDate());
        fd.setMaturityDate(request.getStartDate().plusMonths(request.getTenureMonths()));
        fd.setTenureMonths(request.getTenureMonths());
        fd.setStatus(request.getStatus());

        double maturity = calculateMaturityAmount(request.getDepositAmount(), request.getInterestRate(), request.getTenureMonths());
        fd.setMaturityAmount(maturity);

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
            return "⚠ You are withdrawing before maturity!\n" +
                        "Withdrawal amount after penalty: " + amount +
                        "\nDo you want to proceed?";
        } else {
            // After maturity
            double amount = fd.getMaturityAmount();
            return "✅ Your withdrawal amount: " + amount +
                    "\nNo penalty. Do you want to proceed?";
        }
    }

    public double withdrawFD(Long fdId,LocalDate date) {
        FixedDeposit fd = fdrepo.findById(fdId)
                .orElseThrow(() -> new RuntimeException("FD not found"));

        LocalDate today = date;
        long monthsCompleted = ChronoUnit.MONTHS.between(fd.getStartDate(), today);

        double payout;

        if (monthsCompleted < fd.getTenureMonths()) {
            // Premature withdrawal
            double penaltyRate = 1.0;
            double years = monthsCompleted / 12.0;
            double effectiveRate = fd.getInterestRate() - penaltyRate;
            payout = calculateMaturityAmount(fd.getDepositAmount(), effectiveRate, fd.getTenureMonths());
            fd.setStatus(FixedDeposit.DepositStatus.valueOf("PREMATURE_CLOSURE"));
        } else {
            // Normal withdrawal
            payout = fd.getMaturityAmount();
            fd.setStatus(FixedDeposit.DepositStatus.valueOf("CLOSED"));

        }
        fd.setMaturityAmount(payout);
        fdrepo.save(fd);
        return payout;
    }
}
