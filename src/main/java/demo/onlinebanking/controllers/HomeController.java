package demo.onlinebanking.controllers;

import demo.onlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class HomeController {

    private final Logger logger = Logger.getLogger(HomeController.class.getName());

    static final String ERROR = "error";
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public ModelAndView getHome() {
        ModelAndView getHomePage = new ModelAndView("home");
        getHomePage.addObject("PageTitle", "Home");
        logger.log(Level.INFO,"Location: Home Page");
        return getHomePage;
    }

    @GetMapping("/error")
    public ModelAndView getErrorPage() {
        ModelAndView getErrorPage = new ModelAndView(ERROR);
        getErrorPage.addObject("PageTitle", "Error");
        logger.log(Level.INFO,"Location: Error Page");
        return getErrorPage;
    }

    @GetMapping("/verify")
    public ModelAndView getVerifyPage(@RequestParam("token") String token, @RequestParam("code") String code) {
        ModelAndView getVerifyPage;
        String dbToken = userRepository.checkToken(token);

        if (dbToken == null) {
            getVerifyPage = new ModelAndView(ERROR);
            getVerifyPage.addObject(ERROR, "This session has expired");
            return getVerifyPage;
        }

        userRepository.verifyAccount(token, code);
        getVerifyPage = new ModelAndView("login");
        getVerifyPage.addObject("success", "Account Verification is Successful. Please login");
        logger.log(Level.INFO,"Location: Login Page");
        return getVerifyPage;
    }
}
