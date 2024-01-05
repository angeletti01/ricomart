/**
 * Author: Rico-Kali Hayes
 * Date: 01/03/2024
 * Description: AES256 KeyGenerator, Encryption, and Decryption program 
 * made to be used for Server-Side (Backend) validation of user login 
 * credentials and Client-Side (Frontend) entity creation. 
 * 
 * */

package com.ricomart.cryptography;

//import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
//import java.security.Key;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.util.Base64;
//import java.util.Random;
//import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.util.encoders.Hex;

import com.ricomart.entity.Customer;

public class AES256 
{
	private static final String ALGORITHM = "AES";
	private static final String ALGORITHM_WITH_PADDING = "AES/CBC/PKCS5Padding";
		
	private String sk;
	private String encryptedString;
	private String decryptedString;
	private byte[] aKey;
	
	
	private static final Logger log = LogManager.getFormatterLogger(AES256.class);		
	
	/*
	public byte[] establishKeys() throws NoSuchAlgorithmException
	{	
		System.out.println("Generating KeyPairs....");
		
		KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
		
		int keysize = 256;
		keyGenerator.init(keysize);	
		
		byte[] key = keyGenerator.generateKey().getEncoded();
		this.setaKey(key);	
		log.info("Secret Key: ");
		log.info(aKey);
		//System.out.println("Secret Key: " + aKey);
		log.info("Key Length: ");
		log.info(aKey.length);
		//System.out.println("Key Length: " + aKey.length);
		log.info("Key Size: ");
		log.info(keysize);
		//System.out.println("Key Size: " + keysize);	
		log.info("Key Algorithm: ");
		log.info(keyGenerator.getAlgorithm());
		//System.out.println("Key Algorithm: " + keyGenerator.getAlgorithm());
		
		return key;		
	}*/
		
