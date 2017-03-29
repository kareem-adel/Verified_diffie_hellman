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

    public void calculateSharedKey(int remoteY){
        k = (int) (Math.pow(remoteY, x) % q);
    }


    //kareem
    public void sendKey(){

    }


    //omar
    public void receiveKey(){

    }
}
