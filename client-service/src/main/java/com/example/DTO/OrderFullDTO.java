package com.example.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderFullDTO {
    @JsonProperty("order_code")
    private String orderCode;
    @JsonProperty("sku_code")
    private String skuCode;
    private String email;
    private String status;
}
