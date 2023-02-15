package com.winkly.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Likes {

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(max = 120)
    @Column(name = "username")
    private String username;

    @NotBlank
    @Size(max = 200)
    @Column(name = "name")
    private String name;

    @NotBlank
    @Size(max = 255)
    @Column(name = "reason")
    private String reason;

}
