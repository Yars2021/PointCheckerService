package ru.itmo.p3214.s312198.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.p3214.s312198.db.InternalUser;
import ru.itmo.p3214.s312198.db.UserRepository;
import ru.itmo.p3214.s312198.rest.reqest.LoginRequest;
import ru.itmo.p3214.s312198.rest.reqest.RegistrationRequest;
import ru.itmo.p3214.s312198.rest.response.LoginResponse;
import ru.itmo.p3214.s312198.rest.response.Status;
import ru.itmo.p3214.s312198.rest.response.UserResponse;
import ru.itmo.p3214.s312198.security.HashPasswordEncoder;
import ru.itmo.p3214.s312198.security.TokenUtils;

import java.security.AuthProvider;
import java.sql.Timestamp;
import java.util.Date;

@CrossOrigin(origins = "*")
@RestController
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthProvider.class);


    private final UserRepository userRepository;
    private final HashPasswordEncoder hashPasswordEncoder;
    private final TokenUtils tokenUtils;

    public AuthController(UserRepository userRepository, HashPasswordEncoder hashPasswordEncoder, TokenUtils tokenUtils) {
        this.userRepository = userRepository;
        this.hashPasswordEncoder = hashPasswordEncoder;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping(path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        if (loginRequest == null) {
            loginResponse.setStatus(Status.FAIL);
            loginResponse.setDescription("No credentials provided. Check input.");
            logger.error("Empty credentials provided");
            return new ResponseEntity<>(loginResponse, HttpStatus.NO_CONTENT);
        } else {
            if (loginRequest.getLogin() == null) {
                loginResponse.setStatus(Status.FAIL);
                loginResponse.setDescription("No login provided. Check input.");
                logger.error("Empty login provided");
                return new ResponseEntity<>(loginResponse, HttpStatus.PARTIAL_CONTENT);
            } else {
                InternalUser user = userRepository.findByLogin(loginRequest.getLogin());
                if (user == null) {
                    loginResponse.setStatus(Status.FAIL);
                    loginResponse.setDescription("Incorrect user or password.");
                    logger.warn("Unknown user [" + loginRequest.getLogin() + "]");
                    return new ResponseEntity<>(loginResponse, HttpStatus.UNAUTHORIZED);
                } else {
                    user.setLogged(Boolean.FALSE);
                    if (!user.getPassword().equals(hashPasswordEncoder.encode(loginRequest.getPassword()))) {
                        loginResponse.setStatus(Status.FAIL);
                        loginResponse.setDescription("Incorrect user or password.");
                        logger.warn("Incorrect password provided for user [" + loginRequest.getLogin() + "]");
                        return new ResponseEntity<>(loginResponse, HttpStatus.UNAUTHORIZED);
                    } else {
                        loginResponse.setToken(tokenUtils.getToken());
                        user.setLogged(Boolean.TRUE);
                        user.setToken(loginResponse.getToken());
                        user.setUpdatedAt(new Timestamp(new Date().getTime()));
                        userRepository.save(user);
                        loginResponse.setStatus(Status.SUCCESS);
                        loginResponse.setDescription("User [" + loginRequest.getLogin() + "] has successfully logged in.");
                        logger.info("User [" + loginRequest.getLogin() + "] has successfully logged in.");
                        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
                    }
                }
            }
        }
    }

    @DeleteMapping(path = "/logout",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> logout(@RequestHeader(name = "Token", required = false) String token) {
        LoginResponse logoutResponse = new LoginResponse();
        if (token == null) {
            logoutResponse.setStatus(Status.FAIL);
            logoutResponse.setDescription("Impossible to logout. Check Input.");
            logger.error("No token provided.");
            return new ResponseEntity<>(logoutResponse, HttpStatus.NO_CONTENT);
        } else {
            InternalUser user = userRepository.findByToken(token);
            if (user == null) {
                logoutResponse.setStatus(Status.FAIL);
                logoutResponse.setDescription("Incorrect info for logout. Check Input.");
                logger.error("Unknown token provided.");
                return new ResponseEntity<>(logoutResponse, HttpStatus.NOT_FOUND);
            } else {
                user.setToken(null);
                user.setUpdatedAt(new Timestamp(new Date().getTime()));
                user.setLogged(Boolean.FALSE);
                userRepository.save(user);
                logoutResponse.setStatus(Status.SUCCESS);
                logoutResponse.setDescription("Logged out");
                logger.info("User [" + user.getLogin() + "] has successfully logged out.");
                return new ResponseEntity<>(logoutResponse, HttpStatus.OK);
            }
        }
    }

    @PostMapping(path = "/registration",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        UserResponse userResponse = new UserResponse();
        if (registrationRequest == null) {
            userResponse.setUser(null);
            userResponse.setStatus(Status.FAIL);
            userResponse.setDescription("Registration data is invalid. Check inputs.");
            logger.error("Empty registration request");
            return new ResponseEntity<>(userResponse, HttpStatus.NO_CONTENT);
        } else {
            String localUser = StringUtils.hasLength(registrationRequest.getLogin()) ? registrationRequest.getLogin().trim() : "";
            String localPassword1 = StringUtils.hasLength(registrationRequest.getPassword()) ? registrationRequest.getPassword().trim() : "";
            String localPassword2 = StringUtils.hasLength(registrationRequest.getPasswordRepeat()) ? registrationRequest.getPasswordRepeat().trim() : "";
            if (localUser.length() > 0
                    && localPassword1.length() > 0
                    && localPassword2.length() > 0) {
                InternalUser existingUser = userRepository.findByLogin(localUser);
                if (existingUser != null) {
                    userResponse.setStatus(Status.FAIL);
                    userResponse.setDescription("User [" + localUser + "] already exists");
                    logger.error("User [" + localUser + "] already exists");
                    return new ResponseEntity<>(userResponse, HttpStatus.CONFLICT);
                } else {
                    if (!localPassword1.equals(localPassword2)) {
                        userResponse.setStatus(Status.FAIL);
                        userResponse.setDescription("Passwords are not equal");
                        logger.error("User [" + localUser + "] registration: Passwords are not equal");
                        return new ResponseEntity<>(userResponse, HttpStatus.PARTIAL_CONTENT);
                    } else {
                        InternalUser newUser = new InternalUser(localUser,
                                hashPasswordEncoder.encode(localPassword1),
                                Boolean.TRUE,
                                "USER",
                                Boolean.TRUE,
                                tokenUtils.getToken(),
                                new Timestamp(new Date().getTime()));
                        userRepository.save(newUser);
                        newUser.setPassword("SECURED");
                        userResponse.setUser(newUser);
                        userResponse.setStatus(Status.SUCCESS);
                        logger.info("New user [" + newUser.getLogin() + "] has been successfully registered");
                        userResponse.setDescription("New user [" + newUser.getLogin() + "] has been successfully registered");
                        return new ResponseEntity<>(userResponse, HttpStatus.OK);
                    }
                }
            } else {
                userResponse.setStatus(Status.FAIL);
                userResponse.setDescription("Incorrect input");
                logger.error("Not enough data provided for registration. Username or at least one of passwords is empty");
                return new ResponseEntity<>(userResponse, HttpStatus.NO_CONTENT);
            }
        }
    }
}
