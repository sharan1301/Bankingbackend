package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.FixedDeposit;
import com.example.BankingBackend.Model.RecurringDeposit;
import com.example.BankingBackend.Repository.AccountRepo;
import com.example.BankingBackend.Repository.RecurringDepositRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.example.BankingBackend.Model.RecurringDeposit.DepositStatus.PAID_WAIT_MATURE;

@Service
public class RecurringDepositService {

    @Autowired
    private RecurringDepositRepo rdRepo;
    @Autowired
    AccountRepo accountRepository;

    public Iterable<RecurringDeposit> fetchAllAccountswithfine(LocalDate date) {
        for (RecurringDeposit rd : rdRepo.findAll()) {
            applyMissedInstallmentFine(rd, date);
            LocalDate maturitydate = rd.getMaturityDate();
            double totalDue = rd.getMonthlyInstallment() * rd.getTenureMonths();

            // 1. Fully paid, no fine, after or on maturity → MATURED
            if (rd.getTotalDeposited() == totalDue && rd.getFine() == 0 &&
                    (date.isAfter(maturitydate) || date.isEqual(maturitydate))) {
                rd.setStatus(RecurringDeposit.DepositStatus.MATURED);
            }

            // 2. Fully paid, no fine, before maturity → PAID_WAIT_MATURE
            else if (rd.getTotalDeposited() == totalDue && rd.getFine() == 0 &&
                    date.isBefore(maturitydate)) {
                rd.setStatus(RecurringDeposit.DepositStatus.PAID_WAIT_MATURE);
            }

            // 3. Not fully paid, before maturity, not closed/premature → ACTIVE
            else if (rd.getTotalDeposited() < totalDue &&
                    date.isBefore(maturitydate) &&
                    rd.getStatus() != RecurringDeposit.DepositStatus.CLOSED &&
                    rd.getStatus() != RecurringDeposit.DepositStatus.PREMATURE_CLOSURE) {
                rd.setStatus(RecurringDeposit.DepositStatus.ACTIVE);
            }

            // 4. Fully paid, after maturity but previously PAID_WAIT_MATURE → MATURED
            else if (rd.getTotalDeposited() == totalDue && rd.getStatus() == RecurringDeposit.DepositStatus.PAID_WAIT_MATURE) {
                rd.setStatus(RecurringDeposit.DepositStatus.MATURED);
            }

            rdRepo.save(rd);
        }

        return rdRepo.findAll();
    }

