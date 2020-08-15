package com.coinnote.entryservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String userName;
    private String password;
    //TODO:enable email verification
    private String email;

    public int hashcode(){
        return Objects.hash(userName, email);
    }
}
