package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.dto.Item.ItemRequest;
import com.group1.Care_Koi_System.dto.Item.ItemResponse;
import com.group1.Care_Koi_System.entity.FoodItem;
import com.group1.Care_Koi_System.entity.OrderDetail;
import com.group1.Care_Koi_System.exceptionhandler.OrderDetail.ItemNotFoundException;
import com.group1.Care_Koi_System.exceptionhandler.OrderDetail.OutOfStockException;
import com.group1.Care_Koi_System.repository.FoodItemRepository;
import com.group1.Care_Koi_System.repository.OrderDetailRepository;
import com.group1.Care_Koi_System.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodItemService {
    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private AccountUtils accountUtils;

    public List<ItemResponse> getAllItems() {
        List<FoodItem> foodItems = foodItemRepository.findAll();
        return foodItems.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public ItemResponse createItem(ItemRequest itemRequest) {
        FoodItem foodItem = FoodItem.builder()
                .itemName(itemRequest.getItemName())
                .price(itemRequest.getPrice())
                .category(itemRequest.getCategory())
                .quantity(itemRequest.getQuantity())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
        FoodItem savedFoodItem = foodItemRepository.save(foodItem);
        return convertToResponse(savedFoodItem);
    }

    public Optional<ItemResponse> updateItem(int id, ItemRequest itemRequest) {
        Optional<FoodItem> itemOptional = foodItemRepository.findById(id);
        if (itemOptional.isPresent()) {
            FoodItem foodItem = itemOptional.get();
            foodItem.setItemName(itemRequest.getItemName());
            foodItem.setPrice(itemRequest.getPrice());
            foodItem.setCategory(itemRequest.getCategory());
            foodItem.setQuantity(itemRequest.getQuantity());
            foodItem.setUpdateAt(LocalDateTime.now());

            FoodItem updateFoodItem = foodItemRepository.save(foodItem);
            return Optional.of(convertToResponse(updateFoodItem));
        }
        return Optional.empty();
    }

    public Optional<ItemResponse> getItemById(int id) {
        Optional<FoodItem> itemOptional = foodItemRepository.findById(id);

        return itemOptional.map(this::convertToResponse);
    }

    public ItemResponse editItem(int id, ItemRequest itemRequest) {
        Optional<FoodItem> existingItemOptional = foodItemRepository.findById(id);
        if (existingItemOptional.isEmpty()) {
            throw new RuntimeException("Item with ID " + id + " not found.");
        }
        FoodItem existingFoodItem = existingItemOptional.get();
        existingFoodItem.setItemName(itemRequest.getItemName());
        existingFoodItem.setPrice(itemRequest.getPrice());
        existingFoodItem.setCategory(itemRequest.getCategory());
        existingFoodItem.setQuantity(itemRequest.getQuantity());
        existingFoodItem.setUpdateAt(LocalDateTime.now());

        foodItemRepository.save(existingFoodItem);

        return convertToResponse(existingFoodItem);
    }

    public String orderItem(int itemId, int quantity) {
        //kiểm tra item có tồn tại không
        Optional<FoodItem> itemOptional = foodItemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new ItemNotFoundException("Item with ID " + itemId + " not found.");
        }
        FoodItem foodItem = itemOptional.get();
        //kiểm tra số lượng đặt hàng có lớn hơn số lượng tồn kho không
        if (foodItem.getQuantity() < quantity) {
            throw new OutOfStockException("Item with ID " + itemId + " is out of stock.");
        }
        // Cap nhat so luong ton kho
        foodItem.setQuantity(foodItem.getQuantity() - quantity);
        foodItemRepository.save(foodItem);
        // Tạo Order mới
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setFoodItem(foodItem);
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(foodItem.getPrice() * quantity);
        // Lưu OrderDetail vào cơ sở dữ liệu
        orderDetailRepository.save(orderDetail);
        return "Order placed  successfully.";
    }

    private ItemResponse convertToResponse(FoodItem foodItem) {
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setId(foodItem.getId());
        itemResponse.setItemName(foodItem.getItemName());
        itemResponse.setPrice(foodItem.getPrice());
        itemResponse.setCategory(foodItem.getCategory());
        itemResponse.setQuantity(foodItem.getQuantity());
        itemResponse.setCreateAt(foodItem.getCreateAt());
        itemResponse.setUpdateAt(foodItem.getUpdateAt());
        itemResponse.setDeleted(foodItem.isDeleted());

        return itemResponse;
    }


}
