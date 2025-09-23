package com.example.BankingBackend.Model;

import javax.persistence.*;
/*import javax.validation.constraints.*;*/
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.ConnectionBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "CARDS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_seq")
    @SequenceGenerator(name = "card_seq", sequenceName = "CARD_SEQ", allocationSize = 1)
    @Column(name = "CARD_ID", nullable = false)
    private Long cardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CARD_USER"))

    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_NO", nullable = false, foreignKey = @ForeignKey(name = "FK_CARD_ACCOUNT"))

    private Account account;

    @Column(name = "CARD_NUMBER", nullable = false, length = 16, unique = true)

    private String cardNumber;

    @Column(name = "EXPIRY_DATE", nullable = false)

    private LocalDate expiryDate;

    @Column(name = "CVV", nullable = false)

    private Integer cvv;

    @Column(name = "STATUS", length = 20)
    @Enumerated(EnumType.STRING)
    private CardStatus status = CardStatus.ACTIVE;

    @Column(name = "CARD_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;



    public enum CardStatus {
        ACTIVE, INACTIVE, BLOCKED, EXPIRED, CANCELLED
    }

    public enum CardType {
        DEBIT, CREDIT, PREPAID
    }
}
