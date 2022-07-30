package demo.onlinebanking.controllers;

import demo.onlinebanking.models.Account;
import demo.onlinebanking.models.User;
import demo.onlinebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/app")
public class ApplicationController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/dashboard")
    public ModelAndView getDashboard(HttpSession session) {
        ModelAndView getDashboardPage = new ModelAndView("dashboard");

        User user = (User) session.getAttribute("user");

        List<Account> getAccounts = accountRepository.getUserAccountById(user.getUserId());

        BigDecimal getTotalAccountBalance = accountRepository.getTotalBalance(user.getUserId());

        getDashboardPage.addObject("userAccounts", getAccounts);
        getDashboardPage.addObject("totalBalance", getTotalAccountBalance);

        return getDashboardPage;
    }

}
