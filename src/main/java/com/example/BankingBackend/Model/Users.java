
package com.example.BankingBackend.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq_gen")
    @SequenceGenerator(name = "users_seq_gen", sequenceName = "users_seq", allocationSize = 1)
    private int userId;


    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(nullable = false, unique = true, length = 12)
    private String aadhaarNumber;

    @Column(nullable = false, unique = true, length = 10)
    private String panNumber;

    @Column(nullable = false, length = 100)
    private String accountType;

    @Column(length = 100)
    private String occupation;

    @Column(name = "annual_income")
    private Double annualIncome;

    @Column(nullable = false, length = 255)
    private String password; // generated password

    @Column(length = 20)
    private String status; // APPROVED
}




