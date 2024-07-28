package org.example.solve_stock;

import org.example.solve_stock.domain.Stock;
import org.example.solve_stock.repository.StockRepository;
import org.example.solve_stock.service.PessmisticLockStockService;
import org.example.solve_stock.service.StockService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SolveStockApplicationTests {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
//    private StockService stockService;
    private PessmisticLockStockService stockService;


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

    /*
     * 동시에 여러 개의 요청을 보냈을 때의 Test
     * 동시에 요청을 보내기 위해 Thread 사용
     * ExecutorService: 병렬 작업 시 여러 개의 작업을 효율적으로 처리하기 위해 제공되는 JAVA 라이브러리
     *                  Task만 지정하면 알아서 ThreadPool을 이용하여 Task를 실행하고 관리함.
     *                  Task는 Queue로 관리됨. ThreadPool에 있는 Thread수보다 Task가 많으면,
     *                  미실행된 Task는 Queue에 저장되고, 실행을 마친 Thread로 할당되어 순차적으로 수행된다.
     */
    @Test
    public void Concurrent_Requests() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // 다른 Thread에서 수행 중인 작업이 완료될 때까지 대기할 수 있도록 돕는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0; i < threadCount; i++){
            executorService.submit(() -> {
               try{
                   stockService.decrease(1L, 1L);
               } finally {
                   latch.countDown();
               }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(0, stock.getQuantity());
    }

}
