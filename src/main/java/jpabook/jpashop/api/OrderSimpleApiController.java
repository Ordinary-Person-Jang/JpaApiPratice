package jpabook.jpashop.api;

import jpabook.jpashop.Repository.OrderRepository;
import jpabook.jpashop.Repository.OrderSearch;
import jpabook.jpashop.Repository.order.simplequery.OrderSimpleQueryRepository;
import jpabook.jpashop.Repository.order.simplequery.OrderSimpleQueryDTO;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-order")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }

    @GetMapping("/api/v2/simple-order")
    public List<SimpleOrderDTO> orderV2() {
        List<Order> allOrders = orderRepository.findAllByString(new OrderSearch());

        List<SimpleOrderDTO> result = allOrders.stream().map(o -> new SimpleOrderDTO(o)).collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v3/simple-order")
    public List<SimpleOrderDTO> orderV3(){
        List<Order> orders =orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDTO> result = orders.stream().map(o -> new SimpleOrderDTO(o)).collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v4/simple-order")
    public List<OrderSimpleQueryDTO> orderV4(){
        return orderSimpleQueryRepository.findOrderDto();

    }

    @Data
    static class SimpleOrderDTO {
        private Long orderId;
        private String name;
        private LocalDateTime time;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDTO(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            time = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
