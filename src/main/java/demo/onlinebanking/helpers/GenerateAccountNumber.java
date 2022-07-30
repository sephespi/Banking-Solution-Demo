package demo.onlinebanking.helpers;

import java.util.Random;

public class GenerateAccountNumber {

    public static int generateAccountNumber(){
        int accountNumber;
        Random rand = new Random();
        int bound = 1000;
        accountNumber = bound * rand.nextInt(bound);
        return accountNumber;
    }

}
