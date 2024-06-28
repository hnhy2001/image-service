package com.example.imageservice.model.request;

import lombok.Data;

@Data
public class SearchReq {
    private String filter;

    private Integer page;

    private Integer size;

    private String sort;
}
