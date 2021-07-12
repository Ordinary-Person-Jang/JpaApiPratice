package jpabook.jpashop.Repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();
        result.forEach(r -> {
            List<OrderItemsQueryDto> orderItems = findOrderItems(r.getOrderId());
            r.setItemsDtos(orderItems);
        });
        return result;
    }
    public List<OrderItemsQueryDto> findOrderItems(Long orderId){
        return em.createQuery(
                "select new jpabook.jpashop.Repository.order.query.OrderItemsQueryDto" +
                "(oi.order.id, i.name, oi.orderPrice, oi.count ) " +
                "from OrderItem oi " +
                "join oi.item i " +
                "where oi.order.id = :orderId", OrderItemsQueryDto.class)
                .setParameter("orderId", orderId).getResultList();
    }

    public List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.Repository.order.query.OrderQueryDto" +
                        "(o.id, m.name, o.orderDate, o.status, d.address) " +
                        "from Order o " +
                "join o.member m " +
                "join o.delivery d", OrderQueryDto.class).getResultList();
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> orders = findOrders();
        List<Long> orderIdMap = toOrderIds(orders);

        Map<Long, List<OrderItemsQueryDto>> OrderItemsMap = findOrderItemsMap(orderIdMap);
        orders.forEach(o -> o.setItemsDtos(OrderItemsMap.get(o.getOrderId())));

        return orders;
    }

    private Map<Long, List<OrderItemsQueryDto>> findOrderItemsMap(List<Long> orderIdMap) {
        List<OrderItemsQueryDto> orderItemsQueryDtos = em.createQuery(
                "select new jpabook.jpashop.Repository.order.query.OrderItemsQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                "from OrderItem oi " +
                "join oi.item i " +
                "where oi.order.id in :orderIds", OrderItemsQueryDto.class)
                .setParameter("orderIds", orderIdMap).getResultList();

        Map<Long, List<OrderItemsQueryDto>> OrderItemsMap = orderItemsQueryDtos.stream().
                collect(Collectors.groupingBy(orderItemsQueryDto -> orderItemsQueryDto.getOrderId()));
        return OrderItemsMap;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> orders) {
        List<Long> orderIdMap = orders.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
        return orderIdMap;
    }
}
