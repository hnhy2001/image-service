package com.example.imageservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private int status;

    @Column(name = "is_active")
    private int isActive;

    @Column(name = "create_date")
    private Long createDate;

    @Column(name = "update_date")
    private Long updateDate;
}
