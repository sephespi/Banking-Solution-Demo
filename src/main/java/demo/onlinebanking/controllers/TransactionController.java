package demo.onlinebanking.controllers;

import demo.onlinebanking.models.User;
import demo.onlinebanking.repositories.AccountRepository;
import demo.onlinebanking.repositories.PaymentRepository;
import demo.onlinebanking.repositories.TransactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;


@SuppressWarnings("all")
@Controller
@RequestMapping("/transact")
public class TransactionController {

    String errorMsg;
    String successMsg;
    User user;
    double currentBalance;
    double newBalance;
    LocalDateTime currentDateTime = LocalDateTime.now();
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private TransactRepository transactRepository;

    // Deposit Transaction Controller
    @PostMapping("/deposit")
    public String deposit(@RequestParam("deposit_amount") String depositAmount,
                          @RequestParam("account_id") String accountID,
                          HttpSession session, RedirectAttributes redirectAttributes) {

        if (depositAmount.isEmpty() || accountID.isEmpty()) {
            errorMsg = "Deposit amount or Account cannot be empty";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        user = (User) session.getAttribute("user");

        int accountId = Integer.parseInt(accountID);
        double depositAmountValue = Double.parseDouble(depositAmount);

        if (depositAmountValue < 500) {
            errorMsg = "Minimum deposit amount is 500";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        currentBalance = accountRepository.getAccountBalance(user.getUser_id(), accountId);

        transactRepository.logTransaction(accountId, "Deposit", depositAmountValue, "Online", "Success", "Deposit transaction success", currentDateTime);

        newBalance = currentBalance + depositAmountValue;

        accountRepository.changeAccountBalanceById(newBalance, accountId);
        redirectAttributes.addFlashAttribute("success", "Amount Deposited Successfully");
        return "redirect:/app/dashboard";

    }

    // Transfer Transaction Controller
    @PostMapping("/transfer")
    public String transfer(@RequestParam("transfer_from") String transferFrom,
                           @RequestParam("transfer_to") String transferTo,
                           @RequestParam("transfer_amount") String transferAmount,
                           HttpSession session, RedirectAttributes redirectAttributes) {

        if (transferFrom.isEmpty() || transferTo.isEmpty() || transferAmount.isEmpty()) {
            errorMsg = "Transfer account from and to are required. Transfer amount also cannot be empty";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        int transferFromId = Integer.parseInt(transferFrom);
        int transferToId = Integer.parseInt(transferTo);
        double transAmount = Double.parseDouble(transferAmount);

        if (transferFromId == transferToId) {
            errorMsg = "Transferring from and to same account. Transaction failed.";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        if (transAmount < 500) {
            errorMsg = "Minimum transfer amount is 500";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        user = (User) session.getAttribute("user");

        double currentBalanceTransferFrom = accountRepository.getAccountBalance(user.getUser_id(), transferFromId);

        if (currentBalanceTransferFrom < transAmount) {
            errorMsg = "You have insufficient funds to transfer";
            transactRepository.logTransaction(transferFromId, "Transfer", transAmount, "Online", "Failed", "Insufficient funds", currentDateTime);
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        double currentBalanceTransferTo = accountRepository.getAccountBalance(user.getUser_id(), transferToId);

        double newBalanceTransferFrom = currentBalanceTransferFrom - transAmount;
        double newBalanceTransferTo = currentBalanceTransferTo + transAmount;

        accountRepository.changeAccountBalanceById(newBalanceTransferFrom, transferFromId);

        accountRepository.changeAccountBalanceById(newBalanceTransferTo, transferToId);

        transactRepository.logTransaction(transferFromId, "Transfer", transAmount, "Online", "Success", "Transfer transaction success", currentDateTime);
        successMsg = "Transfer transaction completed successfully";
        redirectAttributes.addFlashAttribute("success", successMsg);
        return "redirect:/app/dashboard";

    }

    // Withdrawal Transaction Controller
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("withdrawal_amount") String withdrawalAmount,
                           @RequestParam("account_id") String accountID,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

        if (withdrawalAmount.isEmpty() || accountID.isEmpty()) {
            errorMsg = "Withdrawal Amount and Account must be specified";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        double withdrawAmount = Double.parseDouble(withdrawalAmount);
        int accountId = Integer.parseInt(accountID);

        if (withdrawAmount < 500) {
            errorMsg = "Minimum withdraw amount is 500";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        user = (User) session.getAttribute("user");

        currentBalance = accountRepository.getAccountBalance(user.getUser_id(), accountId);
        newBalance = currentBalance - withdrawAmount;

        if (currentBalance < withdrawAmount) {
            errorMsg = "You have insufficient funds to withdraw";
            transactRepository.logTransaction(accountId, "Withdraw", withdrawAmount, "Online", "Failed", "Insufficient funds", currentDateTime);
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        accountRepository.changeAccountBalanceById(newBalance, accountId);

        transactRepository.logTransaction(accountId, "Withdraw", withdrawAmount, "Online", "Success", "Withdraw transaction success", currentDateTime);
        successMsg = "Withdraw Transaction completed successfully!";
        redirectAttributes.addFlashAttribute("success", successMsg);
        return "redirect:/app/dashboard";

    }

    // Payment Transaction Controller
    @PostMapping("/payment")
    public String payment(@RequestParam("beneficiary") String beneficiary,
                          @RequestParam("account_number") String accountNumber,
                          @RequestParam("account_id") String accountId,
                          @RequestParam("reference") String reference,
                          @RequestParam("payment_amount") String paymentAmount,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {

        if (beneficiary.isEmpty() || accountNumber.isEmpty() || accountId.isEmpty() || paymentAmount.isEmpty()) {
            errorMsg = "All fields are required";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        int accountID = Integer.parseInt(accountId);
        double payAmount = Double.parseDouble(paymentAmount);

        if (payAmount == 0) {
            errorMsg = "Payment amount cannot be zero (0). Please enter a valid amount.";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        user = (User) session.getAttribute("user");

        currentBalance = accountRepository.getAccountBalance(user.getUser_id(), accountID);

        if (currentBalance < payAmount) {
            errorMsg = "You have insufficient funds to perform this payment";
            transactRepository.logTransaction(accountID, "Payment", payAmount, "Online", "Failed", "Insufficient funds", currentDateTime);
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        newBalance = currentBalance - payAmount;
        String reasonCode = "Payment transaction completed successfully!";
        paymentRepository.makePayment(accountID, beneficiary, accountNumber, payAmount, reference, "success", reasonCode, currentDateTime);

        accountRepository.changeAccountBalanceById(newBalance, accountID);

        transactRepository.logTransaction(accountID, "Payment", payAmount, "Online", "Success", "Payment transaction success", currentDateTime);
        successMsg = reasonCode;
        redirectAttributes.addFlashAttribute("success", successMsg);
        return "redirect:/app/dashboard";

    }

}
