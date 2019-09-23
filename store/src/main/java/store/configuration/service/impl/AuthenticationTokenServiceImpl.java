package store.configuration.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import store.configuration.service.AuthenticationTokenService;
@Service
public class AuthenticationTokenServiceImpl implements AuthenticationTokenService{
	private final String ENCODE_KEY = "abc123";
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public String generateToken(String email) {
		User user = (User) userDetailsService.loadUserByUsername(email);
		Map<String, Object> tokenData = new HashMap<>();
		tokenData.put("USERNAME", user.getUsername());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, 1);
		tokenData.put("TOKEN_EXPIRATION_DATE", calendar.getTime());
		JwtBuilder jwtBuilder = Jwts.builder();
		jwtBuilder.setExpiration(calendar.getTime());
		jwtBuilder.setClaims(tokenData);
		String token = jwtBuilder.signWith(SignatureAlgorithm.HS512, ENCODE_KEY).compact();
		return token;
	}

	@Override
	public User getUserFromToken(String token) throws AuthenticationException {
		DefaultClaims claims = (DefaultClaims) Jwts.parser().setSigningKey(ENCODE_KEY).parse(token).getBody();
		if (claims.get("TOKEN_EXPIRATION_DATE", Long.class) == null)
			throw new AuthenticationServiceException("Invalid token");
		Date expiredDate = new Date(claims.get("TOKEN_EXPIRATION_DATE", Long.class));
		if (expiredDate.after(new Date())) {
			return (User) userDetailsService.loadUserByUsername(claims.get("USERNAME", String.class));
		} else
			throw new AuthenticationServiceException("Token expired error");
	}
}
