package com.miaoshaproject.controller;

import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.miaoshaproject.controller.viewObject.ItemVo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.CacheService;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.text.DateFormat;
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
@RestController
@RequestMapping("/item")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class ItemController{
    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private PromoService promoService;

    /**
     * 添加商品
     * @param title
     * @param description
     * @param price
     * @param stock
     * @param imgUrl
     * @return
     * @throws BusinessException
     */
    @PostMapping(value = "/create")
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title") String title,
                                       @RequestParam(name = "description") String description,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock") Integer stock,
                                       @RequestParam(name = "imgUrl") String imgUrl)throws BusinessException {
        // 封装service请求用来创建商品,创建商品跟销量无关
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemModelReturn = itemService.createItem(itemModel);
        ItemVo itemVo=convertVoFromModel(itemModelReturn);
        return CommonReturnType.create(itemVo);
    }

    private ItemVo convertVoFromModel(ItemModel itemModel) {
        if(itemModel==null){
            return null;
        }
        ItemVo itemVo = new ItemVo();
        BeanUtils.copyProperties(itemModel, itemVo);
        if (itemModel.getPromoModel() != null) {
            itemVo.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVo.setPromoId(itemModel.getPromoModel().getId());
            itemVo.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVo.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            itemVo.setPromoStatus(0);
        }
        return itemVo;
    }

    /**
     * 获取商品
     * @param id
     * @return
     */
    @GetMapping("/getItem")
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id") String id) {
        ItemModel itemModel=null;
        // 先取本地缓存
        itemModel=(ItemModel)cacheService.getFromCommonCache("item_"+id);
        // 本地缓存为null
        if(itemModel==null){
            // 根据商品的id到缓存获取
            itemModel=(ItemModel) redisTemplate.opsForValue().get("item_"+id);
            // 从redis获取为null
            if(itemModel==null){
                itemModel = itemService.getItemById(id);
                redisTemplate.opsForValue().set("item_"+id,itemModel);
            }
            cacheService.setCommonCache("item_"+id,itemModel);
        }

        ItemVo itemVo = convertVoFromModel(itemModel);
        return CommonReturnType.create(itemVo);
    }

    /**
     * 商品列表
     * @return
     */
    @GetMapping("/list")
    public CommonReturnType listItem(){
        List<ItemModel> itemModelList = itemService.listItem();
        List<ItemVo> itemVoList=itemModelList.stream().map(itemModel -> {
            ItemVo itemVo=this.convertVoFromModel(itemModel);
            return itemVo;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVoList);
    }

    @GetMapping(value="/publicPromo")
    @ResponseBody
    public CommonReturnType publicPromo(@RequestParam String promoId){
        promoService.publishPromo(promoId);
        return CommonReturnType.create(null);
    }
}
