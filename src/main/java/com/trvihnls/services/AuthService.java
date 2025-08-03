package com.trvihnls.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.trvihnls.domains.User;
import com.trvihnls.dtos.auth.SignInRequest;
import com.trvihnls.dtos.auth.SignInResponse;
import com.trvihnls.enums.ErrorCodeEnum;
import com.trvihnls.exceptions.AppException;
import com.trvihnls.repositories.UserRepository;
import com.trvihnls.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${security.secret-key}")
    private String secretKey;

    /**
     * Authenticates a user by validating their email and password.
     *
     * @param request The sign-in request containing the user's email and password.
     * @return A {@link SignInResponse} containing the generated JWT token.
     * @throws AppException if the user is not found or the password does not match.
     */
    public SignInResponse signIn(SignInRequest request) {
        String email = request.getEmail();
        String rawPassword = request.getPassword();

        // Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.UNAUTHENTICATED_USER_NOT_FOUND));

        // Check if the password is matched or not
        String encryptedPassword = user.getPassword();
        if (!passwordEncoder.matches(rawPassword, encryptedPassword)) {
            throw new AppException(ErrorCodeEnum.UNAUTHENTICATED_PASSWORD_NOT_MATCH);
        }

        // generate the jwt token
        String jwtToken = null;

        try {
            jwtToken = generateToken(user);
        } catch (JOSEException e) {
            throw new AppException(ErrorCodeEnum.UNAUTHENTICATED_GENERATE_TOKEN_ERROR);
        }

        return SignInResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Generate a token from user's information
     * @param user the user whose data use to generate the token
     * @return serialized token in String format
     */
    private String generateToken(User user) throws JOSEException {
        // jwt token info
        String issuer = AppConstant.ISSUER;
        String subject = user.getId();
        String scope = user.getRoles().stream()
                .map((role) -> role.getName())
                .collect(Collectors.joining(" "));
        Date issuedDate = new Date();
        long epochTime = Instant.now().plus(3600, ChronoUnit.SECONDS).toEpochMilli();
        Date expirationDate = new Date(epochTime);
        String jwtId = UUID.randomUUID().toString();

        // build the jwt claim set
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer(issuer)
                .claim("scope", scope)
                .issueTime(issuedDate)
                .expirationTime(expirationDate)
                .jwtID(jwtId)
                .build();

        // build the head of jwt token
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        // build and sign the token
        SignedJWT signedJWT = new SignedJWT(header, jwtClaimsSet);
        JWSSigner jwsSigner = new MACSigner(secretKey.getBytes());
        signedJWT.sign(jwsSigner);

        String serializedToken = signedJWT.serialize();

        return serializedToken;
    }
}
