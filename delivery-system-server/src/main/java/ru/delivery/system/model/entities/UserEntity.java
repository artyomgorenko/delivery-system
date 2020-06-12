package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "public", catalog = "delivery_system")
public class UserEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "GEN_USER")
    @SequenceGenerator(name="GEN_USER", sequenceName = "USERS_U_ID_SEQ", allocationSize = 1)
    @Column(name = "u_id")
    @Getter @Setter
    private Integer id;

    @Basic
    @Column(name = "u_username")
    @Getter @Setter
    private String username;

    @Basic
    @Column(name = "u_password")
    @Getter @Setter
    private String password;

    @Basic
    @Column(name = "u_name")
    @Getter @Setter
    private String name;

    @Basic
    @Column(name = "u_surname")
    @Getter @Setter
    private String surname;

    @Basic
    @Column(name = "u_role")
    @Getter @Setter
    private String role;
}
