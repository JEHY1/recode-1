package com.example.recode.dto;

import com.example.recode.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DeleteCartResponse {

    private List<Product> products;
}
