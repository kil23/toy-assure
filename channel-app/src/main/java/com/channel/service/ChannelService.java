package com.channel.service;

import com.channel.dao.ChannelDao;
import com.channel.model.response.ChannelOrdersDataResponse;
import com.channel.pojo.Channel;
import com.channel.pojo.ChannelListing;
import com.channel.socket.Order;
import com.channel.socket.OrderItems;
import com.commons.form.ChannelForm;
import com.commons.response.ChannelDataResponse;
import com.commons.response.OrderDataResponse;
import com.commons.response.OrderItemDataResponse;
import com.commons.service.ApiException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.commons.util.Validation.validateChannelName;

@Service
public class ChannelService {

    private static final Logger logger = Logger.getLogger(ChannelService.class);

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private ChannelListingService listingService;

    public static Channel convertFormToPojo(ChannelForm formData) {
        Channel channel = new Channel();
        channel.setName(formData.getName());
        channel.setInvoiceType(formData.getType());
        return channel;
    }

    public static Channel convertFormToPojo(ChannelForm formData, Long channelId) {
        Channel channel = new Channel();
        channel.setId(channelId);
        channel.setName(formData.getName());
        channel.setInvoiceType(formData.getType());
        return channel;
    }

    public static ChannelDataResponse convertPojoToFormData(Channel channel) {
        ChannelDataResponse data = new ChannelDataResponse();
        data.setId(channel.getId());
        data.setName(channel.getName());
        data.setType(channel.getInvoiceType());
        return data;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void addChannel(ChannelForm formData) throws ApiException {
        logger.info("add-client");
        Channel channelToInsert = convertFormToPojo(formData);
        validateChannelName(channelToInsert.getName());
        Channel channel = channelDao.getChannelDetailsByChannelName(channelToInsert.getName());
        if(channel==null){
            logger.info("calling insert method now...");
            channelDao.insertChannel(channelToInsert);
        }else{
            logger.info("name cannot be duplicate");
            throw new ApiException("Duplicate name found while creating New channel");
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public void updateChannel(ChannelForm formData, Long channelId) throws ApiException {
        logger.info("update-client");
        Channel channelToInsert = convertFormToPojo(formData, channelId);
        validateChannelName(channelToInsert.getName());
        Channel cp = channelDao.getChannelDetailsById(channelToInsert.getId());
        if (cp == null) {
            logger.info("No Channel found with given id.");
            throw new ApiException("Channel with given Id does not exit, id: " + channelToInsert.getId());
        }else {
            Channel channel = channelDao.getChannelDetailsByChannelName(channelToInsert.getName());
            if(channel==null){
                logger.info("calling update method now...");
                channelDao.updateChannel(channelToInsert);
            }else{
                logger.info("channel-name cannot be duplicate");
                throw new ApiException("Duplicate name found while updating Channel Name");
            }
        }
    }

    public ChannelDataResponse getChannelDetails(Long channelId) throws ApiException {
        logger.info("get-channel-details");
        Channel channel = channelDao.getChannelDetailsById(channelId);
        if(channel==null){
            logger.info("No Channel found with channel-id : "+channelId);
            throw new ApiException("No Channel found.");
        }
        return convertPojoToFormData(channel);
    }

    public Channel getChannelById(Long channelId) throws ApiException {
        logger.info("get-channel-by-id");
        Channel channel = channelDao.getChannelDetailsById(channelId);
        if(channel==null){
            logger.info("No Channel data found with channel-id : "+channelId);
            throw new ApiException("No Channel found. Please add channel first.");
        }
        return channel;
    }

    public List<ChannelDataResponse> getAllChannelDetails() throws ApiException {
        logger.info("get-all-channel-data");
        List<Channel> channelList = channelDao.getAllChannelDetails();
        if(channelList.isEmpty()){
            logger.info("No Channel data found. Please create Channel first.");
            throw new ApiException("No Channel data found. Please create Channel first.");
        }
        List<ChannelDataResponse> list2 = new ArrayList<>();
        for (Channel channel : channelList) {
            list2.add(convertPojoToFormData(channel));
        }
        return list2;
    }

    public List<ChannelDataResponse> getAllChannelDetails(Long clientId) throws ApiException {
        logger.info("get-channel-listing-by-client-Id");
        Set<Long> channelIdSet = listingService.getUniqueChannelIdByClientId(clientId);
        logger.info("Distinct listings : "+channelIdSet.size());
        logger.info("get-All-channels-by-clientId");
        List<Channel> list1 = new ArrayList<>();
        for(Long channelId : channelIdSet){
            Channel channel = channelDao.getChannelDetailsById(channelId);
            list1.add(channel);
        }
        if(list1.isEmpty()){
            logger.info("No channel-listing added yet");
            throw new ApiException("No Channel-Items found for selected client. Please add Channel-items first.");
        }else{
            List<ChannelDataResponse> list2 = new ArrayList<>();
            for (Channel channel : list1) {
                list2.add(convertPojoToFormData(channel));
            }
            return list2;
        }
    }

    public List<ChannelOrdersDataResponse> getAllChannelOrderList() {

        List<ChannelOrdersDataResponse> ordersDataList = new ArrayList<>();

        logger.info("get-all-channel-order-list");
        // Getting list of all channels
        List<Channel> channelList = channelDao.getAllChannelDetails();
        // Removing Internal channel for the channel-list
        channelList.remove(0);
        logger.info("Total-channels : "+channelList.size());
        if(channelList.size()!=0){
            ordersDataList = getChannelOrderData(ordersDataList, channelList);
        }
        logger.info("No Channels found.");
        return ordersDataList;
    }

    public List<ChannelOrdersDataResponse> getChannelOrderData(List<ChannelOrdersDataResponse> ordersDataList, List<Channel> channelList) {
        // Traversing through all channels
        for(Channel channel : channelList){
            logger.info("Channel: "+ channel.getName() + " and id : "+channel.getId());
            // Getting list of all the orders related to specific channel-id
            List<OrderDataResponse> orderList = Order.getOrderDetails(channel.getId());
            if(orderList==null){
                logger.info("No orders found for channel with id : "+ channel.getId());
            }else{
                logger.info("Order-list-size : "+orderList.size());
                // Traversing through all the orders for that channel-id
                for(OrderDataResponse order : orderList){
                    logger.info("Order id : "+order.getOrderId());
                    // Getting list of all the order-items for the given order.
                    List<OrderItemDataResponse> orderItemList = OrderItems.getOrderItemDetails(order.getOrderId());
                    if(orderItemList==null){
                        logger.info("No Order-items found for given order id : "+order.getOrderId());
                    }else{
                        for(OrderItemDataResponse itemData : orderItemList){
                            // Getting Channel-listing for given order-item's globalSkuId.
                            ChannelListing listing = listingService.getChannelListing(itemData.getGlobalSkuId(), order.getChannelId());
                            Long qty = itemData.getOrderedQuantity();
                            if(qty>0){
                                ChannelOrdersDataResponse orderData = new ChannelOrdersDataResponse();
                                orderData.setClientName(order.getClientName());
                                orderData.setChannelName(channel.getName());
                                orderData.setCustomerId(order.getCustomerId());
                                orderData.setChannelOrderId(order.getChannelOrderId());
                                orderData.setChannelItem(listing.getChannelSkuId());
                                orderData.setQuantity(qty);
                                orderData.setStatus(order.getStatus());
                                logger.info(Collections.singletonList(orderData));
                                ordersDataList.add(orderData);
                                logger.info("Orders List : "+ordersDataList.size());
                            }
                        }
                        logger.info("OrderData-list size : "+ordersDataList.size());
                    }
                }
            }
        }
        return ordersDataList;
    }
}
