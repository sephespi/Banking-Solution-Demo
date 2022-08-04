package demo.onlinebanking.controllers;

import demo.onlinebanking.helpers.Token;
import demo.onlinebanking.models.User;
import demo.onlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class AuthenticationController {

    private static final Logger logger = Logger.getLogger(AuthenticationController.class.getName());

    String login = "login";
    String error = "error";
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        ModelAndView getLoginPage = new ModelAndView(login);
        String token = Token.generateToken();
        getLoginPage.addObject("token", token);
        getLoginPage.addObject("PageTitle", "Login");
        logger.log(Level.INFO,"Location: Login Page");
        return getLoginPage;
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        @RequestParam(value = "token", required = false) String token,
                        Model model,
                        HttpSession session) {

        if (email.isEmpty() || password.isEmpty()) {
            model.addAttribute(error, "Username or Password cannot be empty");
            return login;
        }

        String getEmailFromDatabase = userRepository.getUserEmail(email);

        if (getEmailFromDatabase != null) {
            String getPasswordFromDatabase = userRepository.getUserPassword(getEmailFromDatabase);
            if (!BCrypt.checkpw(password, getPasswordFromDatabase)) {
                model.addAttribute(error, "Incorrect Username or Password");
                return login;
            }
        }

        int verified = userRepository.isVerified(getEmailFromDatabase);

        if (verified != 1) {
            String msg = "This account is not verified. Please check your email and verify your account";
            model.addAttribute(error, msg);
            return login;
        }

        User user = userRepository.getUserDetails(getEmailFromDatabase);

        session.setAttribute("user", user);
        session.setAttribute("token", token);
        session.setAttribute("authenticated", true);

        return "redirect:/app/dashboard";

    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("logged_out", "User logged out successfully");
        return "redirect:/login";
    }

}
