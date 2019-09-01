package com.miaoshaproject.dao;

import com.miaoshaproject.dataObjec.PromoDO;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoDOMapper {

    PromoDO selectByItemId(String id);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Wed Jul 17 07:53:11 CST 2019
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Wed Jul 17 07:53:11 CST 2019
     */
    int insert(PromoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Wed Jul 17 07:53:11 CST 2019
     */
    int insertSelective(PromoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Wed Jul 17 07:53:11 CST 2019
     */
    PromoDO selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Wed Jul 17 07:53:11 CST 2019
     */
    int updateByPrimaryKeySelective(PromoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Wed Jul 17 07:53:11 CST 2019
     */
    int updateByPrimaryKey(PromoDO record);
}