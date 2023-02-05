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
    },
    uniqueConstraints = {
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

  @Column(name = "fb_link")
  @Nullable
  private String fbLink;

  @Column(name = "twitter_link")
  @Nullable
  private String twitterLink;

  @Column(name = "insta_link")
  @Nullable
  private String instaLink;

  @Column(name = "snapchat_link")
  @Nullable
  private String snapchatLink;

  @Column(name = "linkedin_link")
  @Nullable
  private String linkedinLink;

  @Column(name = "linktree_link")
  @Nullable
  private String linktreeLink;

  public UserEntity(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public UserEntity(String email) {
    this.email = email;
  }

}
