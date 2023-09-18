package com.russozaripov.springsecurityjwttoken.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class USerDTO {
    private Long id;
    private String email;
    private String username;
}