    public RecurringDeposit AddingAcc(RecurringDeposit request) {
        Account account = accountRepository.findById(request.getAccount().getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found with id " + request.getAccount().getAccountId()));

        RecurringDeposit rd = new RecurringDeposit();
        rd.setAccount(account);
        rd.setMonthlyInstallment(request.getMonthlyInstallment());
        rd.setInterestRate(request.getInterestRate());
        rd.setStartDate(request.getStartDate());
        rd.setTenureMonths(request.getTenureMonths());

        // 3. Initialize deposit details
        rd.setTotalDeposited(0.0);  // no deposit at start
        rd.setMaturityDate(request.getStartDate().plusMonths(request.getTenureMonths()));
        rd.setStatus(RecurringDeposit.DepositStatus.ACTIVE);
        rd.setLastInstallmentDate(request.getStartDate());

        double maturity = calculateMaturityAmount(request.getMonthlyInstallment(), request.getInterestRate(), request.getTenureMonths());
        rd.setMaturityAmount(maturity);

        return rdRepo.save(rd);
    }

    // Auto-calc maturity amount

    public double calculateMaturityAmount(double monthlyInstallment, double annualRate, int tenureMonths) {
        double quarterlyRate = (annualRate / 100.0) / 4.0;
        double balance = 0.0;
        int monthsInQuarter = 0;
        //RD MATURITY CALCULATION LOGIC

        for (int i = 1; i <= tenureMonths; i++) {
            balance += monthlyInstallment; // deposit this month's installment
            monthsInQuarter++;

            // End of quarter → add full quarter’s interest
            if (i % 3 == 0) {
                balance += balance * quarterlyRate;
                monthsInQuarter = 0;
            }
            // End of tenure but not full quarter → add proportional interest
            else if (i == tenureMonths) {
                double fraction = monthsInQuarter / 3.0;
                balance += balance * quarterlyRate * fraction;
            }
        }

        return Math.round(balance * 100.0) / 100.0;
    }

    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public String payInstallment(Long rdId, Double amount, LocalDate paymentDate) {
        String message;
        // 1. Get RD
        RecurringDeposit rd = rdRepo.findById(rdId)
                .orElseThrow(() -> new RuntimeException("RD not found with id " + rdId));

        // 3. First clear fines if any
        double finePending = rd.getFine();
        double actualInstallments=finePending/50.0;
        double x=(rd.getMonthlyInstallment()*actualInstallments) + finePending;
        if (finePending > 0 && amount == x) {
            // Fine is fully cleared
            rd.setFine(0.0);
            rd.setTotalDeposited(rd.getTotalDeposited()+(x-finePending));
            double maxDeposit = rd.getMonthlyInstallment() * rd.getTenureMonths();
            if (maxDeposit==(rd.getTotalDeposited()+(x-finePending))){
                rd.setStatus(RecurringDeposit.DepositStatus.MATURED);
            }
            rdRepo.save(rd); // Save the fine clearance before returning
            return "Fine amount of " + finePending + " and installment amount of "+
                    (x-finePending)+" is cleared.";
        }
        if(finePending > 0 && amount!=x){
            return "You should pay both all previous missed fine and monthly installments of "+x;
        }

        // 4. Validate installment amount (after clearing fine, must match monthly installment)
        if (!amount.equals(rd.getMonthlyInstallment())) {
            throw new RuntimeException("Installment amount must be exactly " + rd.getMonthlyInstallment());
        }

        // 5. Prevent overpayment beyond tenure
        double maxDeposit = rd.getMonthlyInstallment() * rd.getTenureMonths();
        if (rd.getTotalDeposited() + amount > maxDeposit) {
            throw new RuntimeException("Cannot deposit more than total RD amount (" + maxDeposit + ")");
        }

        // 6. Update deposited total
        rd.setTotalDeposited(rd.getTotalDeposited() + amount);

        // 7. Update last installment date
        rd.setLastInstallmentDate(paymentDate);

        message = "Payment successful";

        // 8. If all installments paid → check maturity
        int monthsPaid = (int) (rd.getTotalDeposited() / rd.getMonthlyInstallment());
        if (monthsPaid >= rd.getTenureMonths()) {
            LocalDate maturityDate = rd.getStartDate().plusMonths(rd.getTenureMonths());

            if (!paymentDate.isBefore(maturityDate)) {
                // ✅ Maturity date reached or passed
                double maturityAmount = calculateMaturityAmount(
                        rd.getMonthlyInstallment(),
                        rd.getInterestRate(),
                        rd.getTenureMonths()
                );
                rd.setMaturityAmount(maturityAmount);
                rd.setStatus(RecurringDeposit.DepositStatus.MATURED);
                message = "Payment done. RD is Matured.";
            } else {
                // ✅ Fully paid but waiting for maturity date
                rd.setStatus(PAID_WAIT_MATURE);
                message = "All installments paid. Waiting until maturity date.";
            }
        }

        // 9. Save updates
        rdRepo.save(rd);
        return message;
    }

    // ===============================
// Fine Calculation Helper
// ===============================
    private void applyMissedInstallmentFine(RecurringDeposit rd, LocalDate today) {
        // Months passed since start (full months only)
        long monthsElapsed = ChronoUnit.MONTHS.between(
                rd.getStartDate().withDayOfMonth(1),
                today.withDayOfMonth(1)
        );

        // Expected installments (cannot exceed tenure)
        long expectedInstallments = Math.min(monthsElapsed, rd.getTenureMonths());

        // Actual installments paid
        long actualInstallments = (long) (rd.getTotalDeposited() / rd.getMonthlyInstallment());

        double totalFine = 0;
        if (expectedInstallments > actualInstallments) {
            long missedInstallments = expectedInstallments - actualInstallments;

            double finePerMiss = 50.0; // Example fixed fine
            totalFine = finePerMiss * missedInstallments;
        }
        rd.setFine(totalFine);

    }


    // Step 1: Preview Withdrawal (like "Do you want to proceed?")
    public String previewWithdrawal(Long rdId, LocalDate requestDate) {
        RecurringDeposit rd = rdRepo.findById(rdId)
                .orElseThrow(() -> new RuntimeException("RD not found with id " + rdId));
        if(rd.getFine()>0){
            return "You should pay all missed Installments and its fine";
        }else {
            LocalDate maturityDate = rd.getStartDate().plusMonths(rd.getTenureMonths());

            if (requestDate.isBefore(maturityDate)) {
                // Premature withdrawal
                int monthsPaid = (int) (rd.getTotalDeposited() / rd.getMonthlyInstallment());
                double penaltyRate = 1.0; // 1% penalty
                double effectiverate = rd.getInterestRate() - penaltyRate;
                double amount = calculateMaturityAmount(rd.getMonthlyInstallment(), effectiverate, monthsPaid);

                return "Premature Withdrawal Detected!\n" +
                        "Installments paid: " + monthsPaid + " out of " + rd.getTenureMonths() + "\n" +
                        "Withdrawal amount (after 1% penalty): " + amount +
                        "\nDo you want to proceed?";
            } else {
                // Normal maturity withdrawal
                double maturityAmount = calculateMaturityAmount(
                        rd.getMonthlyInstallment(),
                        rd.getInterestRate(),
                        rd.getTenureMonths()
                );

                return "Eligible for Maturity Withdrawal\n" +
                        "Withdrawal amount: " + maturityAmount +
                        "\nNo penalty. Do you want to proceed?";
            }
        }
    }

    // Step 2: Final Withdrawal (after user clicks confirm)
    public double withdrawRD(Long rdId, LocalDate requestDate) {

        RecurringDeposit rd = rdRepo.findById(rdId)
                .orElseThrow(() -> new RuntimeException("RD not found with id " + rdId));

        LocalDate maturityDate = rd.getStartDate().plusMonths(rd.getTenureMonths());
        double payout;

        if (requestDate.isBefore(maturityDate)) {
            // Premature closure
            int monthsPaid = (int) (rd.getTotalDeposited() / rd.getMonthlyInstallment());
            double penaltyRate = 1.0; // 1% penalty
            double effectiverate = rd.getInterestRate()-penaltyRate;
            payout = calculateMaturityAmount(rd.getMonthlyInstallment(),  effectiverate,monthsPaid);

            rd.setStatus(RecurringDeposit.DepositStatus.PREMATURE_CLOSURE);

        } else {
            // Normal maturity closure
            payout = calculateMaturityAmount(
                    rd.getMonthlyInstallment(),
                    rd.getInterestRate(),
                    rd.getTenureMonths()
            );
            rd.setStatus(RecurringDeposit.DepositStatus.CLOSED);
        }
        rd.setMaturityAmount(payout);
        rdRepo.save(rd);
        return payout;
    }


}
