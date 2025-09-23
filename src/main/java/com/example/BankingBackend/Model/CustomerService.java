package com.example.BankingBackend.Model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerService {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_service_seq")
    @SequenceGenerator(name = "customer_service_seq", sequenceName = "CUSTOMER_SERVICE_SEQ", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProductServiceType productService;

    private String name;
    private String accountApplicationNumber;
    @Column(length = 2000)
    private String complaintComments;
    private String email;
    private String mobileNumber;
    private String telephoneNumber;


    public enum ProductServiceType {
        ACCOUNT,
        LOANS,
        CARDS,
        INVESTMENTS
    }
}

