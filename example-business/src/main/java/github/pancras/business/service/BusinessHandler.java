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
        if (!orderService.createOrder(context)) {
            throw new RuntimeException("创建订单失败");
        }
        if (!stockService.reduceStock(context)) {
            throw new RuntimeException("减少库存失败");
        }
        if (!balanceService.reduceBalance(context)) {
            throw new RuntimeException("扣款失败");
        }
    }
}
