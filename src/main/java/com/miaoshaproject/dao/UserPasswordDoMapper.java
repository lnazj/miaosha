package com.miaoshaproject.dao;

import com.miaoshaproject.dataObjec.UserPasswordDo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordDoMapper {

    UserPasswordDo selectByUserId(String userId);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Jul 16 11:44:49 CST 2019
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Jul 16 11:44:49 CST 2019
     */
    int insert(UserPasswordDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Jul 16 11:44:49 CST 2019
     */
    int insertSelective(UserPasswordDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Jul 16 11:44:49 CST 2019
     */
    UserPasswordDo selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Jul 16 11:44:49 CST 2019
     */
    int updateByPrimaryKeySelective(UserPasswordDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Jul 16 11:44:49 CST 2019
     */
    int updateByPrimaryKey(UserPasswordDo record);
}