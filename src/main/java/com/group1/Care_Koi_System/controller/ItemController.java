package com.group1.Care_Koi_System.controller;

import com.group1.Care_Koi_System.dto.Item.ItemRequest;
import com.group1.Care_Koi_System.dto.Item.ItemResponse;
import com.group1.Care_Koi_System.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/item")
@CrossOrigin("**")

public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        List<ItemResponse> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @PostMapping("/create")
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemRequest itemRequest) {
        ItemResponse newItem = itemService.createItem(itemRequest);
        return ResponseEntity.ok(newItem);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable int id, @RequestBody ItemRequest itemRequest) {
        Optional<ItemResponse> updatedItem = itemService.updateItem(id, itemRequest);
        return updatedItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> editItem(@PathVariable int id, @RequestBody ItemRequest itemRequest) {
        Optional<ItemResponse> updatedItem = itemService.updateItem(id, itemRequest);
        return updatedItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/order/{id}")
    public ResponseEntity<String> orderItem(@PathVariable int id, @RequestParam int quantity) {
        String message = itemService.orderItem(id, quantity);
        return ResponseEntity.ok(message);
    }
}
