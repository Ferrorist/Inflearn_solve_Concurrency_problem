package org.example.solve_stock;

import org.example.solve_stock.domain.Stock;
import org.example.solve_stock.repository.StockRepository;
import org.example.solve_stock.service.StockService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SolveStockApplicationTests {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    @BeforeEach
    public void before() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    /*
     * 테스트 케이스가 1개만 들어왔을 경우의 Test
     */
    @Test
    public void 재고감소() {
        stockService.decrease(1L, 1L);
        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(99, stock.getQuantity());
    }
}
