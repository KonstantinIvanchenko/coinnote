package com.coinnote.entryservice;

import com.coinnote.entryservice.dto.AuthenticationDto;
import com.coinnote.entryservice.dto.LoginDto;
import com.coinnote.entryservice.dto.UserDto;
import com.coinnote.entryservice.service.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDto userDto){
        if (authService.isUserPresent(userDto))
            return new ResponseEntity<String>("User Already Exists", HttpStatus.OK);

        authService.signup(userDto);
        return new ResponseEntity<String>("User Registered Successfully", HttpStatus.OK);
    }
}
