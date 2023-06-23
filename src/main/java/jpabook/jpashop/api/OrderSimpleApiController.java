package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * XtoOne
 * Order
 * Order -> Memeber ( Many To One
 * Order -> Delivery ( One To One
 * To One 엔티티관계 (다루는게 엔티티) 에서의 최적화
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;


    /**
     * V1 : 엔티티자체
     * 무한루프에 빠진다 -> order 안의 member 안의 order 안의 member 무한루프돌면서 뽑아냄
     * 해결방법 : 각 양뱡향이 걸린 엔티티에 @JsonIgnore 설정을 해주면 됨. -> 에러 (에러 메세지가 타입이 정의가 되지않음
     * ByteBuddyInterceptror() -> 프록시 기술로 가짜 객체를 넣어둬서 접근할 일 있으면 채우는 과정을 거치는데, 이런 애를 뽑을 수 없어서 에러남
     * Jackson 라이브러리가 proxy 객체를 json 타입으로 변환하는 과정에서 -> 에러
     * 해결방법 : 하이버네이트5모듈을 설치해야됨
     *
     * 결론 : force lazy loading으로 인한 성능상 문제, 엔티티자체를 반환하는건 원치않은 정보를 넘김
     * @return
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        for(Order order : orders){
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        return orders;
    }


    /**
     * DTO에 맞춰서 필요한 부분만 리턴
     * order 조회 sql 1번 -> row 2개 루프가 2번 돔
     * 1번째 루프 : member를 찾기위해 쿼리 1개, delevery 찾기위해 쿼리 1개
     * 2번째 루프 : member를 찾기위해 쿼리 1개, delevery 찾기위해 쿼리 1개
     * 총 쿼리 5개 (N + N + 1 문제 발생) -> Fetch 타입을 EAGER 로 바꾸는게 맞지 않나 -> 이상한쿼리, 예측이 안된다 -> 사용 자제... 다 LAZY 로 로딩
     * @return
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        List<SimpleOrderDto> list = orders.stream().map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return list;
    }

    /**
     * fetch 조인을 사용해서 최적화
     * 쿼리가 1번 나감
     * order meber 조인
     * order delivery 조인
     * order 에서 접근할때 fetch 되었기 때문에 지연로딩은 사용되지않는다.
     * 긴 row 가 반환되지만, 그래프탐색간 JPA가 촤자작 나눠서 뿌려준다.
     *
     * 단점) select에서 모든것을 다끌고와서 문제임
     * @return
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> list = orders.stream().map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 쿼리는 원하는 정보만 가져오지만, 로직의 재사용성이 좋지않다.
     * DTO로 조회를 했기 때문에 변경할 수 없다.
     *
     * 단) 지저분함, api 스펙에 맞춰 작성해야됨, 화면에 박힌 데이터로 만드는것이다.
     * @return
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
        List<OrderSimpleQueryDto> orderDtos = orderSimpleQueryRepository.findOrderDtos();
        return orderDtos;
    }

    @Data
    private class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //Lazy 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //Lazy 초기화
        }
    }



}
