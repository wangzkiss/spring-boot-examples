package cn.vigor.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * AES加解密算法  
 * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符
 * 此处使用AES-128-CBC加密模式，key需要为16位。  
 * @author zhangfeng
 */
public class AESUtil
{
    public static void main(String[] args)
    {
        System.out.println(AESUtil.encForTD("123456"));
        System.out.println(AESUtil.encForTD("root"));
        System.out.println(AESUtil.encForTD("hdfs"));
        System.out.println(AESUtil.encForTD("hive"));
        System.out.println(AESUtil.decForTD("icKy4m/+UPNa3ZzTp6tjKQ=="));
    }
    
    /**
     * 平台统一的加密方法
     * @param sSrc
     * @return
     */
    public static String encForTD(String sSrc)
    {
        return encrypt(sSrc, "tospurexmindcomp");
    }
    
    /**
     * 平台统一的解密方法
     * @param sSrc
     * @return
     */
    public static String decForTD(String sSrc)
    {
        return decrypt(sSrc, "tospurexmindcomp");
    }

    /**
     * 加密，key与iv设为一致
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public static String encrypt(String sSrc, String sKey)
    {
        if (sKey == null)
        {
            System.out.print("Key为空null");
            return null;
        }
        
        // 判断Key是否为16位    
        if (sKey.length() != 16)
        {
            System.out.print("Key长度不是16位");
            return null;
        }
        
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        
        try
        {
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
          
            //使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(sKey.getBytes());
            
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            
            byte[] encrypted = cipher.doFinal(sSrc.getBytes());
            
            //此处使用BAES64做转码功能，同时能起到2次加密的作用。
            return Base64.encodeBase64String(encrypted);
        }
        catch (Exception e)
        {
        }
        
        return null;
    }
    
    /**
     * 解密,key与iv设为一致
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public static String decrypt(String sSrc, String sKey)
    {
        // 判断Key是否正确    
        if (sKey == null)
        {
            System.out.print("Key为空null");
            
            return null;
        }
        
        // 判断Key是否为16位    
        if (sKey.length() != 16)
        {
            System.out.print("Key长度不是16位");
            
            return null;
        }
        
        try
        {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(sKey.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            
            //先用bAES64解密    
            byte[] encrypted1 = Base64.decodeBase64(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            
            String originalString = new String(original);
            
            return originalString;
        }
        catch (Exception e)
        {
        }
        
        return null;
    }
    
    /**
     * 生成随机密码
     * @param len
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String generateRandomCharAndNumber(Integer len)
    {
        StringBuffer sb = new StringBuffer();
        
        for (Integer i = 0; i < len; i++)
        {
            int intRand = (int) (Math.random() * 52);
            int numValue = (int) (Math.random() * 10);
            char base = (intRand < 26) ? 'A' : 'a';
            char c = (char) (base + intRand % 26);
            if (numValue % 2 == 0)
            {
                sb.append(c);
            }
            else
            {
                sb.append(numValue);
            }
        }
        
        return sb.toString().toLowerCase();
    }
    
    /**
     * 转换日期方法
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String tranDate(String format)
    {
        Calendar calendar = Calendar.getInstance();
        
        calendar.setTime(new Date());
        
        // 将处理后的时间转换为指定的日期格式返回
        String tDate = new SimpleDateFormat(format).format(calendar.getTime());
        
        return tDate;
    }
}