package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.ItemModel;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-16
 */

public interface ItemService {

    /**
     * 销量增加
     * @param itemId
     * @param amount
     * @throws BusinessException
     */
    void increaseSales(String itemId,Integer amount)throws BusinessException;
    /**
     * 添加商品
     * @param itemModel
     * @return
     * @throws BusinessException
     */
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    ItemModel getItemById(String id);

    /**
     * 商品列表浏览
     * @return
     */
    List<ItemModel> listItem();

    /**
     * 减库存
     * @param itemId
     * @param amount
     * @return
     */
    Boolean decreaseStock(String itemId,Integer amount);

    ItemModel getItemByIdInCache(String id);


}
