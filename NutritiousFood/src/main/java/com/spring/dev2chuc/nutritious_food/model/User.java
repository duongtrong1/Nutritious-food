package com.spring.dev2chuc.nutritious_food.model;

import com.spring.dev2chuc.nutritious_food.model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 6, max = 40)
    private String name;

    @NotNull
    @Size(min = 6, max = 15)
    private String username;

    @NaturalId
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Length(max = 100)
    private String password;

    @NotNull
    @Size(max = 20)
    private String phone;

    private int status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<UserProfile> userProfiles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Address> addresses;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<RattingFood> rattingFoods;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<RattingSchedule> rattingSchedules;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<RattingCombo> rattingCombos;

    public User() {
        this.status = Status.ACTIVE.getValue();
    }

    public User(String name, String username, String email, String password, String phone) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.status = Status.ACTIVE.getValue();
    }
}