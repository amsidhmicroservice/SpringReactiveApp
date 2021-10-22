package com.amsidh.mvc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private Integer _id;
    private String name;
    private String city;
    private Long pinCode;
    private Long mobileNumber;
    private String emailId;
}
