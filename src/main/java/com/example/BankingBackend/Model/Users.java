
package com.example.BankingBackend.Model;
import javax.persistence.*;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@Data
@Getter
@Setter

public class Users {


       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       @Column(name="USER_ID")
        private int userId;

        @Column(name="FULL_NAME")
        private String fullName;
        @Column(name = "EMAIL", nullable = false, unique = true)
        private String email;
        @Column(name = "PASSWORD", nullable = false)
        private String password;
        @Column(name = "ROLE", nullable = false)
        private String role = "USER";

    public Users(String fullName, String email, String password, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Users() {

    }
}



