package com.group1.Care_Koi_System.controller;

import com.group1.Care_Koi_System.dto.Order.OrderRequest;
import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.Order;
import com.group1.Care_Koi_System.exceptionhandler.ResponseException;
import com.group1.Care_Koi_System.service.OrderService;
import com.group1.Care_Koi_System.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin("**")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountUtils accountUtils;

    @PutMapping("/change-order-status/{orderID}")
    public ResponseEntity<ResponseException> changeOrderStatus(@PathVariable int orderID, @RequestBody OrderRequest orderRequest){
        return orderService.updateOrderStatus(orderID, orderRequest);
    }

    @GetMapping("/view-all")
    public ResponseEntity<List<Order>> viewAllOrders() {
        Account account = accountUtils.getCurrentAccount();
        return orderService.viewAllOrdersForAccount(account);
    }
}
