package ma.micda.journal.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.micda.journal.constants.Mappings;
import ma.micda.journal.dto.UserDTO;
import ma.micda.journal.models.User;
import ma.micda.journal.payload.request.LoginRequest;
import ma.micda.journal.payload.request.SignupRequest;
import ma.micda.journal.payload.response.JwtResponse;
import ma.micda.journal.security.config.UserDetailsImpl;
import ma.micda.journal.security.jwt.JwtUtils;
import ma.micda.journal.services.AuthService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = Mappings.REQUEST_MAPPING_AUTH)
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService,
            JwtUtils jwtUtils, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = Mappings.LOGIN)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping(value = Mappings.REGISTER)

    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signUpRequest) {

        return ResponseEntity.ok(getUserEntity(this.authService.registerUser(signUpRequest)));
    }

    private UserDTO getUserEntity(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
