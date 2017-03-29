import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.util.Random;

/**
 * Created by kareem on 3/29/2017.
 */
public class user {

    int q;
    //int a;
    int x;//private
    int y;//public
    int k;//sharedkey

    public user(int q, int a) {
        this.q = q;
        x = (int) (Math.random() % q);
        y = (int) (Math.pow(a, x) % q);
    }

    public void calculateSharedKey(int remoteY) {
        k = (int) (Math.pow(remoteY, x) % q);
    }


    //kareem
    public void sendKey() throws SignatureException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        String s = String.valueOf(y);
        BufferedWriter writer;
        writer = Files.newBufferedWriter(new File("y").toPath());
        writer.write(s, 0, s.length());
        writer.flush();
        writer.close();

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey priv = pair.getPrivate();
        PublicKey pub = pair.getPublic();

        Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
        dsa.initSign(priv);

        FileInputStream fis = new FileInputStream("y");
        BufferedInputStream bufin = new BufferedInputStream(fis);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bufin.read(buffer)) >= 0) {
            dsa.update(buffer, 0, len);
        }
        bufin.close();

        byte[] realSig = dsa.sign();

        FileOutputStream sigfos = new FileOutputStream("sy");
        sigfos.write(realSig);
        sigfos.close();

        byte[] key = pub.getEncoded();
        FileOutputStream keyfos = new FileOutputStream("puk");
        keyfos.write(key);
        keyfos.close();
    }


    //omar (should return the shared key)
    public int receiveKey() {

        return 0;
    }
}
