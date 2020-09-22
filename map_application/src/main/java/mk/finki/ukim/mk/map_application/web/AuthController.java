package mk.finki.ukim.mk.map_application.web;

import mk.finki.ukim.mk.map_application.model.Auth.Role;
import mk.finki.ukim.mk.map_application.model.Auth.RoleName;
import mk.finki.ukim.mk.map_application.model.Auth.User;
import mk.finki.ukim.mk.map_application.model.Exception.Auth.AppException;
import mk.finki.ukim.mk.map_application.model.Exception.Auth.UserNotFoundException;
import mk.finki.ukim.mk.map_application.payload.Request.LoginRequest;
import mk.finki.ukim.mk.map_application.payload.Request.SignUpRequest;
import mk.finki.ukim.mk.map_application.payload.Response.ApiResponse;
import mk.finki.ukim.mk.map_application.payload.Response.JwtAuthenticationResponse;
import mk.finki.ukim.mk.map_application.repository.Auth.RoleRepository;
import mk.finki.ukim.mk.map_application.repository.Auth.UserRepository;
import mk.finki.ukim.mk.map_application.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    private static final Logger LOG = LoggerFactory.getLogger(MapsController.class.getName());


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User signedInUser = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(),loginRequest.getUsernameOrEmail()).orElseThrow(UserNotFoundException::new);

        LOG.info("METHOD NAME: authenticateUser - POST request , USER:"+signedInUser.getUsername()+" "+ new Date());

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt,signedInUser));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        System.out.println("AuthController/sign-up");
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        //Checking if registration data is valid
//        userRepository.findByUsername(signUpRequest.getUsername()).ifPresent(user -> {throw new UsernameAlreadyExists("User with username "+user.getUsername()+" already exists");});
//        userRepository.findByEmail(signUpRequest.getEmail()).ifPresent(user -> {throw new UserEmailAlreadyExists("User with email "+user.getEmail()+" already exists");});

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        //user.setRoles(Collections.singleton(userRole));

        Set<Role> role_user = new HashSet<>();
        role_user.add(userRole);

        user.setRoles(role_user);

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        LOG.info("METHOD NAME: registerUser - POST request , USER:"+user.getUsername()+" "+ new Date());

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}