package org.example.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "PAYEES")
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Payee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payee_seq")
    @SequenceGenerator(name = "payee_seq", sequenceName = "PAYEE_SEQ", allocationSize = 1)
    @Column(name = "PAYEE_ID",precision = 20)
    private int payeeId;

    @Column(name = "PAYEE_NAME", length = 20, nullable = false)
    private String payeeName;


    @Column(name = "PAYEE_ACC_NO", nullable = false, precision = 20)
    private Long payeeAccNo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_PAYEE_ACCOUNT"))
    private Account account;

    @Column(name = "IFSC_CODE", length = 15, nullable = false)
    private String ifscCode;


    public Payee(String ifscCode, Account account, Long payeeAccNo, String payeeName, int payeeId) {
        this.ifscCode = ifscCode;
        this.account = account;
        this.payeeAccNo = payeeAccNo;
        this.payeeName = payeeName;
        this.payeeId = payeeId;
    }

    public Payee() {

    }


    public int getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(int payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public Long getPayeeAccNo() {
        return payeeAccNo;
    }

    public void setPayeeAccNo(Long payeeAccNo) {
        this.payeeAccNo = payeeAccNo;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }
}
