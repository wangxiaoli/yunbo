package com.yunbo.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.ab.activity.AbActivity;

import android.content.Context;
import android.telephony.TelephonyManager;

public class ENDEUtil {  

	    /**
	     * ECB加密,不要IV
	     * @param key 密钥
	     * @param data 明文
	     * @return Base64编码的密文
	     * @throws Exception
	     */
	    public static byte[] des3EncodeECB(byte[] key, byte[] data)
	            throws Exception {

	        Key deskey = null;
	        DESedeKeySpec spec = new DESedeKeySpec(key);
	        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
	        deskey = keyfactory.generateSecret(spec);

	        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");

	        cipher.init(Cipher.ENCRYPT_MODE, deskey);
	        byte[] bOut = cipher.doFinal(data);

	        return bOut;
	    }

	    /**
	     * ECB解密,不要IV
	     * @param key 密钥
	     * @param data Base64编码的密文
	     * @return 明文
	     * @throws Exception
	     */
	    public static byte[] ees3DecodeECB(byte[] key, byte[] data)
	            throws Exception {

	        Key deskey = null;
	        DESedeKeySpec spec = new DESedeKeySpec(key);
	        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
	        deskey = keyfactory.generateSecret(spec);

	        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");

	        cipher.init(Cipher.DECRYPT_MODE, deskey);

	        byte[] bOut = cipher.doFinal(data);

	        return bOut;

	    }

