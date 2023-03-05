package com.javaprojects.orderservice.service;

import com.javaprojects.orderservice.dto.InventoryStockDto;
import com.javaprojects.orderservice.dto.OrderLineItemDto;
import com.javaprojects.orderservice.dto.OrderRequestDto;
import com.javaprojects.orderservice.model.Order;
import com.javaprojects.orderservice.model.OrderLineItem;
import com.javaprojects.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private  List<OrderLineItem> orderLineItemList;
    private  List<String> skuCodes;

    public void createOrder(OrderRequestDto orderRequestDto){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        orderLineItemList = orderRequestDto.getOrderLineItemList()
                .stream()
                .map(this::mapOrderLineItemDtoToEntity)
                .collect(Collectors.toList());
        order.setOrderLineItemList(orderLineItemList);
        //call inventory service and place order if product is in stock
        skuCodes = order.getOrderLineItemList()
                .stream()
                .map(OrderLineItem::getSkuCode).toList();
        InventoryStockDto[] inventoryResponseDetails = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder ->uriBuilder.queryParam("skuCodes",skuCodes).build() )
                .retrieve()
                .bodyToMono(InventoryStockDto[].class)
                .block();
        boolean allProductsInStock = Arrays.stream(inventoryResponseDetails)
                .allMatch(InventoryStockDto::isInStock);
       if(allProductsInStock){
            orderRepository.save(order);
        }else{
            throw new IllegalArgumentException("Product is not in stock. Please try again later");
        }

    }
    public OrderLineItem mapOrderLineItemDtoToEntity(OrderLineItemDto orderLineItemDto ) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(orderLineItemDto.getPrice());
        orderLineItem.setSkuCode((orderLineItemDto.getSkuCode()));
        orderLineItem.setQuantity(orderLineItemDto.getQuantity());
        return orderLineItem;
    }
}
