package com.javaprojects.orderservice.controller;

import com.javaprojects.orderservice.dto.OrderRequestDto;
import com.javaprojects.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrder(@RequestBody OrderRequestDto orderRequestDto){

        orderService.createOrder(orderRequestDto);
        return "Ordered successfully";
    }


}
