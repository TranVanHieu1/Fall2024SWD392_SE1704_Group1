package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.entity.OrderDetail;
import com.group1.Care_Koi_System.exceptionhandler.OrderDetail.OrderDetailNotFoundException;
import com.group1.Care_Koi_System.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

//    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
//        return orderDetailRepository.save(orderDetail);
//    }
    public void deleteOrderDetail(int id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new OrderDetailNotFoundException("OrderDetail không tồn tại với id: " + id));
        orderDetail.setDeleted(true);
        orderDetailRepository.save(orderDetail);
    }
    public List<OrderDetail> getAllActiveOrderDetails() {
        return orderDetailRepository.findByIsDeletedFalse();
    }
}
