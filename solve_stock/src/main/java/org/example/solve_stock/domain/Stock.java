package org.example.solve_stock.domain;

import jakarta.persistence.*;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long productId;
    private Long quantity;

    /*
     * Optimistic Lock 사용을 위해 version column 추가
     * jakarta 라이브러리 (javax 라이브러리) 를 활용한 Version annotation 을 사용할 것.
     */
    @Version
    private Long version;

    public Stock() {
    }

    public Stock(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void decrease(Long quantity){
        if(this.quantity - quantity < 0)
            throw new RuntimeException("재고는 0개 미만이 될 수 없습니다.");

        this.quantity -= quantity;
    }
}
