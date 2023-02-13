package com.winkly.entity;

import com.winkly.dto.LikeDto;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Table(
    name = "user_table",
    indexes = {
      @Index(name = "table_symbol", columnList = "email", unique = true),
      @Index(name = "table_username", columnList = "username", unique = true),
    },
    uniqueConstraints = {
      @UniqueConstraint(columnNames = "email"),
      @UniqueConstraint(columnNames = "username")
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
  @Column(name = "username")
  private String username;

  @NotBlank
  @Size(max = 200)
  @Column(name = "name")
  private String name;

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

  @Column(name = "verified_status")
  private Boolean verifiedStatus = false;

  @Column(name = "liked_you")
  @ElementCollection
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<String> likedYou;

  @Column(name = "you_liked")
  @ElementCollection
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<String> youLiked;

  @Column(name = "matched")
  @ElementCollection
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<String> matched;

  public UserEntity(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public UserEntity(String email) {
    this.email = email;
  }

}
