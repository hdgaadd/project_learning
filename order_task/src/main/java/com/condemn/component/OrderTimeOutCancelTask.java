package com.condemn.component;

import com.condemn.domain.OrderSetting;
import com.condemn.domain.OrderTable;
import com.condemn.mapper.OrderSettingMapper;
import com.condemn.service.IOrderTableService;
import com.condemn.service.ISkuStockService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderTimeOutCancelTask {

    @Resource
    private OrderSettingMapper orderSettingMapper;
    @Resource
    private IOrderTableService orderTableService;
    @Resource
    private ISkuStockService skuStockService;

    @Scheduled(fixedRate = 2000)
    public void cancelTimeOutOrder() {

        System.out.println("--------------开始执行任务-----------------");

        // 查询出订单超时时间
        OrderSetting orderSetting = orderSettingMapper.selectById(1);
        int NORMAL_OVER_TIME = orderSetting.getNormalOrderOvertime();

        // 根据订单超时时间，查询出所有超时订单
        List<OrderTable> orderTableListist = orderTableService.getTimeOutOrder(NORMAL_OVER_TIME);

        List<Integer> orderIdList = new ArrayList<>();
        for (OrderTable orderTable : orderTableListist) {
            orderIdList.add(orderTable.getId());
        }

        // 根据超时订单，修改订单状态为关闭，而不是删除订单数据
        orderTableService.updateOrder(orderIdList);


        // 根据超时订单，修改订单对应商品的锁定库存
        skuStockService.updateStock(orderTableListist);
    }
}
