package demo.onlinebanking.helpers;

import java.security.SecureRandom;
import java.util.Random;

public class GenerateAccountNumber {

    private static final SecureRandom RANDOM = new SecureRandom();
    public static int generateAccountNumber(){
        int accountNumber;

        int bound = 1000;
        accountNumber = bound * RANDOM.nextInt(bound);
        return accountNumber;
    }

}
