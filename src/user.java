import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.X509EncodedKeySpec;

public class user
{
	BigInteger q;
	int x;// Private key
	BigInteger y;// Public key
	BigInteger k;// Shared key
	String name;

	public user(int q, int a, String name)
	{
		this.q = new BigInteger(q + "");
		this.x = (int) (Math.random() * (q - 2) + 1);
		BigInteger aBig = new BigInteger(a + "");
		BigInteger temp = aBig.pow(x);
		y = temp.mod(this.q);
		this.name = name;
	}

	public BigInteger calculateSharedKey(BigInteger remoteY)
	{
		BigInteger temp = remoteY.pow(x);
		k = temp.mod(q);
		return k;
	}

	// Kareem
	public void sendKey() throws SignatureException, NoSuchProviderException, NoSuchAlgorithmException,
			InvalidKeyException, IOException
	{
		String s = y + "";
		BufferedWriter writer;
		writer = Files.newBufferedWriter(new File(this.name + "y").toPath());
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

		FileInputStream fis = new FileInputStream(this.name + "y");
		BufferedInputStream bufin = new BufferedInputStream(fis);
		byte[] buffer = new byte[1024];
		int len;
		while ((len = bufin.read(buffer)) >= 0)
		{
			dsa.update(buffer, 0, len);
		}
		bufin.close();

		byte[] realSig = dsa.sign();

		FileOutputStream sigfos = new FileOutputStream(this.name + "sy");
		sigfos.write(realSig);
		sigfos.close();

		byte[] key = pub.getEncoded();
		FileOutputStream keyfos = new FileOutputStream(this.name + "puk");
		keyfos.write(key);
		keyfos.close();
	}

	// Omar
	public BigInteger receiveKey(String userName) throws Exception
	{
		FileInputStream keyfis = new FileInputStream(userName + "puk");
		byte[] encKey = new byte[keyfis.available()];
		keyfis.read(encKey);
		keyfis.close();

		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
		KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
		PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

		FileInputStream sigfis = new FileInputStream(userName + "sy");
		byte[] sigToVerify = new byte[sigfis.available()];
		sigfis.read(sigToVerify);
		sigfis.close();

		Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
		sig.initVerify(pubKey);

		FileInputStream datafis = new FileInputStream(userName + "y");
		BufferedInputStream bufin = new BufferedInputStream(datafis);

		byte[] buffer = new byte[1024];
		int len;
		while (bufin.available() != 0)
		{
			len = bufin.read(buffer);
			sig.update(buffer, 0, len);
		}
		bufin.close();

		boolean verifies = sig.verify(sigToVerify);
		System.out.println(userName + " signature verifies: " + verifies);

		if (!verifies)
		{
			return new BigInteger("-1");
		}

		BufferedReader reader = new BufferedReader(new FileReader(userName + "y"));
		BigInteger line = new BigInteger(reader.readLine());
		reader.close();

		return calculateSharedKey(line);
	}
}
