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
public class Links {

    @NotBlank
    @Size(max = 50)
    @Column(name = "link_name")
    private String linkName;

    @NotBlank
    @Size(max = 255)
    @Column(name = "url")
    private String url;

}
