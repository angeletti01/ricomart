package com.ricomart.config;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.SignatureException;

@SuppressWarnings("deprecation")
@Service(value = "JwtService")
public class JwtService {
	
	private static final Logger log = LogManager.getFormatterLogger(JwtService.class);
	private static String SECRET_KEY = "";
	private static final String ALGORITHM = "AES";
	private static final String ALGORITHM_WITH_PADDING = "AES/CBC/PKCS5Padding";
	private byte[] aKey;
	
	public String extractUsername(String token) throws SignatureException, 
	ExpiredJwtException, 
	UnsupportedJwtException, 
	MalformedJwtException, 
	SecurityException, 
	IllegalArgumentException, 
	NoSuchAlgorithmException {
			
		return extractClaim(token,Claims::getSubject);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws SignatureException,
	ExpiredJwtException, 
	UnsupportedJwtException, 
	MalformedJwtException, 
	SecurityException,
	IllegalArgumentException, 
	NoSuchAlgorithmException {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) throws SignatureException, 
	ExpiredJwtException, 
	UnsupportedJwtException, 
	MalformedJwtException, 
	SecurityException, 
	IllegalArgumentException, 
	NoSuchAlgorithmException {
		final String username = extractUsername(token);
		
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}
	
	private boolean isTokenExpired(String token) throws SignatureException, 
	ExpiredJwtException, UnsupportedJwtException,
	MalformedJwtException, 
	SecurityException, 
	IllegalArgumentException, 
	NoSuchAlgorithmException {
		return extractExpiration(token).before(new Date());
	}
	
	private Date extractExpiration(String token) throws SignatureException, 
	ExpiredJwtException, 
	UnsupportedJwtException, 
	MalformedJwtException, 
	SecurityException, 
	IllegalArgumentException, 
	NoSuchAlgorithmException {
		return extractClaim(token, Claims::getExpiration);
	}
	
	@SuppressWarnings("deprecation")
	private Claims extractAllClaims(String token) throws SignatureException, 
	ExpiredJwtException, 
	UnsupportedJwtException, 
	MalformedJwtException, 
	SecurityException, 
	IllegalArgumentException, 
	NoSuchAlgorithmException {
		
		generateSecretKey();
		
		return Jwts
				.parser()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	// Generating User token without claims
	public String generateToekn(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}
	
	
	// Generating User token from claims
	@SuppressWarnings("deprecation")
	public String generateToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails
			) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	private byte[] generateSecretKey() throws NoSuchAlgorithmException
	{	
		System.out.println("Generating Secret Key....");
		
		KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);		
		
		int keysize = 256;
		keyGenerator.init(keysize);	
		
		byte[] key = keyGenerator.generateKey().getEncoded();
		this.setaKey(key);	
		log.info("Secret Key: ");
		log.info(aKey);		
		log.info("Key Length: ");
		log.info(aKey.length);		
		log.info("Key Size: ");
		log.info(keysize);			
		log.info("Key Algorithm: ");
		log.info(keyGenerator.getAlgorithm());		
		
		String convertByteArray = new String(key, StandardCharsets.UTF_8); // converts byte array to a String				
		
		StringBuffer sb = new StringBuffer();
		
	      //Converting string to character array
	      char ch[] = convertByteArray.toCharArray();
	      for(int i = 0; i < ch.length; i++) {
	         String hexString = Integer.toHexString(ch[i]);
	         sb.append(hexString);
	      }
	      SECRET_KEY = sb.toString();
	      
	     log.info("Secret Key: "+SECRET_KEY);		
		
		return key;		
	}

	public byte[] getaKey() {
		return aKey;
	}

	public void setaKey(byte[] aKey) {
		this.aKey = aKey;
	}

	
	
	
}
