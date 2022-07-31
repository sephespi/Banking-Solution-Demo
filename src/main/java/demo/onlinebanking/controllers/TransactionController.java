package demo.onlinebanking.controllers;

import demo.onlinebanking.models.User;
import demo.onlinebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/transact")
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;

    String errorMsg;
    String successMsg;
    User user;
    double currentBalance;
    double newBalance;

    // Deposit form
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

        newBalance = currentBalance + depositAmountValue;

        accountRepository.changeAccountBalanceById(newBalance, acc_id);
        redirectAttributes.addFlashAttribute("success","Amount Deposited Successfully");
        return "redirect:/app/dashboard";

    }

    // Transfer form
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
        double currentBalanceTransferTo = accountRepository.getAccountBalance(user.getUser_id(), transferToId);

        double newBalanceTransferFrom = currentBalanceTransferFrom - transferAmount;
        double newBalanceTransferTo = currentBalanceTransferTo + transferAmount;

        accountRepository.changeAccountBalanceById(newBalanceTransferFrom, transferFromId);

        accountRepository.changeAccountBalanceById(newBalanceTransferTo, transferToId);

        successMsg = "Transfer transaction completed successfully";
        redirectAttributes.addFlashAttribute("success", successMsg);
        return "redirect:/app/dashboard";

    }

    // Withdrawal form
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

        accountRepository.changeAccountBalanceById(newBalance, account_id);

        successMsg = "Withdraw Transaction completed successfully!";
        redirectAttributes.addFlashAttribute("success", successMsg);
        return "redirect:/app/dashboard";

    }

    }
