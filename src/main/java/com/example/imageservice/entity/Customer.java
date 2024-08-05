package com.example.imageservice.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Customer extends BaseEntity{
    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "reponse_status")
    private String reponseStatus;

    @Column(name = "code")
    private String code;

    @Column(name = "note", length = 9999999)
    private String note;

    @Column(name = "loan")
    private Long loan;

    @Column(name = "date")
    private String date;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Image> images;


}
