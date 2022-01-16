package github.pancras.business.service;

import org.springframework.stereotype.Component;

import github.pancras.api.BalanceService;
import github.pancras.api.OrderService;
import github.pancras.api.StockService;
import github.pancras.spring.annotation.RpcReference;
import github.pancras.tcccore.dto.TccActionContext;

/**
 * 只会被BusinessService使用，定义为包级访问
 */
@Component
class BusinessHandler {
    @RpcReference
    private OrderService orderService;
    @RpcReference
    private StockService stockService;
    @RpcReference
    private BalanceService balanceService;

    public void doCreateBusiness(TccActionContext context) {
//        orderService.createOrder(context);
//        stockService.reduceStock(context);
        balanceService.reduceBalance(context);
    }
}
