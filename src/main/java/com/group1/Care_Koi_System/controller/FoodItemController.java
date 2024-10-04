package com.group1.Care_Koi_System.controller;

import com.group1.Care_Koi_System.dto.Item.ItemRequest;
import com.group1.Care_Koi_System.dto.Item.ItemResponse;
import com.group1.Care_Koi_System.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/item")
@CrossOrigin("**")

public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        List<ItemResponse> items = foodItemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @PostMapping("/create")
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemRequest itemRequest) {
        ItemResponse newItem = foodItemService.createItem(itemRequest);
        return ResponseEntity.ok(newItem);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable int id, @RequestBody ItemRequest itemRequest) {
        Optional<ItemResponse> updatedItem = foodItemService.updateItem(id, itemRequest);
        return updatedItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> editItem(@PathVariable int id, @RequestBody ItemRequest itemRequest) {
        Optional<ItemResponse> updatedItem = foodItemService.updateItem(id, itemRequest);
        return updatedItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/order/{id}")
    public ResponseEntity<String> orderItem(@PathVariable int id, @RequestParam int quantity) {
        String message = foodItemService.orderItem(id, quantity);
        return ResponseEntity.ok(message);
    }
}
