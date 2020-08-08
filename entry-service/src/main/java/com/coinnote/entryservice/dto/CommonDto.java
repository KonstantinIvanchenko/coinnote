package com.coinnote.entryservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

//import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class CommonDto {

    //private Long id;
    private String id;
    private String title;

    @JsonIgnore
    private final LocalDateTime editedAt = LocalDateTime.now();
    @JsonIgnore
    private final LocalDateTime createdAt = LocalDateTime.now();

    private Long accountDelta;

    public int hashcode(){
        return Objects.hash(title, createdAt);
    }

    public void setAccountDeltaZero(){
        this.accountDelta=0L;
    }
    public void setAccountDelta(Long accountDelta){
        this.accountDelta=accountDelta;
    }
}
