package com.example.recode.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeleteCartListRequest {

    private List<Long> cartIds;
}
