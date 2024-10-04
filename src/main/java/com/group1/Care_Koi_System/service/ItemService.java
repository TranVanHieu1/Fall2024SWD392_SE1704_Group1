package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.dto.Item.ItemRequest;
import com.group1.Care_Koi_System.dto.Item.ItemResponse;
import com.group1.Care_Koi_System.entity.FoodItem;
import com.group1.Care_Koi_System.entity.OrderDetail;
import com.group1.Care_Koi_System.repository.ItemRepository;
import com.group1.Care_Koi_System.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<ItemResponse> getAllItems() {
        List<FoodItem> foodItems = itemRepository.findAll();
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
        FoodItem savedFoodItem = itemRepository.save(foodItem);
        return convertToResponse(savedFoodItem);
    }

    public Optional<ItemResponse> updateItem(int id, ItemRequest itemRequest) {
        Optional<FoodItem> itemOptional = itemRepository.findById(id);
        if (itemOptional.isPresent()) {
            FoodItem foodItem = itemOptional.get();
            foodItem.setItemName(itemRequest.getItemName());
            foodItem.setPrice(itemRequest.getPrice());
            foodItem.setCategory(itemRequest.getCategory());
            foodItem.setQuantity(itemRequest.getQuantity());
            foodItem.setUpdateAt(LocalDateTime.now());

            FoodItem updateFoodItem = itemRepository.save(foodItem);
            return Optional.of(convertToResponse(updateFoodItem));
        }
        return Optional.empty();
    }

    public Optional<ItemResponse> getItemById(int id) {
        Optional<FoodItem> itemOptional = itemRepository.findById(id);

        return itemOptional.map(this::convertToResponse);
    }

    public ItemResponse editItem(int id, ItemRequest itemRequest) {
        Optional<FoodItem> existingItemOptional = itemRepository.findById(id);
        if (existingItemOptional.isEmpty()) {
            throw new RuntimeException("Item with ID " + id + " not found.");
        }
        FoodItem existingFoodItem = existingItemOptional.get();
        existingFoodItem.setItemName(itemRequest.getItemName());
        existingFoodItem.setPrice(itemRequest.getPrice());
        existingFoodItem.setCategory(itemRequest.getCategory());
        existingFoodItem.setQuantity(itemRequest.getQuantity());
        existingFoodItem.setUpdateAt(LocalDateTime.now());

        itemRepository.save(existingFoodItem);

        return convertToResponse(existingFoodItem);
    }

    public String orderItem(int itemId, int quantity) {
        //kiểm tra item có tồn tại không
        Optional<FoodItem> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new RuntimeException("Item with ID " + itemId + " not found.");
        }
        FoodItem foodItem = itemOptional.get();
        //kiểm tra số lượng đặt hàng có lớn hơn số lượng tồn kho không
        if (foodItem.getQuantity() < quantity) {
            throw new RuntimeException("Item with ID " + itemId + " is out of stock.");
        }
        // Cap nhat so luong ton kho
        foodItem.setQuantity(foodItem.getQuantity() - quantity);
        itemRepository.save(foodItem);
        // Tạo Order mới
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setFoodItem(foodItem);
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(foodItem.getPrice() * quantity);
        // Lưu OrderDetail vào cơ sở dữ liệu
        orderDetailRepository.save(orderDetail);
        return "Order successfully.";
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
