package com.chrisgya.springsecurity.model;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class BasePage {
    private int pageNumber =0;
    private int pageSize = 20;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
}
