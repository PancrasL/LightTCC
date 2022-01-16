package github.pancras.stock.service;

import github.pancras.api.StockService;
import github.pancras.spring.annotation.RpcService;
import github.pancras.tcccore.annotation.TccTry;
import github.pancras.tcccore.dto.TccActionContext;

@RpcService
public class StockServiceImpl implements StockService {
    @TccTry
    @Override
    public boolean reduceStock(TccActionContext context) {
        System.out.println("create Order");
        return false;
    }

    @Override
    public boolean commit(TccActionContext context) {
        System.out.println("Order commit");
        return false;
    }

    @Override
    public boolean rollback(TccActionContext context) {
        System.out.println("Order rollback");
        return false;
    }
}
