package com.example.BankingBackend.Requests;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class LoginRequestAdmin {
    private String WorkId;
//    private String email;
    private String password;
}
