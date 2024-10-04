package com.group1.Care_Koi_System.dto.Item;

import com.group1.Care_Koi_System.entity.Enum.CategoryItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequest {

    private String itemName;
    private double price;
    private CategoryItem category;
    private int quantity;
    private int quantityOrdered;
}
