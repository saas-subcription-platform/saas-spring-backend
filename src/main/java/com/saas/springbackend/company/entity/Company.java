package com.saas.springbackend.company.entity;

import com.saas.springbackend.common.entity.BaseClass;
import com.saas.springbackend.subscription.entity.Subscription;
import com.saas.springbackend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(
                name = "id",
                column = @Column(name = "company_id")
)
@ToString
public class Company
        extends BaseClass {
    @Column(name = "company_name", nullable = false, unique = true)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_size", nullable = false)
    private CompanySize companySize;

    @Column(length = 15)
    private String phone;

    private String address;

    private String city;

    private String state;

    private String country;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "gst_number")
    private String gstNumber;

//    @OneToMany(
//            mappedBy = "company",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//    private List<User> users = new ArrayList<>();
//
//    @OneToOne(
//            mappedBy = "company",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true,
//            fetch = FetchType.LAZY
//    )
//    private Subscription subscription;
}