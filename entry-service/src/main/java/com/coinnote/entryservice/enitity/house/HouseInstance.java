package com.coinnote.entryservice.enitity.house;

import com.coinnote.entryservice.enitity.CommonInstance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@SuperBuilder
@AllArgsConstructor
public class HouseInstance extends CommonInstance {
}
