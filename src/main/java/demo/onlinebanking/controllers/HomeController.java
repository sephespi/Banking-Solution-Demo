package demo.onlinebanking.controllers;

import demo.onlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    static final String ERROR = "error";

    @GetMapping("/")
    public ModelAndView getHome(){
        ModelAndView getHomePage = new ModelAndView("home");
        getHomePage.addObject("PageTitle", "Home");
        System.out.println("Location: Home Page");
        return getHomePage;
    }

    @GetMapping("/error")
    public ModelAndView getErrorPage(){
        ModelAndView getErrorPage = new ModelAndView(ERROR);
        getErrorPage.addObject("PageTitle", "Error");
        System.out.println("Location: Error Page");
        return getErrorPage;
    }

    @GetMapping("/verify")
    public ModelAndView getVerifyPage(@RequestParam("token") String token, @RequestParam("code") String code){
        ModelAndView getVerifyPage;
        String dbToken = userRepository.checkToken(token);

        if(dbToken == null){
            getVerifyPage = new ModelAndView(ERROR);
            getVerifyPage.addObject(ERROR, "This session has expired");
            return getVerifyPage;
        }

        userRepository.verifyAccount(token, code);
        getVerifyPage = new ModelAndView("login");
        getVerifyPage.addObject("success", "Account Verification is Successful. Please login");
        System.out.println("Location: Verify Page");
        return getVerifyPage;
    }
}
