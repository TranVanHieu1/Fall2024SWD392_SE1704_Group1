package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.dto.Item.ItemRequest;
import com.group1.Care_Koi_System.dto.Item.ItemResponse;
import com.group1.Care_Koi_System.entity.Item;
import com.group1.Care_Koi_System.repository.ItemRepository;
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
