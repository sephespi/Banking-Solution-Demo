package demo.onlinebanking.helpers;

import java.util.UUID;

public class Token {

    private Token(){
    }

    public static String generateToken() {

        return UUID.randomUUID().toString();
    }

}
