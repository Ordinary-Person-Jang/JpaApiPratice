package jpabook.jpashop.Repository.order.query;

import lombok.Data;

@Data
public class OrderItemsQueryDto {
    private Long orderId;
    private String name;
    private int price;
    private int count;

    public OrderItemsQueryDto(Long orderId, String name, int price, int count) {
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.count = count;
    }
}