	public String encrypt(String strToEncrypt) throws NoSuchAlgorithmException
	{	
		Customer customer = new Customer();
		log.info("Running encrypt() method...");
		// log.info("String to Encrypt: " + strToEncrypt);
		log.info("Generating KeyPairs....");
		
		KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
		
		int keysize = 256;
		keyGenerator.init(keysize);	
		
		byte[] key = keyGenerator.generateKey().getEncoded();
		this.setaKey(key);	
		
		log.info("Secret Key: ");
		log.info(aKey);
		//System.out.println("Secret Key: " + aKey);
		log.info("Key Length: ");
		log.info(aKey.length);
		//System.out.println("Key Length: " + aKey.length);
		log.info("Key Size: ");
		log.info(keysize);
		//System.out.println("Key Size: " + keysize);	
		log.info("Key Algorithm: ");
		log.info(keyGenerator.getAlgorithm());
		//System.out.println("Key Algorithm: " + keyGenerator.getAlgorithm());
		
		try 
		{
		//Scanner s = new Scanner(System.in);
		// String Input as User Input, can be converted to a different form of data input
		//System.out.println("Enter text to Encrypt: ");
		//strToEncrypt = s.nextLine();		// used only for stand alone program with manual entry
		
		// Encrypting the converted data with AES
		SecretKey secretKeySpec = new SecretKeySpec(this.getaKey(), ALGORITHM);
		
		Cipher cipher = Cipher.getInstance(ALGORITHM_WITH_PADDING, new BouncyCastleProvider());
		
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(new byte[16]));		
		
		String encryptedText = null;
		
		log.info("Encrypting Text....");		
		
		encryptedText = new String(Hex.encode(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)))).toUpperCase();
		
		// OPTIONAL - If there will be different types of encoded text. Define encodingType if switch statement will be used.  				
		/*
		 * String encodingType = null;
		 * 
		switch(encodingType) 
		{
		case "HEX_ENCODE":
			encryptedText = new String(Hex.encode(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)))).toUpperCase();
			break;
		case "BASE_64":
			encryptedText = Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
			break;
		default:
            encryptedText = new String(Hex.encode(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)))).toUpperCase();
            break; 
		} 
		*/
        
		log.info("Encryption is Complete!");
		log.info("Encrypted Text: ");
		log.info(encryptedText);
		
		log.info(key);
		
		this.setEncryptedString(encryptedText);
		customer.setPassword(encryptedText);
		customer.setaKey(key);
		return encryptedText;
		} 
		
		catch(InvalidKeyException i) 
		{
			log.error("Error while encrypting: InvalidKeyException", i);			
		}
		
		catch(NoSuchPaddingException nsp) 
		{
			log.error("Error while encrypting: NoSuchPaddingException", nsp);			
		}
		
		catch(NoSuchAlgorithmException nsa) 
		{
			log.error("Error while encrypting: NoSuchAlgorithmException", nsa);			
		} 
		
		catch (InvalidAlgorithmParameterException iap) {
			log.error("Error while encrypting: InvalidAlgorithmParameterException", iap);			
		} 
		
		catch (IllegalBlockSizeException ibs) {
			log.error("Error while encrypting: IllegalBlockSizeException", ibs);			
		} 
		
		catch (BadPaddingException bpe) {
			log.error("Error while encrypting: BadPaddingException", bpe);			
		}
		
		log.info("String did not Encrypt do to Error....");
		
		return null;
	}
	
	public String decrypt(String strToDecrypt, byte[] key) // pull both from Database
	{	
		log.info("Running decrypt() method...");
		
		try 
		{		
		// Text to Decrypt being passed to new variable		
		log.info("Encrypted Text being passed for Decryption....");
		//strToDecrypt = this.getEncryptedString();		// this is for running the program without database
		
		// Encrypting the converted data with AES
		SecretKey secretKeySpec = new SecretKeySpec(/*this.getaKey()*/key, ALGORITHM); // getaKey is for running program without database
		
		Cipher cipher = Cipher.getInstance(ALGORITHM_WITH_PADDING, new BouncyCastleProvider());
		
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(new byte[16]));		// <-- Key length not 128/192/256 bits		
		
		log.info("Decrypting the Encrypted Text....");		
		
		String decryptedText = new String(cipher.doFinal(Hex.decode(strToDecrypt)));
		
		// OPTIONAL - If there will be different types of encoded text. Define encodingType if switch statement will be used.  				
		/*
		 * String encodingType = null;
		 * 
		switch(encodingType) 
		{
		case "HEX_ENCODE":
			decryptedText = new String(Hex.decode(cipher.doFinal(strToDecrypt.getBytes(StandardCharsets.UTF_8)))).toUpperCase();
			break;
		case "BASE_64":
			decryptedText = Base64.getEncoder().decodeToString(cipher.doFinal(strToDecrypt.getBytes(StandardCharsets.UTF_8)));
			break;
		default:
            decryptedText = new String(Hex.decode(cipher.doFinal(strToDecrypt.getBytes(StandardCharsets.UTF_8)))).toUpperCase();
            break; 
		} 
		*/
        
		log.info("Decryption is Complete!");
		log.info("Decrypted Text: ");
		log.info(decryptedText);
		this.setDecryptedString(decryptedText);
		//System.out.println("Decrypted Text: " + this.getDecryptedString());
		
		return decryptedText;
		} 
		
		catch(InvalidKeyException i) 
		{
			log.error("Error while decrypting: InvalidKeyException", i);			
		}
		
		catch(NoSuchPaddingException nsp) 
		{
			log.error("Error while decrypting: NoSuchPaddingException", nsp);			
		}
		
		catch(NoSuchAlgorithmException nsa) 
		{
			log.error("Error while encrypting: NoSuchAlgorithmException", nsa);			
		} 
		
		catch (InvalidAlgorithmParameterException iap) {
			log.error("Error while encrypting: InvalidAlgorithmParameterException", iap);			
		} 
		
		catch (IllegalBlockSizeException ibs) {
			log.error("Error while encrypting: IllegalBlockSizeException", ibs);			
		} 
		
		catch (BadPaddingException bpe) {
			log.error("Error while encrypting: BadPaddingException", bpe);			
		}	
		log.info("String did not Decrypt do to Error....");
		
		return null;
	}
	
	public String getSk() {
		return sk;
	}

	public void setSk(String sk) {
		this.sk = sk;
	}

	public byte[] getaKey() {
		return aKey;
	}

	public void setaKey(byte[] aKey) {
		this.aKey = aKey;
	}

	public String getEncryptedString() {
		return encryptedString;
	}

	public void setEncryptedString(String encryptedString) {
		this.encryptedString = encryptedString;
	}

	public String getDecryptedString() {
		return decryptedString;
	}

	public void setDecryptedString(String decryptedString) {
		this.decryptedString = decryptedString;
	}	
	
}



