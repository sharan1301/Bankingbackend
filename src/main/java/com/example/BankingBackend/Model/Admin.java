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
    @Column(name = "WORK_ID")
    private String workId;
    @Column(name="FULL_NAME")
    private String fullName;
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "DEPARTMENT" , length = 20)
    private String department="IT Administration";
    @Column(name = "ROLE", nullable = false)
    private String role = "ADMIN";

    public Admin(int adminId, String workID, String fullName, String email, String password, String department, String role) {
        this.adminId = adminId;
        this.workId = workID;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.department = department;
        this.role = role;
    }

    public Admin(){}

}
