package com.coinnote.entryservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
public class MobilityEntity {
    private String id;
    private String title;
    private Long accountDelta;
    public Double partSpending;
    public Double serviceSpending;
    public Double petrolSpending;
}
