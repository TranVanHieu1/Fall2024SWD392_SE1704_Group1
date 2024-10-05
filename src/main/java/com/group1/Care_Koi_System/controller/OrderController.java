package com.group1.Care_Koi_System.controller;

import com.group1.Care_Koi_System.dto.Order.OrderRequest;
import com.group1.Care_Koi_System.exceptionhandler.ResponseException;
import com.group1.Care_Koi_System.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@CrossOrigin("**")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PutMapping("/change-order-status/{orderID}")
    public ResponseEntity<ResponseException> changeOrderStatus(@PathVariable int orderID, @RequestBody OrderRequest orderRequest){
        return orderService.updateOrderStatus(orderID, orderRequest);
    }
}
