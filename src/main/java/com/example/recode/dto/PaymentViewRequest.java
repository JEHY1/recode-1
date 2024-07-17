package com.example.recode.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PaymentViewRequest {

    List<Long> cartId;
}
