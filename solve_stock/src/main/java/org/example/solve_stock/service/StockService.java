package org.example.solve_stock.service;

import org.example.solve_stock.domain.Stock;
import org.example.solve_stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /*
     * Transactional annotation
     * 클래스를 래핑한 클래스로 새로 만들어서 실행하게 한다.
     * 트랜잭션 종료 시점에 DB가 업데이트되는데,
     * method가 완료된 후 실제 DB에 업데이트 되기 전에 다른 Thread가 호출할 수 있다.
     * 그렇다면 그 Thread는 갱신 이전의 값으로 method를 수행한다.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decrease(Long id, Long quantity){
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
