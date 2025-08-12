package com.trvihnls.configurations.securities;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.trvihnls.repositories.InvalidatedTokenRepository;
import com.trvihnls.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CustomJWTDecoder implements JwtDecoder {

    @Value("${security.secret-key}")
    private String secretKey;

    private final InvalidatedTokenRepository invalidatedTokenRepository;


    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            validateToken(token);
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
        return createJwtDecoder().decode(token);
    }

    private void validateToken(String token) throws ParseException, JOSEException {
        if (!verifyToken(token)) {
            throw  new InvalidBearerTokenException("token is invalid");
        }
    }

    private NimbusJwtDecoder createJwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), JWSAlgorithm.HS512.getName());
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }

    /**
     * Verifies the validity of a JWT token.
     * This method checks the token's signature, expiration time, and ensures it has not been invalidated.
     *
     * @param token The JWT token to be verified.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     * @throws JOSEException If there is an error during the token verification process.
     * @throws ParseException If the token cannot be parsed.
     */
    public boolean verifyToken(String token) throws JOSEException, ParseException {
        // Parse the JWT token
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier jwsVerifier = new MACVerifier(secretKey.getBytes());

        // Verify the token's signature
        if (!signedJWT.verify(jwsVerifier)) {
            return false;
        }

        // Retrieve the claims from the token
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        Date expTime = claims.getExpirationTime();

        // Check if the token has expired
        if (expTime == null || expTime.before(new Date())) {
            return false;
        }

        // Check if the token has been invalidated
        String jwtID = claims.getJWTID();
        return jwtID != null && !invalidatedTokenRepository.existsByJwtId(jwtID);
    }
}
