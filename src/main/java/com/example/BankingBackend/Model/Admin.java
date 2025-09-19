package com.example.BankingBackend.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ADMIN")
@Data
@Getter
@Setter
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_seq")
    @SequenceGenerator(name = "admin_seq", sequenceName = "ADMIN_SEQ", allocationSize = 1)
    @Column(name = "ADMIN_ID")
    private int adminId;

    @Column(name="FULL_NAME")
    private String fullName;
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ROLE", nullable = false)
    private String role = "ADMIN";

    public Admin(){}
    public Admin(int adminId, String fullName, String email, String password, String role) {
        this.adminId = adminId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
