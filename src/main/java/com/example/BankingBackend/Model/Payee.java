package com.example.BankingBackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "PAYEES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payee_seq")
    @SequenceGenerator(name = "payee_seq", sequenceName = "PAYEE_SEQ", allocationSize = 1)
    @Column(name = "PAYEE_ID", nullable = false, precision = 20)
    private int payeeId;

    @Column(name = "PAYEE_NAME", length = 20, nullable = false)
    private String payeeName;


    @Column(name = "PAYEE_ACC_NO", nullable = false, precision = 20)
    private Long payeeAccNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_PAYEE_ACCOUNT"))
    private Account account;
}
