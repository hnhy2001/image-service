package com.example.imageservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "danh_muc_url")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DanhMucUrl extends BaseEntity{
    @Column(name = "url")
    private String url;

}
