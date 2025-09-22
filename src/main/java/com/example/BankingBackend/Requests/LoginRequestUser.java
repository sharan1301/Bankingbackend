package com.example.BankingBackend.Requests;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class LoginRequestUser {
    private String userName;
    private String password;
}
