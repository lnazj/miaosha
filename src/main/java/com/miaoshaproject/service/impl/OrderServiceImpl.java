package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.OrderDoMapper;
import com.miaoshaproject.dao.SequenceDOMapper;
import com.miaoshaproject.dataObjec.OrderDo;
import com.miaoshaproject.dataObjec.SequenceDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIUserConversion;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-16
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDoMapper orderDoMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    /**
     * 提交订单
     * @param userId
     * @param itemId
     * @param amount
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderModel createOrder(String userId, String itemId, Integer amount,String promoId) throws BusinessException{
        // 1校验下单状态,下单商品是否存在,用户是否合法,购买数量是否正确
        //ItemModel itemModel=itemService.getItemById(itemId);
        // 通过缓存进行校验商品
        ItemModel itemModel=itemService.getItemByIdInCache(itemId);
        if (itemModel==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商品信息不存在");
        }

        // 通过缓存校验用户
        //UserModel userModel=userService.getUserById(userId);
//        UserModel userModel=userService.getUserByCache(userId);
//        if (userModel == null) {
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户信息不存在");
//        }
        if (amount<=0||amount>99){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息不合法");
        }

        // 校验活动信息
//        if (promoId!=null){
//            // 校验活动是否存在这个适用商品
//            if (!promoId.equals(itemModel.getPromoModel().getId())){
//                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息不合法");
//            }else if (itemModel.getPromoModel().getStatus().intValue()!=2){
//                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息不合法");
//            }
//        }

        //2 落单减库存
        boolean result=itemService.decreaseStock(itemId,amount);
        if(!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        // 3.订单入库
        OrderModel orderModel=new OrderModel();
        orderModel.setAmount(amount);
        orderModel.setItemId(itemId);
        orderModel.setUserId(userId);
        orderModel.setPromoId(promoId);
        if(promoId!=null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else{
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));
        OrderDo orderDo=convertFromModelToDO(orderModel);
        orderDo.setId(generateOrderNo());
        //生成交易流水号
        generateOrderNo();
        orderDoMapper.insertSelective(orderDo);

        // 增加销量
        itemService.increaseSales(itemId,amount);

        //4.返回前端
        return orderModel;
    }

    /**
     * 生成交易流水号,不能重复使用,无论订单事务成功与否，都进行提交
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private String generateOrderNo(){
        // 日期
        StringBuilder stringBuilder=new StringBuilder();
        LocalDateTime now= LocalDateTime.now() ;
        String nowDate=now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);

        // 中间6位为自增序列，mysql不会有自增，所以新建一个表实现
        int sequence=0;
        SequenceDO sequenceDO=sequenceDOMapper.selectSequenceByName("order_info");
        sequence=sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequence+sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr=String.valueOf(sequence);
        for(int i=0;i<6-sequenceStr.length();i++){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);

        // 最后两位为分库分表，暂时写死
        stringBuilder.append("00");
        return stringBuilder.toString();
        }

    private OrderDo convertFromModelToDO(OrderModel orderModel){
        if (orderModel == null) {
            return null;
        }
        OrderDo orderDo=new OrderDo();
        BeanUtils.copyProperties(orderModel,orderDo);
        orderDo.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDo.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDo;
    }
}
