package com.coinnote.entryservice.entity;

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
    private String id;
    private String title;
    private String userName;
    private Long totalSpending;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
}
