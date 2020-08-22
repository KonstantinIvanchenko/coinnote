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
@RequestMapping("/usercontext")
@AllArgsConstructor
public class UserContextController {

    private final AuthService authService;

    @GetMapping("/token")
    public ResponseEntity<String> getUserToken(){
        return new ResponseEntity<>(authService.getUserToken(), HttpStatus.OK);
    }
}
