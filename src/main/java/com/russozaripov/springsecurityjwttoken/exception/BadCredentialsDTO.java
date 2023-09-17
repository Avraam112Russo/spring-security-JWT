package com.russozaripov.springsecurityjwttoken.exception;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
public class BadCredentialsDTO {
    private int status;
    private String message;
    private Date date;

    public BadCredentialsDTO(int status, String message) {
        this.status = status;
        this.message = message;
        this.date = new Date();
    }
}
