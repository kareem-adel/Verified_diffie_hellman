import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

/**
 * Created by kareem on 3/29/2017.
 */
public class mainApp {
    public static void main(String[] args) {
        user sender = new user(7, 7);
        try {
            sender.sendKey();
        } catch (SignatureException | NoSuchProviderException | NoSuchAlgorithmException | IOException | InvalidKeyException e) {
            e.printStackTrace();
        }

        user receiver = new user(7, 7);
        System.out.println(sender.receiveKey());
    }
}
