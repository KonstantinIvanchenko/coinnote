package com.coinnote.entryservice.enitity;

//import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.persistence.Id;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

@Data
@Document
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class CommonInstance {
    @Id
    //@NotNull
    //private Long id;
    private String id;
    //@NotNull
    private String title;

    //@NotNull
    private User user;
    private Long totalSpending;

    //@NotNull
    private LocalDateTime createdAt;
    //@NotNull
    private LocalDateTime editedAt;
}
