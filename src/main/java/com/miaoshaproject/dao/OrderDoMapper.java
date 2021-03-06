package com.miaoshaproject.dao;

import com.miaoshaproject.dataObjec.OrderDo;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Tue Jul 16 15:59:39 CST 2019
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Tue Jul 16 15:59:39 CST 2019
     */
    int insert(OrderDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Tue Jul 16 15:59:39 CST 2019
     */
    int insertSelective(OrderDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Tue Jul 16 15:59:39 CST 2019
     */
    OrderDo selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Tue Jul 16 15:59:39 CST 2019
     */
    int updateByPrimaryKeySelective(OrderDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Tue Jul 16 15:59:39 CST 2019
     */
    int updateByPrimaryKey(OrderDo record);
}