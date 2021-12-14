package com.example.restservice.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil implements Serializable {
	private static final long serialVersionUID = -2550185165626007488L;
	
	public static final long JWT_TOKEN_VALIDITY_IN_SECONDS = 5*60*60;  // 5 hours
	public static final long JWT_TOKEN_REFRESH_IN_SECONDS = 2*60*60;  // 2 hours

	private String secret;
	private long tokenValidityInSeconds = -1;
	private long refreshExpirationDateInSeconds = -1;

	@Value("${jwt.secret}")
	public void setSecret(String secret) {
		this.secret = secret;
	}

	//@Value("#{T(Long).parseLong('${jwt.tokenValidityInSeconds}')}")
	@Value("${jwt.tokenValidityInSeconds}")
	public void setJwtExpirationInSeconds(long tokenValidityInSeconds) {
		this.tokenValidityInSeconds = tokenValidityInSeconds;
	}

	@Value("${jwt.refreshExpirationDateInSeconds}")
	public void setRefreshExpirationDateInSeconds(long refreshExpirationDateInSeconds) {
		this.refreshExpirationDateInSeconds = refreshExpirationDateInSeconds;
	}

	private long getTokenValidityInSeconds() {
		if (this.tokenValidityInSeconds == -1) {
			return JWT_TOKEN_VALIDITY_IN_SECONDS;
		}
		return this.tokenValidityInSeconds;
	}

	private long getTokenRefreshExpirationDateInSeconds() {
		if (this.refreshExpirationDateInSeconds == -1) {
			return JWT_TOKEN_REFRESH_IN_SECONDS;
		}
		return this.refreshExpirationDateInSeconds;
	}


	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {

		// return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
		// 		.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_IN_SECONDS*1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + this.getTokenValidityInSeconds()*1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + this.getTokenRefreshExpirationDateInSeconds()*1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();

	}

	// public Boolean canTokenBeRefreshed(String token) {
	// 	return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	// }

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}    
}
