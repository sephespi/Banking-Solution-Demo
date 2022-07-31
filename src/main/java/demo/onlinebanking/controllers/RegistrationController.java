package demo.onlinebanking.controllers;

import demo.onlinebanking.helpers.HTML;
import demo.onlinebanking.helpers.Token;
import demo.onlinebanking.mail_messenger.MailMessenger;
import demo.onlinebanking.models.User;
import demo.onlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("all")
@Controller
public class RegistrationController {

    private final Logger logger = Logger.getLogger(RegistrationController.class.getName());

    private static final SecureRandom RANDOM = new SecureRandom();
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        ModelAndView getRegisterPage = new ModelAndView("register");
        getRegisterPage.addObject("PageTitle", "Register");
        logger.log(Level.INFO,"Location: Register Page");
        return getRegisterPage;
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid @ModelAttribute("registerUser") User user,
                                 BindingResult result,
                                 @RequestParam("first_name") String firstName,
                                 @RequestParam("last_name") String lastName,
                                 @RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 @RequestParam("confirm_password") String confirmPassword) throws MessagingException {

        ModelAndView registerPage = new ModelAndView("register");

        if (result.hasErrors() && confirmPassword.isEmpty()) {
            registerPage.addObject("error", "Please confirm your password");
            return registerPage;
        }

        if (!password.equals(confirmPassword)) {
            registerPage.addObject("passwordMismatch", "Password do not match");
            return registerPage;
        }

        String token = Token.generateToken();

        int bound = 123;
        int code = bound * RANDOM.nextInt(bound);

        String emailBody = HTML.emailVerification(token, Integer.toString(code));

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        userRepository.registerUser(firstName, lastName, email, hashedPassword, token, code);

        MailMessenger.htmlMailMessenger("no-reply@jabilee.com", email, "Account Verification", emailBody);

        String successMessage = "Account successfully registered! Please check your email to verify your account.";
        registerPage.addObject("success", successMessage);
        return registerPage;
    }

}