	    /**
	     * CBC加密
	     * @param key 密钥
	     * @param keyiv IV
	     * @param data 明文
	     * @return Base64编码的密文
	     * @throws Exception
	     */
	    public static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data)
	            throws Exception {

	        Key deskey = null;
	        DESedeKeySpec spec = new DESedeKeySpec(key);
	        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
	        deskey = keyfactory.generateSecret(spec);

	        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
	        IvParameterSpec ips = new IvParameterSpec(keyiv);
	        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
	        byte[] bOut = cipher.doFinal(data);

	        return bOut;
	    }

	    /**
	     * CBC解密
	     * @param key 密钥
	     * @param keyiv IV
	     * @param data Base64编码的密文
	     * @return 明文
	     * @throws Exception
	     */
	    public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data)
	            throws Exception {

	        Key deskey = null;
	        DESedeKeySpec spec = new DESedeKeySpec(key);
	        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
	        deskey = keyfactory.generateSecret(spec);

	        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
	        IvParameterSpec ips = new IvParameterSpec(keyiv);

	        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

	        byte[] bOut = cipher.doFinal(data);

	        return bOut;

	    } 
	    
	    public static  String getMyUUID(AbActivity abActivity){

	    	  final TelephonyManager tm = (TelephonyManager) abActivity.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);   

	    	  final String tmDevice, tmSerial, tmPhone, androidId;   

	    	  tmDevice = "" + tm.getDeviceId();  

	    	  tmSerial = "" + tm.getSimSerialNumber();   

	    	  androidId = "" + android.provider.Settings.Secure.getString(abActivity.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);   

	    	  UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());   

	    	  String uniqueId = deviceUuid.toString(); 
	    	  return uniqueId;

	    	 } 
	    public static  String getMyUUID1(AbActivity abActivity){
   final String   androidId;   
 
	    	  androidId = "" + android.provider.Settings.Secure.getString(abActivity.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);   

	    	  UUID deviceUuid = new UUID(androidId.hashCode(), ((long)androidId.hashCode() << 32) | androidId.hashCode());   

	    	  String uniqueId = deviceUuid.toString(); 
	    	  return uniqueId;

	    	 } 
	        private static String RSA = "RSA";  
	      
	        /** 
	         * 随机生成RSA密钥对(默认密钥长度为1024) 
	         *  
	         * @return 
	         */  
	        public static KeyPair generateRSAKeyPair()  
	        {  
	            return generateRSAKeyPair(1024);  
	        }  
	      
	        /** 
	         * 随机生成RSA密钥对 
	         *  
	         * @param keyLength 
	         *            密钥长度，范围：512～2048<br> 
	         *            一般1024 
	         * @return 
	         */  
	        public static KeyPair generateRSAKeyPair(int keyLength)  
	        {  
	            try  
	            {  
	                KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);  
	                kpg.initialize(keyLength);  
	                return kpg.genKeyPair();  
	            } catch (NoSuchAlgorithmException e)  
	            {  
	                e.printStackTrace();  
	                return null;  
	            }  
	        }  
	      
	        /** 
	         * 用公钥加密 <br> 
	         * 每次加密的字节数，不能超过密钥的长度值减去11 
	         *  
	         * @param data 
	         *            需加密数据的byte数据 
	         * @param pubKey 
	         *            公钥 
	         * @return 加密后的byte型数据 
	         */  
	        public static byte[] encryptData(byte[] data, PublicKey publicKey)  
	        {  
	            try  
	            {  
	                Cipher cipher = Cipher.getInstance(RSA);  
	                // 编码前设定编码方式及密钥  
	                cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
	                // 传入编码数据并返回编码结果  
	                return cipher.doFinal(data);  
	            } catch (Exception e)  
	            {  
	                e.printStackTrace();  
	                return null;  
	            }  
	        }  
	      
	        /** 
	         * 用私钥解密 
	         *  
	         * @param encryptedData 
	         *            经过encryptedData()加密返回的byte数据 
	         * @param privateKey 
	         *            私钥 
	         * @return 
	         */  
	        public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey)  
	        {  
	            try  
	            {  
	                Cipher cipher = Cipher.getInstance(RSA);  
	                cipher.init(Cipher.DECRYPT_MODE, privateKey);  
	                return cipher.doFinal(encryptedData);  
	            } catch (Exception e)  
	            {  
	                return null;  
	            }  
	        }  
	      
	        /** 
	         * 通过公钥byte[](publicKey.getEncoded())将公钥还原，适用于RSA算法 
	         *  
	         * @param keyBytes 
	         * @return 
	         * @throws NoSuchAlgorithmException 
	         * @throws InvalidKeySpecException 
	         */  
	        public static PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException,  
	                InvalidKeySpecException  
	        {  
	            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
	            KeyFactory keyFactory = KeyFactory.getInstance(RSA);  
	            PublicKey publicKey = keyFactory.generatePublic(keySpec);  
	            return publicKey;  
	        }  
	      
	        /** 
	         * 通过私钥byte[]将公钥还原，适用于RSA算法 
	         *  
	         * @param keyBytes 
	         * @return 
	         * @throws NoSuchAlgorithmException 
	         * @throws InvalidKeySpecException 
	         */  
	        public static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException,  
	                InvalidKeySpecException  
	        {  
	            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);  
	            KeyFactory keyFactory = KeyFactory.getInstance(RSA);  
	            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);  
	            return privateKey;  
	        }  
	      
	        /** 
	         * 使用N、e值还原公钥 
	         *  
	         * @param modulus 
	         * @param publicExponent 
	         * @return 
	         * @throws NoSuchAlgorithmException 
	         * @throws InvalidKeySpecException 
	         */  
	        public static PublicKey getPublicKey(String modulus, String publicExponent)  
	                throws NoSuchAlgorithmException, InvalidKeySpecException  
	        {  
	            BigInteger bigIntModulus = new BigInteger(modulus);  
	            BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);  
	            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);  
	            KeyFactory keyFactory = KeyFactory.getInstance(RSA);  
	            PublicKey publicKey = keyFactory.generatePublic(keySpec);  
	            return publicKey;  
	        }  
	      
	        /** 
	         * 使用N、d值还原私钥 
	         *  
	         * @param modulus 
	         * @param privateExponent 
	         * @return 
	         * @throws NoSuchAlgorithmException 
	         * @throws InvalidKeySpecException 
	         */  
	        public static PrivateKey getPrivateKey(String modulus, String privateExponent)  
	                throws NoSuchAlgorithmException, InvalidKeySpecException  
	        {  
	            BigInteger bigIntModulus = new BigInteger(modulus);  
	            BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);  
	            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);  
	            KeyFactory keyFactory = KeyFactory.getInstance(RSA);  
	            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);  
	            return privateKey;  
	        }  
	      
	        /** 
	         * 从字符串中加载公钥 
	         *  
	         * @param publicKeyStr 
	         *            公钥数据字符串 
	         * @throws Exception 
	         *             加载公钥时产生的异常 
	         */  
	        public static PublicKey loadPublicKey(String publicKeyStr) throws Exception  
	        {  
	            try  
	            {  
	                byte[] buffer =  decode(publicKeyStr);  
	                KeyFactory keyFactory = KeyFactory.getInstance(RSA);  
	                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);  
	                return (RSAPublicKey) keyFactory.generatePublic(keySpec);  
	            } catch (NoSuchAlgorithmException e)  
	            {  
	                throw new Exception("无此算法");  
	            } catch (InvalidKeySpecException e)  
	            {  
	                throw new Exception("公钥非法");  
	            } catch (NullPointerException e)  
	            {  
	                throw new Exception("公钥数据为空");  
	            }  
	        }  
	      
	        /** 
	         * 从字符串中加载私钥<br> 
	         * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。 
	         *  
	         * @param privateKeyStr 
	         * @return 
	         * @throws Exception 
	         */  
	        public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception  
	        {  
	            try  
	            {  
	                byte[] buffer =  decode(privateKeyStr);  
	                // X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);  
	                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);  
	                KeyFactory keyFactory = KeyFactory.getInstance(RSA);  
	                return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  
	            } catch (NoSuchAlgorithmException e)  
	            {  
	                throw new Exception("无此算法");  
	            } catch (InvalidKeySpecException e)  
	            {  
	                throw new Exception("私钥非法");  
	            } catch (NullPointerException e)  
	            {  
	                throw new Exception("私钥数据为空");  
	            }  
	        }  
	      
	        /** 
	         * 从文件中输入流中加载公钥 
	         *  
	         * @param in 
	         *            公钥输入流 
	         * @throws Exception 
	         *             加载公钥时产生的异常 
	         */  
	        public static PublicKey loadPublicKey(InputStream in) throws Exception  
	        {  
	            try  
	            {  
	                return loadPublicKey(readKey(in));  
	            } catch (IOException e)  
	            {  
	                throw new Exception("公钥数据流读取错误");  
	            } catch (NullPointerException e)  
	            {  
	                throw new Exception("公钥输入流为空");  
	            }  
	        }  
	      
	        /** 
	         * 从文件中加载私钥 
	         *  
	         * @param keyFileName 
	         *            私钥文件名 
	         * @return 是否成功 
	         * @throws Exception 
	         */  
	        public static PrivateKey loadPrivateKey(InputStream in) throws Exception  
	        {  
	            try  
	            {  
	                return loadPrivateKey(readKey(in));  
	            } catch (IOException e)  
	            {  
	                throw new Exception("私钥数据读取错误");  
	            } catch (NullPointerException e)  
	            {  
	                throw new Exception("私钥输入流为空");  
	            }  
	        }  
	      
	        /** 
	         * 读取密钥信息 
	         *  
	         * @param in 
	         * @return 
	         * @throws IOException 
	         */  
	        private static String readKey(InputStream in) throws IOException  
	        {  
	            BufferedReader br = new BufferedReader(new InputStreamReader(in));  
	            String readLine = null;  
	            StringBuilder sb = new StringBuilder();  
	            while ((readLine = br.readLine()) != null)  
	            {  
	                if (readLine.charAt(0) == '-')  
	                {  
	                    continue;  
	                } else  
	                {  
	                    sb.append(readLine);  
	                    sb.append('\r');  
	                }  
	            }  
	      
	            return sb.toString();  
	        }  
	      
	        /** 
	         * 打印公钥信息 
	         *  
	         * @param publicKey 
	         */  
	        public static void printPublicKeyInfo(PublicKey publicKey)  
	        {  
	            RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;  
	            System.out.println("----------RSAPublicKey----------");  
	            System.out.println("Modulus.length=" + rsaPublicKey.getModulus().bitLength());  
	            System.out.println("Modulus=" + rsaPublicKey.getModulus().toString());  
	            System.out.println("PublicExponent.length=" + rsaPublicKey.getPublicExponent().bitLength());  
	            System.out.println("PublicExponent=" + rsaPublicKey.getPublicExponent().toString());  
	        }  
	      
	        public static void printPrivateKeyInfo(PrivateKey privateKey)  
	        {  
	            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;  
	            System.out.println("----------RSAPrivateKey ----------");  
	            System.out.println("Modulus.length=" + rsaPrivateKey.getModulus().bitLength());  
	            System.out.println("Modulus=" + rsaPrivateKey.getModulus().toString());  
	            System.out.println("PrivateExponent.length=" + rsaPrivateKey.getPrivateExponent().bitLength());  
	            System.out.println("PrivatecExponent=" + rsaPrivateKey.getPrivateExponent().toString());  
	      
	        }   
	        private static char[] base64EncodeChars = new char[]  
	        	    { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',  
	        	            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',  
	        	            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',  
	        	            '6', '7', '8', '9', '+', '/' };  
	        	    private static byte[] base64DecodeChars = new byte[]  
	        	    { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  
	        	            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53,  
	        	            54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,  
	        	            12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29,  
	        	            30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1,  
	        	            -1, -1, -1 };  
	        	  
	        	    /** 
	        	     * 加密 
	        	     *  
	        	     * @param data 
	        	     * @return 
	        	     */  
	        	    public static String encode(byte[] data)  
	        	    {  
	        	        StringBuffer sb = new StringBuffer();  
	        	        int len = data.length;  
	        	        int i = 0;  
	        	        int b1, b2, b3;  
	        	        while (i < len)  
	        	        {  
	        	            b1 = data[i++] & 0xff;  
	        	            if (i == len)  
	        	            {  
	        	                sb.append(base64EncodeChars[b1 >>> 2]);  
	        	                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);  
	        	                sb.append("==");  
	        	                break;  
	        	            }  
	        	            b2 = data[i++] & 0xff;  
	        	            if (i == len)  
	        	            {  
	        	                sb.append(base64EncodeChars[b1 >>> 2]);  
	        	                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);  
	        	                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);  
	        	                sb.append("=");  
	        	                break;  
	        	            }  
	        	            b3 = data[i++] & 0xff;  
	        	            sb.append(base64EncodeChars[b1 >>> 2]);  
	        	            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);  
	        	            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);  
	        	            sb.append(base64EncodeChars[b3 & 0x3f]);  
	        	        }  
	        	        return sb.toString();  
	        	    }  
	        	  
	        	    /** 
	        	     * 解密 
	        	     *  
	        	     * @param str 
	        	     * @return 
	        	     */  
	        	    public static byte[] decode(String str)  
	        	    {  
	        	        try  
	        	        {  
	        	            return decodePrivate(str);  
	        	        } catch (UnsupportedEncodingException e)  
	        	        {  
	        	            e.printStackTrace();  
	        	        }  
	        	        return new byte[]  
	        	        {};  
	        	    }  
	        	  
	        	    private static byte[] decodePrivate(String str) throws UnsupportedEncodingException  
	        	    {  
	        	        StringBuffer sb = new StringBuffer();  
	        	        byte[] data = null;  
	        	        data = str.getBytes("US-ASCII");  
	        	        int len = data.length;  
	        	        int i = 0;  
	        	        int b1, b2, b3, b4;  
	        	        while (i < len)  
	        	        {  
	        	  
	        	            do  
	        	            {  
	        	                b1 = base64DecodeChars[data[i++]];  
	        	            } while (i < len && b1 == -1);  
	        	            if (b1 == -1)  
	        	                break;  
	        	  
	        	            do  
	        	            {  
	        	                b2 = base64DecodeChars[data[i++]];  
	        	            } while (i < len && b2 == -1);  
	        	            if (b2 == -1)  
	        	                break;  
	        	            sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));  
	        	  
	        	            do  
	        	            {  
	        	                b3 = data[i++];  
	        	                if (b3 == 61)  
	        	                    return sb.toString().getBytes("iso8859-1");  
	        	                b3 = base64DecodeChars[b3];  
	        	            } while (i < len && b3 == -1);  
	        	            if (b3 == -1)  
	        	                break;  
	        	            sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));  
	        	  
	        	            do  
	        	            {  
	        	                b4 = data[i++];  
	        	                if (b4 == 61)  
	        	                    return sb.toString().getBytes("iso8859-1");  
	        	                b4 = base64DecodeChars[b4];  
	        	            } while (i < len && b4 == -1);  
	        	            if (b4 == -1)  
	        	                break;  
	        	            sb.append((char) (((b3 & 0x03) << 6) | b4));  
	        	        }  
	        	        return sb.toString().getBytes("iso8859-1");  
	        	    }  
}
