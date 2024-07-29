package org.example.solve_stock.facade;

import org.example.solve_stock.service.OptimisticLockStockService;
import org.springframework.stereotype.Component;

@Component
public class OptimisticLockStockFacade {

    private final OptimisticLockStockService optimisticLockStockService;

    public OptimisticLockStockFacade(OptimisticLockStockService optimisticLockStockService) {
        this.optimisticLockStockService = optimisticLockStockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        // Optimistic Lock은 실패했을 때 재시도를 해야하므로 while문을 활용함.
        while(true) {
            try{
                optimisticLockStockService.decrease(id, quantity);
                break;
            } catch (Exception e) {
                Thread.sleep(50); // decrease 실패 시, 50ms 뒤에 재시도
            }

        }
    }
}
