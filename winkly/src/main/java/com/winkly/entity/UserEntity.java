package com.winkly.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Table(
    name = "user_table",
    indexes = {
      @Index(name = "table_symbol", columnList = "email", unique = true),
      @Index(name = "table_gsin", columnList = "userName", unique = true)
    },
    uniqueConstraints = {
      @UniqueConstraint(columnNames = "userName"),
      @UniqueConstraint(columnNames = "email")
    })
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity implements Serializable {

  private static final long serialVersionUID = -645834567897L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotBlank
  @Size(max = 20)
  @Column(name = "userName")
  private String userName;

  @NotBlank
  @Size(max = 50)
  @Email
  @Column(name = "email")
  private String email;

  @NotBlank
  @Size(max = 120)
  @Column(name = "password")
  private String password;

  @Column(name = "resetToken")
  @Nullable
  private String resetToken;

  @Column(name = "tokenCreationTime")
  @Nullable
  private LocalDateTime tokenCreationTime;

  public UserEntity(String username, String email, String password) {
    this.userName = username;
    this.email = email;
    this.password = password;
  }

  public UserEntity(String email) {
    this.email = email;
  }

}
