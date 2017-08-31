package cn.vigor.modules.sys.utils;
import java.math.BigInteger;
public class Encr 
{
	private static final int RADIX = 16;
	private static final String SEED = "0933910847463829827159347601486730416058";
	
	public Encr() 
	{
	}
	
	public boolean init()
	{
		return true;
	}
	
	public String buildSignature(String mac, String username, String company, String products)
	{
		try
		{
			BigInteger bi_mac      = new BigInteger(mac.getBytes());
			BigInteger bi_username = new BigInteger(username.getBytes());
			BigInteger bi_company  = new BigInteger(company.getBytes());
			BigInteger bi_products = new BigInteger(products.getBytes());
			
			BigInteger bi_r0 = new BigInteger(SEED);
			BigInteger bi_r1 = bi_r0.xor(bi_mac);
			BigInteger bi_r2 = bi_r1.xor(bi_username);
			BigInteger bi_r3 = bi_r2.xor(bi_company);
			BigInteger bi_r4 = bi_r3.xor(bi_products);
			
			return bi_r4.toString(RADIX);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static final boolean checkSignatureShort(String signature, String verify)
	{
		return getSignatureShort(signature).equalsIgnoreCase(verify);
	}

	public static final String getSignatureShort(String signature)
	{
		String retval="";
		if (signature==null) return retval;
		int len = signature.length();
		if (len<6) return retval;
		retval=signature.substring(len-5, len);
		
 		return retval;
	}
	
	
    /***
     * 加密
     * @param password
     * @return
     */
	public static final String encryptPassword(String password)
	{
		if (password==null) return "";
		if (password.length()==0) return "";
		
		BigInteger bi_passwd = new BigInteger(password.getBytes());
		
		BigInteger bi_r0  = new BigInteger(SEED);
		BigInteger bi_r1  = bi_r0.xor(bi_passwd);
		
		return bi_r1.toString(RADIX); 
	}
	/**
	 * 解密
	 * @param encrypted
	 * @return
	 */
	public static final String decryptPassword(String encrypted)
	{
		if (encrypted==null) return "";
		if (encrypted.length()==0) return "";
		
		BigInteger bi_confuse  = new BigInteger(SEED);
		
		try
		{
			BigInteger bi_r1 = new BigInteger(encrypted, RADIX);
			BigInteger bi_r0 = bi_r1.xor(bi_confuse);
			
			return new String(bi_r0.toByteArray()); 
		}
		catch(Exception e)
		{
			return "";
		}
	}
    
    public  static final String PASSWORD_ENCRYPTED_PREFIX = "Encrypted ";
    
 
    public static final String encryptPasswordIfNotUsingVariables(String password)
    {
        String encrPassword = "";
        encrPassword = PASSWORD_ENCRYPTED_PREFIX+Encr.encryptPassword(password);
        return encrPassword;
    }

    public static final String decryptPasswordOptionallyEncrypted(String password)
    {
        if (password != null && password.startsWith(PASSWORD_ENCRYPTED_PREFIX)) 
        {
            return Encr.decryptPassword( password.substring(PASSWORD_ENCRYPTED_PREFIX.length()) );
        }
        return password;
    }
    

    public static void main(String[] args) {
    	System.out.println( Encr.encryptPassword("http://192.168.7.149:13900/activiti-explorer/#processmodel?rmd=234")+":url");
    	System.out.println("687474703a2f2f3139322e3136382e372e3134393a31333930302f61637469766974692d6578706c6f7265722f2370726f61dbebdca505c39788f40ba37483c0fc8e:" + Encr.decryptPassword("687474703a2f2f3139322e3136382e372e3134393a31333930302f61637469766974692d6578706c6f7265722f2370726f61dbebdca505c39788f40ba37483c0fc8e"));
    	
    	
    	/*		if (args.length!=2) {
			printOptions();
			System.exit(9);
		}

		String option = args[0];
		String password = args[1];

		if (Const.trim(option).substring(1).equalsIgnoreCase("kettle")) {
			// Kettle password obfuscation
			//
			String obfuscated = Encr.encryptPassword(password);
			System.out.println(PASSWORD_ENCRYPTED_PREFIX+obfuscated);
			System.exit(0);
			
		} else if (Const.trim(option).substring(1).equalsIgnoreCase("carte")) {
			// Jetty password obfuscation
			//
			String obfuscated = Password.obfuscate(password);
			System.out.println(obfuscated);
			System.exit(0);
			
		} else {
			// Unknown option, print usage
			//
			System.err.println("Unknown option '"+option+"'\n");
			printOptions();
			System.exit(1);
		}
*/
		
		
	}

	
}
