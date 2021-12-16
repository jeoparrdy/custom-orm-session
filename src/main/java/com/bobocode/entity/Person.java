package com.bobocode.entity;

import com.bobocode.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table("persons")
@ToString
@Getter
@Setter
public class Person {
    private Long id;

    private String firstName;

    private String lastName;
}
