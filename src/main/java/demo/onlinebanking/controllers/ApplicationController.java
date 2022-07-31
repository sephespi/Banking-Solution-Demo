package demo.onlinebanking.controllers;

import demo.onlinebanking.models.Account;
import demo.onlinebanking.models.PaymentHistory;
import demo.onlinebanking.models.TransactionHistory;
import demo.onlinebanking.models.User;
import demo.onlinebanking.repositories.AccountRepository;
import demo.onlinebanking.repositories.PaymentHistoryRepository;
import demo.onlinebanking.repositories.TransactionHistoryRepository;
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

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    User user;

    @GetMapping("/dashboard")
    public ModelAndView getDashboard(HttpSession session) {
        ModelAndView getDashboardPage = new ModelAndView("dashboard");

        user = (User) session.getAttribute("user");

        List<Account> getUserAccounts = accountRepository.getUserAccountById(user.getUser_id());

        BigDecimal getTotalAccountBalance = accountRepository.getTotalBalance(user.getUser_id());

        getDashboardPage.addObject("userAccounts", getUserAccounts);
        getDashboardPage.addObject("totalBalance", getTotalAccountBalance);

        return getDashboardPage;
    }

    @GetMapping("/payment_history")
    public ModelAndView getPaymentHistory(HttpSession session) {
        ModelAndView getPaymentHistoryPage = new ModelAndView("paymentHistory");
        user = (User) session.getAttribute("user");

        List<PaymentHistory> userPaymentHistory = paymentHistoryRepository.getPaymentRecordsById(user.getUser_id());

        getPaymentHistoryPage.addObject("payment_history",userPaymentHistory);

        return getPaymentHistoryPage;

    }

    @GetMapping("transact_history")
    public ModelAndView getTransactHistory(HttpSession session){
        ModelAndView getTransactHistoryPage = new ModelAndView("transactHistory");
        user = (User) session.getAttribute("user");

        List<TransactionHistory> userTransactHistory = transactionHistoryRepository.getTransactionRecordsById(user.getUser_id());

        getTransactHistoryPage.addObject("transact_history", userTransactHistory);

        return getTransactHistoryPage;
    }

}
