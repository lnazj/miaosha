package com.miaoshaproject.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.miaoshaproject.dao.ItemDoMapper;
import com.miaoshaproject.dao.ItemStockDoMapper;
import com.miaoshaproject.dataObjec.ItemDo;
import com.miaoshaproject.dataObjec.ItemStockDo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.PromoModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-16
 */
@Service
public class ItemServiceImpl implements ItemService{
    @Autowired
    private ItemDoMapper itemDoMapper;

    @Autowired
    private ItemStockDoMapper itemStockDoMapper;

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private PromoService promoService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItemModel createItem(ItemModel itemModel) throws BusinessException{
        //入参校验通过ValidatorImpl
      ValidationResult result=validator.validate(itemModel);
        if (result.isHasError()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        // 插入商品表
        ItemDo itemDo=new ItemDo();
        BeanUtils.copyProperties(itemModel,itemDo);
        itemDo.setId(IdWorker.getIdStr());
        itemDo.setPrice(itemModel.getPrice().doubleValue());
        itemDoMapper.insertSelective(itemDo);

        // 插入库存表
        ItemStockDo itemStockDo=new ItemStockDo();
        BeanUtils.copyProperties(itemModel,itemStockDo);
        itemStockDo.setId(IdWorker.getIdStr());
        itemStockDo.setItemId(itemDo.getId());
        itemStockDoMapper.insertSelective(itemStockDo);

        // 返回值
        return getItemById(itemDo.getId());
    }

    @Override
    public ItemModel getItemById(String id){
        ItemDo itemDo = itemDoMapper.selectByPrimaryKey(id);
        if (itemDo == null) {
            return null;
        }

        ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());
        if(itemStockDo==null){
            return  null;
        }

        ItemModel itemModel=new ItemModel();
        BeanUtils.copyProperties(itemDo,itemModel);
        itemModel.setPrice(new BigDecimal(itemDo.getPrice()));
        itemModel.setStock(itemStockDo.getStock());

        // 获取活动商品信息
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        if (promoModel != null && promoModel.getStatus() != 3) {
            itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }

    @Override
    public List<ItemModel> listItem(){
        List<ItemDo> itemDoList=itemDoMapper.listItem();
        List<ItemModel> itemModelList=itemDoList.stream().map(itemDo->{
            ItemStockDo itemStockDo=itemStockDoMapper.selectByItemId(itemDo.getId());
            ItemModel itemModel=this.convertItemModelFromDO(itemDo, itemStockDo);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    private ItemModel convertItemModelFromDO(ItemDo itemDo, ItemStockDo itemStockDo) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDo, itemModel);
        itemModel.setStock(itemStockDo.getStock());
        itemModel.setPrice(new BigDecimal(itemDo.getPrice()));
        return itemModel;
    }

    @Override
    @Transactional
    public Boolean decreaseStock(String itemId,Integer amount){
        long result=redisTemplate.opsForValue().increment("promo_item_stock_"+itemId,amount.intValue()*-1);
        //int affectedRow=itemStockDoMapper.decreaseStock(itemId,amount);
        if(result>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 增加销量
     * @param itemId
     * @param amount
     * @throws BusinessException
     */
    @Override
    @Transactional
    public void increaseSales(String itemId, Integer amount) throws BusinessException {
        itemDoMapper.increaseSales(itemId, amount);
    }

    @Override
    public ItemModel getItemByIdInCache(String id){
      ItemModel itemModel=(ItemModel)redisTemplate.opsForValue().get("item_validate_"+id);
      if(itemModel==null){
          itemModel=this.getItemById(id);
          redisTemplate.opsForValue().set("item_validate_"+id,itemModel,10, TimeUnit.MINUTES);
      }
      return itemModel;
    }
}
