package jpabook.jpashop.Repository.order.simplequery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDTO {

    private Long orderId;
    private String name;
    private LocalDateTime time;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDTO(Long orderId, String name, LocalDateTime time, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.time = time;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
