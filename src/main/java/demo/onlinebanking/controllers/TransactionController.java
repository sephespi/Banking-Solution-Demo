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

@Controller
@RequestMapping("/transact")
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TransactRepository transactRepository;

    String errorMsg;
    String successMsg;
    User user;
    double currentBalance;
    double newBalance;
    LocalDateTime currentDateTime = LocalDateTime.now();

    // Deposit Transaction Controller
    @PostMapping("/deposit")
    public String deposit(@RequestParam("deposit_amount")String depositAmount,
                          @RequestParam("account_id") String accountID,
                          HttpSession session, RedirectAttributes redirectAttributes){

        if(depositAmount.isEmpty() || accountID.isEmpty()){
            errorMsg = "Deposit amount or Account cannot be empty";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        user = (User) session.getAttribute("user");

        int acc_id = Integer.parseInt(accountID);
        double depositAmountValue = Double.parseDouble(depositAmount);

        if(depositAmountValue < 500){
            errorMsg = "Minimum deposit amount is 500";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        currentBalance = accountRepository.getAccountBalance(user.getUser_id(),acc_id);

        transactRepository.logTransaction(acc_id,"Deposit", depositAmountValue,"Online","Success","Deposit transaction success", currentDateTime);

        newBalance = currentBalance + depositAmountValue;

        accountRepository.changeAccountBalanceById(newBalance, acc_id);
        redirectAttributes.addFlashAttribute("success","Amount Deposited Successfully");
        return "redirect:/app/dashboard";

    }

    // Transfer Transaction Controller
    @PostMapping("/transfer")
    public String transfer(@RequestParam("transfer_from")String transfer_from,
                           @RequestParam("transfer_to") String transfer_to,
                           @RequestParam("transfer_amount")String transfer_amount,
                           HttpSession session, RedirectAttributes redirectAttributes){

        if(transfer_from.isEmpty() || transfer_to.isEmpty() || transfer_amount.isEmpty()){
            errorMsg = "Transfer account from and to are required. Transfer amount also cannot be empty";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        int transferFromId = Integer.parseInt(transfer_from);
        int transferToId = Integer.parseInt(transfer_to);
        double transferAmount = Double.parseDouble(transfer_amount);

        if(transferFromId == transferToId) {
            errorMsg = "Transferring from and to same account. Transaction failed.";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        if (transferAmount < 500){
            errorMsg = "Minimum transfer amount is 500";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        user = (User) session.getAttribute("user");

        double currentBalanceTransferFrom = accountRepository.getAccountBalance(user.getUser_id(), transferFromId);

        if (currentBalanceTransferFrom < transferAmount){
            errorMsg = "You have insufficient funds to transfer";
            transactRepository.logTransaction(transferFromId,"Transfer", transferAmount, "Online", "Failed", "Insufficient funds", currentDateTime);
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        double currentBalanceTransferTo = accountRepository.getAccountBalance(user.getUser_id(), transferToId);

        double newBalanceTransferFrom = currentBalanceTransferFrom - transferAmount;
        double newBalanceTransferTo = currentBalanceTransferTo + transferAmount;

        accountRepository.changeAccountBalanceById(newBalanceTransferFrom, transferFromId);

        accountRepository.changeAccountBalanceById(newBalanceTransferTo, transferToId);

        transactRepository.logTransaction(transferFromId,"Transfer", transferAmount, "Online", "Success", "Transfer transaction success", currentDateTime);
        successMsg = "Transfer transaction completed successfully";
        redirectAttributes.addFlashAttribute("success", successMsg);
        return "redirect:/app/dashboard";

    }

    // Withdrawal Transaction Controller
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("withdrawal_amount")String withdrawalAmount,
                           @RequestParam("account_id")String accountID,
                           HttpSession session,
                           RedirectAttributes redirectAttributes){

        if(withdrawalAmount.isEmpty() || accountID.isEmpty()) {
            errorMsg = "Withdrawal Amount and Account must be specified";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        double withdrawal_amount = Double.parseDouble(withdrawalAmount);
        int account_id = Integer.parseInt(accountID);

        if (withdrawal_amount < 500){
            errorMsg = "Minimum withdraw amount is 500";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        user = (User) session.getAttribute("user");

        currentBalance = accountRepository.getAccountBalance(user.getUser_id(), account_id);
        newBalance = currentBalance - withdrawal_amount;

        if (currentBalance < withdrawal_amount){
            errorMsg = "You have insufficient funds to withdraw";
            transactRepository.logTransaction(account_id,"Withdraw", withdrawal_amount, "Online", "Failed", "Insufficient funds", currentDateTime);
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        accountRepository.changeAccountBalanceById(newBalance, account_id);

        transactRepository.logTransaction(account_id,"Withdraw", withdrawal_amount, "Online", "Success", "Withdraw transaction success", currentDateTime);
        successMsg = "Withdraw Transaction completed successfully!";
        redirectAttributes.addFlashAttribute("success", successMsg);
        return "redirect:/app/dashboard";

    }

    // Payment Transaction Controller
    @PostMapping("/payment")
    public String payment(@RequestParam("beneficiary")String beneficiary,
                          @RequestParam("account_number")String account_number,
                          @RequestParam("account_id")String account_id,
                          @RequestParam("reference")String reference,
                          @RequestParam("payment_amount")String payment_amount,
                          HttpSession session,
                          RedirectAttributes redirectAttributes){

        if(beneficiary.isEmpty() || account_number.isEmpty() || account_id.isEmpty() || payment_amount.isEmpty()){
            errorMsg = "All fields are required";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        int accountID = Integer.parseInt(account_id);
        double paymentAmount = Double.parseDouble(payment_amount);

        if(paymentAmount == 0){
            errorMsg = "Payment amount cannot be zero (0). Please enter a valid amount.";
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        user = (User) session.getAttribute("user");

        currentBalance = accountRepository.getAccountBalance(user.getUser_id(), accountID);

        if(currentBalance < paymentAmount){
            errorMsg = "You have insufficient funds to perform this payment";
            transactRepository.logTransaction(accountID,"Payment", paymentAmount, "Online", "Failed", "Insufficient funds", currentDateTime);
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/app/dashboard";
        }

        newBalance = currentBalance - paymentAmount;
        String reasonCode = "Payment transaction completed successfully!";
        paymentRepository.makePayment(accountID, beneficiary, account_number, paymentAmount, reference, "success", reasonCode, currentDateTime);

        accountRepository.changeAccountBalanceById(newBalance, accountID);

        transactRepository.logTransaction(accountID ,"Payment", paymentAmount, "Online", "Success", "Payment transaction success", currentDateTime);
        successMsg = reasonCode;
        redirectAttributes.addFlashAttribute("success", successMsg);
        return "redirect:/app/dashboard";

    }

    }
