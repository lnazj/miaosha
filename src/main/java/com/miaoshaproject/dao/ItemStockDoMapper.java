package com.miaoshaproject.dao;

import com.miaoshaproject.dataObjec.ItemStockDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemStockDoMapper {
    /**
     * 减库存
     * @param itemId
     * @param amount
     * @return
     */
    int decreaseStock(@Param("itemId") String itemId,@Param("amount") Integer amount);

    ItemStockDo selectByItemId(String itemId);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Jul 16 11:44:49 CST 2019
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Jul 16 11:44:49 CST 2019
     */
    int insert(ItemStockDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Jul 16 11:44:49 CST 2019
     */
    int insertSelective(ItemStockDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Jul 16 11:44:49 CST 2019
     */
    ItemStockDo selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Jul 16 11:44:49 CST 2019
     */
    int updateByPrimaryKeySelective(ItemStockDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Jul 16 11:44:49 CST 2019
     */
    int updateByPrimaryKey(ItemStockDo record);
}