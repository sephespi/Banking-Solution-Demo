package demo.onlinebanking.controllers;

import demo.onlinebanking.helpers.GenerateAccountNumber;
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
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/create_account")
    public String createAccount(@RequestParam("account_name") String accountName,
                                @RequestParam("account_type") String accountType,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {

        if (accountName.isEmpty() || accountType.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Account Name and Account Type is required");
            return "redirect:/app/dashboard";
        }

        User user = (User) session.getAttribute("user");

        int setAccountNumber = GenerateAccountNumber.generateAccountNumber();

        String newAccountNumber = Integer.toString(setAccountNumber);

        accountRepository.createNewAccount(user.getUser_id(), newAccountNumber, accountName, accountType);

        redirectAttributes.addFlashAttribute("success", "New account created successfully");

        return "redirect:/app/dashboard";

    }

}
