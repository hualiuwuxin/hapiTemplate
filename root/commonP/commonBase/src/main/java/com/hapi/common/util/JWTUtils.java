package com.hapi.common.util;

import java.util.Date;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTUtils {

	private static final String JWTSECRET = "ZXlKaGJHY2lPaUpJVXpVeE1pSjk=";

	public static String createToken(String payload,Date expirationData) {
		JwtBuilder jwtBuilder = Jwts.builder().setSubject(payload);
		if( expirationData != null ) {
			jwtBuilder.setExpiration( expirationData );
		}
		
		jwtBuilder = jwtBuilder.signWith(SignatureAlgorithm.HS512, JWTSECRET);
		
		return jwtBuilder.compact();
	}

	public static String verifyToken( String token) {
		String payload = Jwts.parser().setSigningKey(JWTSECRET).parseClaimsJws(token).getBody().getSubject();
		return payload;
	}
	
	
	

}
