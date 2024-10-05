package com.group1.Care_Koi_System.dto.Order;

import com.group1.Care_Koi_System.entity.Enum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private OrderStatus orderStatus;
}
