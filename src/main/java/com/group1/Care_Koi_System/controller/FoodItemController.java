package com.group1.Care_Koi_System.controller;

import com.group1.Care_Koi_System.dto.Item.ItemRequest;
import com.group1.Care_Koi_System.dto.Item.ItemResponse;
import com.group1.Care_Koi_System.service.FoodItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/order/{itemID}")
    public ResponseEntity<String> orderItem(@PathVariable int itemID, @RequestParam int quantity) {
        String message = foodItemService.orderItem(itemID, quantity);
        return ResponseEntity.ok(message);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFoodItem(@PathVariable int id) {
        try {
            foodItemService.deleteItem(id);
            return ResponseEntity.ok("Food item deleted successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
