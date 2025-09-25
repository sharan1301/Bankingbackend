package com.example.BankingBackend.Requests;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class LoginRequestUser {
    private String custId;
    private String password;

    public String getCustId() {
        return custId;
    }

    public String getPassword() {
        return password;
    }
}
