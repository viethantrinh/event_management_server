package com.trvihnls.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.trvihnls.domains.InvalidatedToken;
import com.trvihnls.domains.Role;
import com.trvihnls.domains.User;
import com.trvihnls.dtos.auth.*;
import com.trvihnls.enums.ErrorCodeEnum;
import com.trvihnls.enums.RoleEnum;
import com.trvihnls.exceptions.AppException;
import com.trvihnls.repositories.InvalidatedTokenRepository;
import com.trvihnls.repositories.RoleRepository;
import com.trvihnls.repositories.UserRepository;
import com.trvihnls.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

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
     * Handles the user registration process.
     *
     * @param request The sign-up request containing user details such as email, password, phone number, etc.
     * @return A {@link SignUpResponse} containing the generated JWT token and user details.
     * @throws AppException if the user already exists or if there is an error generating the JWT token.
     */
    public SignUpResponse signUp(SignUpRequest request) {

        boolean isExisted = userRepository.existsByEmail(request.getEmail());

        if (isExisted) throw new AppException(ErrorCodeEnum.REGISTERED_FAILED_USER_EXISTED);

        Role userRole = roleRepository.findByName(RoleEnum.USER.getName())
                .orElseThrow(() -> new AppException(ErrorCodeEnum.REGISTERED_FAILED_ROLE_NOT_EXISTED));

        User registerUser = User.builder()
                .id(UUID.randomUUID().toString())
                .email(request.getEmail())
                .workEmail(request.getWorkEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .fullName(request.getFullName())
                .academicRank(request.getAcademicRank())
                .academicDegree(request.getAcademicDegree())
                .roles(Set.of(userRole))
                .build();

        User savedUser = userRepository.save(registerUser);

        // generate the jwt token
        String jwtToken = null;

        try {
            jwtToken = generateToken(savedUser);
        } catch (JOSEException e) {
            throw new AppException(ErrorCodeEnum.UNAUTHENTICATED_GENERATE_TOKEN_ERROR);
        }

        return SignUpResponse.builder()
                .token(jwtToken)
                .email(savedUser.getEmail())
                .workEmail(savedUser.getWorkEmail())
                .phoneNumber(savedUser.getPhoneNumber())
                .fullName(savedUser.getFullName())
                .academicRank(savedUser.getAcademicRank())
                .academicDegree(savedUser.getAcademicDegree())
                .build();
    }

    /**
     * Invalidates a JWT token by saving it to the database as an invalidated token.
     * This method is typically used during the sign-out process to ensure the token
     * can no longer be used for authentication.
     *
     * @param token The JWT token to be invalidated.
     */
    public void signOut(String token) {
        try {
            invalidateToken(token);
        } catch (ParseException e) {
            throw new AppException(ErrorCodeEnum.GENERAL_ERROR);
        }
    }

    /**
     * Verifies the validity of a JWT token provided in the request.
     * This method delegates the actual verification to the private `verifyToken` method
     * and handles any exceptions that may occur during the process.
     *
     * @param request The request containing the JWT token to be verified.
     * @return An {@link IntrospectTokenResponse} indicating whether the token is valid.
     */
    public IntrospectTokenResponse verifyToken(IntrospectTokenRequest request) {
        String token = request.getToken();
        boolean valid = false;

        try {
            valid = verifyToken(token);
        } catch (JOSEException | ParseException e) {
            valid = false;
        }

        return IntrospectTokenResponse.builder()
                .valid(valid)
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
        long epochTime = Instant.now().plus(360000000, ChronoUnit.SECONDS).toEpochMilli();
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
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // build and sign the token
        SignedJWT signedJWT = new SignedJWT(header, jwtClaimsSet);
        JWSSigner jwsSigner = new MACSigner(secretKey.getBytes());
        signedJWT.sign(jwsSigner);

        String serializedToken = signedJWT.serialize();

        return serializedToken;
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

    /**
     * invalidate the token - use for sign out
     * @param token the token to be validated
     * @throws ParseException
     */
    private void invalidateToken(String token) throws ParseException {
        SignedJWT parsedToken = SignedJWT.parse(token);

        JWTClaimsSet claimsSet = parsedToken.getJWTClaimsSet();

        String jwtID = claimsSet.getJWTID();
        Date expirationTime = claimsSet.getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .jwtId(jwtID)
                .expirationTime(expirationTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }
}
