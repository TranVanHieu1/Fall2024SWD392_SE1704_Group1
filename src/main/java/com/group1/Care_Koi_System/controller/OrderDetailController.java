package com.group1.Care_Koi_System.controller;

import com.group1.Care_Koi_System.entity.OrderDetail;
import com.group1.Care_Koi_System.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-detail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

//    @PostMapping
//    public ResponseEntity<OrderDetail> createOrderDetail(@RequestBody OrderDetail orderDetail) {
//        OrderDetail createdOrderDetail= orderDetailService.createOrderDetail(orderDetail);
//        return ResponseEntity.ok(createdOrderDetail);
//    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteOrderDetail(@PathVariable int id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("OrderDetail deleted successfully");
    }
    @GetMapping("/active")
    public ResponseEntity<List<OrderDetail>> getAllActiveOrderDetails() {
        List<OrderDetail> orderDetails = orderDetailService.getAllActiveOrderDetails();
        return ResponseEntity.ok(orderDetails);
    }

}
