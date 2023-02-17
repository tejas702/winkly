package com.winkly.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Problems {

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(max = 255)
    @Column(name = "problem_1")
    private String problem_1;

    @NotBlank
    @Size(max = 255)
    @Column(name = "problem_2")
    private String problem_2;

    @NotBlank
    @Size(max = 255)
    @Column(name = "problem_3")
    private String problem_3;

    @NotBlank
    @Size(max = 255)
    @Column(name = "problem_4")
    private String problem_4;

}
