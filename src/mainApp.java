import java.math.BigInteger;

public class mainApp
{
	public static void main(String[] args)
	{
		int q = 353;
		int a = 3;

		user sender = new user(q, a, "user1");
		user receiver = new user(q, a, "user2");
		try
		{
			sender.sendKey();
			receiver.sendKey();
			BigInteger sharedKey1 = sender.receiveKey(receiver.name);
			BigInteger sharedKey2 = receiver.receiveKey(sender.name);
			if (sharedKey1.equals(new BigInteger("-1")) || sharedKey2.equals(new BigInteger("-1")))
			{
				System.out.println("Valued to verify signature");
			} else
			{
				System.out.println("Sharedkey 1: " + sharedKey1);
				System.out.println("Sharedkey 2: " + sharedKey2);
				System.out.println("Shared keys are equal? : " + (sharedKey1.equals(sharedKey2)));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
