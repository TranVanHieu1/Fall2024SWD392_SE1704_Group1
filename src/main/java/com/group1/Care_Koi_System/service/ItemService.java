package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.dto.Item.ItemRequest;
import com.group1.Care_Koi_System.dto.Item.ItemResponse;
import com.group1.Care_Koi_System.entity.Item;
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
        List<Item> items = itemRepository.findAll();
        return items.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public ItemResponse createItem(ItemRequest itemRequest) {
        Item item = Item.builder()
                .itemName(itemRequest.getItemName())
                .price(itemRequest.getPrice())
                .category(itemRequest.getCategory())
                .quantity(itemRequest.getQuantity())
                .serviceType(itemRequest.getServiceType())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
        Item savedItem = itemRepository.save(item);
        return convertToResponse(savedItem);
    }

    public Optional<ItemResponse> updateItem(int id, ItemRequest itemRequest) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();
            item.setItemName(itemRequest.getItemName());
            item.setPrice(itemRequest.getPrice());
            item.setCategory(itemRequest.getCategory());
            item.setQuantity(itemRequest.getQuantity());
            item.setServiceType(itemRequest.getServiceType());
            item.setUpdateAt(LocalDateTime.now());

            Item updateItem = itemRepository.save(item);
            return Optional.of(convertToResponse(updateItem));
        }
        return Optional.empty();
    }

    public Optional<ItemResponse> getItemById(int id) {
        Optional<Item> itemOptional = itemRepository.findById(id);

        return itemOptional.map(this::convertToResponse);
    }

    public ItemResponse editItem(int id, ItemRequest itemRequest) {
        Optional<Item> existingItemOptional = itemRepository.findById(id);
        if (existingItemOptional.isEmpty()) {
            throw new RuntimeException("Item with ID " + id + " not found.");
        }
        Item existingItem = existingItemOptional.get();
        existingItem.setItemName(itemRequest.getItemName());
        existingItem.setPrice(itemRequest.getPrice());
        existingItem.setCategory(itemRequest.getCategory());
        existingItem.setQuantity(itemRequest.getQuantity());
        existingItem.setServiceType(itemRequest.getServiceType());
        existingItem.setUpdateAt(LocalDateTime.now());

        itemRepository.save(existingItem);

        return convertToResponse(existingItem);
    }

    public String orderItem(int itemId, int quantity) {
        //kiểm tra item có tồn tại không
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new RuntimeException("Item with ID " + itemId + " not found.");
        }
        Item item = itemOptional.get();
        //kiểm tra số lượng đặt hàng có lớn hơn số lượng tồn kho không
        if (item.getQuantity() < quantity) {
            throw new RuntimeException("Item with ID " + itemId + " is out of stock.");
        }
        // Cap nhat so luong ton kho
        item.setQuantity(item.getQuantity() - quantity);
        itemRepository.save(item);
        // Tạo Order mới
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setItem(item);
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(item.getPrice() * quantity);
        // Lưu OrderDetail vào cơ sở dữ liệu
        orderDetailRepository.save(orderDetail);
        return "Order successfully.";
    }

    private ItemResponse convertToResponse(Item item) {
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setId(item.getId());
        itemResponse.setItemName(item.getItemName());
        itemResponse.setPrice(item.getPrice());
        itemResponse.setCategory(item.getCategory());
        itemResponse.setQuantity(item.getQuantity());
        itemResponse.setServiceType(item.getServiceType());
        itemResponse.setCreateAt(item.getCreateAt());
        itemResponse.setUpdateAt(item.getUpdateAt());
        itemResponse.setDeleted(item.isDeleted());

        return itemResponse;
    }
}
