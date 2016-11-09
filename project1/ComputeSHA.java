import java.io.*;
import java.security.*;
 

public class ComputeSHA {
	
	public static void main(String[] args)throws IOException{
		int buffersize = 256 * 1024;
		FileInputStream fileInputStream = null;
		DigestInputStream digestInputStream = null;
		try{
			MessageDigest messageDigest = MessageDigest.getInstance("SHA");	
			//fileInputStream = new FileInputStream("O:/Eclipse/workspace/sample-input.txt");
			fileInputStream = new FileInputStream(args[0]);
			digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
			byte[] buffer = new byte[buffersize];
			while (digestInputStream.read(buffer)>0);
			messageDigest = digestInputStream.getMessageDigest();
			byte[] resultByteArray = messageDigest.digest();
			System.out.println(byteArrayToHex(resultByteArray));
		}catch (NoSuchAlgorithmException e){
			
		}finally{
			try{
				digestInputStream.close();
			}catch(Exception e){
				
			}
		}
	}
	public static String byteArrayToHex(byte[] byteArray) {
		char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'a','b','c','d','e','f' };
		char[] resultCharArray =new char[byteArray.length * 2];
		  int index = 0;

		  for (byte b : byteArray) {

		     resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];

		     resultCharArray[index++] = hexDigits[b& 0xf];

		  }
		  return new String(resultCharArray);
	
	}	
}

