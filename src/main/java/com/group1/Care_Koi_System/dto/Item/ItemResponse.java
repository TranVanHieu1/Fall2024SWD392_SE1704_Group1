package com.group1.Care_Koi_System.dto.Item;

import com.group1.Care_Koi_System.entity.Enum.CategoryItem;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemResponse {
    private int id;
    private String itemName;
    private double price;
    private CategoryItem category;
    private int quantity;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private boolean isDeleted;
    private String message;
}
