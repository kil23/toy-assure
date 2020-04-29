package com.assure.service;

import com.assure.dao.OrderItemDao;
import com.assure.pojo.Order;
import com.assure.pojo.OrderItem;
import com.assure.pojo.Product;
import com.assure.model.response.OrderItemListingDataResponse;
import com.commons.form.OrderForm;
import com.commons.response.OrderItemDataResponse;
import com.commons.service.ApiException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemService {

    private static final Logger logger = Logger.getLogger(OrderItemService.class);

    @Autowired
    private OrderItemDao itemDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    public OrderItem convertFormToOrderItemPojo(OrderForm form, Long globalSkuId, Long orderId) throws ApiException {
        Product product = productService.getProductById(globalSkuId);
        Order order = orderService.getOrderById(orderId);
        OrderItem orderItem = new OrderItem();
        orderItem.setGlobalSkuId(product.getGlobalSkuId());
        orderItem.setOrderedQuantity(form.getOrderedQuantity());
        orderItem.setSellingPricePerUnit(form.getSellingPricePerUnit());
        orderItem.setOrderId(order.getId());
        return orderItem;
    }

    public OrderItem convertFormToOrderItemPojo(OrderForm form, double sellingPricePerUnit, Long orderId) throws ApiException {
        Product product = productService.getProductById(form.getGlobalSkuId());
        Order order = orderService.getOrderById(orderId);
        OrderItem orderItem = new OrderItem();
        orderItem.setGlobalSkuId(product.getGlobalSkuId());
        orderItem.setOrderedQuantity(form.getOrderedQuantity());
        orderItem.setSellingPricePerUnit(sellingPricePerUnit);
        orderItem.setOrderId(order.getId());
        return orderItem;
    }

    public OrderItemListingDataResponse convertPojoToFormData(OrderItem itemPojo, Product product) {
        OrderItemListingDataResponse itemData = new OrderItemListingDataResponse();
        itemData.setGlobalSkuId(itemPojo.getGlobalSkuId());
        itemData.setOrderedQuantity(itemPojo.getOrderedQuantity());
        itemData.setAllocatedQuantity(itemPojo.getAllocatedQuantity());
        itemData.setFulfilledQuantity(itemPojo.getFulfilledQuantity());
        itemData.setSellingPricePerUnit(itemPojo.getSellingPricePerUnit());
        itemData.setClientSkuId(product.getClientSkuId());
        itemData.setName(product.getName());
        itemData.setBrandId(product.getBrandId());
        return itemData;
    }

    public static OrderItemDataResponse convertPojoToFormData(OrderItem itemPojo) {
        OrderItemDataResponse itemData = new OrderItemDataResponse();
        itemData.setGlobalSkuId(itemPojo.getGlobalSkuId());
        itemData.setOrderedQuantity(itemPojo.getOrderedQuantity());
        return itemData;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void insertOrderItems( OrderItem pojo) {
        logger.info("Inserting Order item with orderId : "+ pojo.getOrderId() + " and GlobalSkuId : "+pojo.getGlobalSkuId());
        itemDao.insertOrderItem(pojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<OrderItem> getOrderItemByOrderId(Long orderId) throws ApiException {
        logger.info("Order-items-by-orderId");
        List<OrderItem> list1 = itemDao.getOrderItemListByOrderId(orderId);
        if(list1.size()==0){
            logger.info("No order items found for order id : "+orderId);
            throw new ApiException("No order items found.");
        }else{
            return list1;
        }
    }

    public void updateOrderItem(OrderItem orderItem) {
        logger.info("update-order-items");
        itemDao.updateOrderItem(orderItem);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<OrderItemListingDataResponse> getAllOrderItemsListingData(Long orderId) throws ApiException {
        logger.info("get-all-order-items");
        List<OrderItem> list1 = itemDao.getOrderItemListByOrderId(orderId);
        List<OrderItemListingDataResponse> list2 = new ArrayList<>();
        if(list1.isEmpty()){
            logger.info("No Order items found.");
            throw new ApiException("No Order items found.");
        }else{
            for (OrderItem itemPojo : list1) {
                Product product = productService.getProductById(itemPojo.getGlobalSkuId());
                list2.add(convertPojoToFormData(itemPojo, product));
            }
            return list2;
        }
    }

    public List<OrderItemDataResponse> getAllOrderItemsData(Long orderId) {
        logger.info("get-all-order-items-by-order-id");
        List<OrderItem> pojoList = itemDao.getOrderItemListByOrderId(orderId);
        if(pojoList.isEmpty()){
            logger.info("No Order item found for orderId("+orderId+")");
            return null;
        }else{
            List<OrderItemDataResponse> list = new ArrayList<>();
            for(OrderItem item : pojoList){
                list.add(convertPojoToFormData(item));
            }
            return list;
        }
    }
}
