package com.miaoshaproject.service;

import com.miaoshaproject.service.model.PromoModel;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-17
 */

public interface PromoService {
    /**
     * 根据商品获取秒杀信息
     * @param itemId
     * @return
     */
    PromoModel getPromoByItemId(String itemId);

    // 活动发布
    void publishPromo(String promoId);

    // 生成秒杀用的令牌
    String generateSecondKillToken(String promoId,String itemId,String userId);
}
