package com.amsidh.mvc.controller.response;

import com.amsidh.mvc.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonEvent {
    private Person person;
    private Date date;
}
