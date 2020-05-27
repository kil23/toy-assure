package com.channel.dto;

import com.channel.api.ChannelApi;
import com.channel.api.ChannelListingApi;
import com.channel.client.ClientClient;
import com.channel.client.InvoicePdfClient;
import com.channel.client.OrderClient;
import com.channel.client.OrderItemClient;
import com.channel.model.form.AddChannelForm;
import com.channel.pojo.Channel;
import com.channel.pojo.ChannelListing;
import com.commons.form.ChannelForm;
import com.commons.form.ChannelOrderForm;
import com.commons.response.*;
import com.commons.service.ApiException;
import com.commons.util.ConvertUtil;
import com.commons.util.PDFHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Component
public class ChannelDto {

    private static final Logger logger = Logger.getLogger(ChannelDto.class);
    private static final String PDF_PATH = "src/main/resources/com/channel/";
    private static final String INVOICE_TEMPLATE_XSL = "src/main/resources/com/channel/invoiceTemplate.xsl";

    @Autowired private ChannelApi channelApi;
    @Autowired private ChannelListingApi channelListingApi;
    @Autowired private OrderClient orderClient;
    @Autowired private ClientClient clientClient;
    @Autowired private InvoicePdfClient pdfClient;
    @Autowired private OrderItemClient itemClient;

    @Transactional
    public void addChannel(AddChannelForm form) {
        for(ChannelForm formData : form.getChannelList()){
            Channel channel = ConvertUtil.convert(formData, Channel.class);
            if (channel != null) {
                channelApi.addChannel(channel);
            }
        }
    }

    @Transactional
    public void updateChannel(ChannelForm formData, Long id) {
        Channel channel = ConvertUtil.convert(formData, Channel.class);
        if (channel != null) {
            channel.setId(id);
            if (channelApi.getChannelById(channel.getId()) == null) {
                throw new ApiException("Channel with given Id does not exit, id: " + channel.getId());
            }
            if(channelApi.getChannelDetails(channel.getName()) != null){
                throw new ApiException("Duplicate name found while updating Channel Name");
            }
            channelApi.updateChannel(channel);
        }
    }

    @Transactional(readOnly = true)
    public ChannelDataResponse getChannelDetails(Long id) {
        Channel channel = channelApi.getChannelById(id);
        if(channel==null){
            throw new ApiException("No Channel found. Please add channel first.");
        }
        return ConvertUtil.convert(channel, ChannelDataResponse.class);
    }

    @Transactional(readOnly = true)
    public List<ChannelDataResponse> getAllChannelDetails(Long clientId) {
        Set<Long> channelIdSet = channelListingApi.getUniqueChannelIdByClientId(clientId);
        List<Channel> channelList = new ArrayList<>();
        for(Long channelId : channelIdSet){
            Channel channel = channelApi.getChannelById(channelId);
            if(channel==null){
                continue;
            }
            channelList.add(channel);
        }
        if(channelList.isEmpty()){
            throw new ApiException("No Channel-Items found for selected client. Please add Channel-items first.");
        }
        return ConvertUtil.convert(channelList, ChannelDataResponse.class);
    }

    @Transactional(readOnly = true)
    public List<ChannelDataResponse> getAllChannelDetails() {
        List<Channel> channelList = channelApi.getAllChannelDetails();
        if(channelList.isEmpty()){
            throw new ApiException("No Channel data found. Please create Channel first.");
        }
        return ConvertUtil.convert(channelList, ChannelDataResponse.class);
    }

    public void postOrderDetails(ChannelOrderForm formData) {
        orderClient.postOrderDetails(formData);
    }

    @Transactional(readOnly = true)
    public List<OrderDataResponse> getAllChannelOrderList(Long channelId) {
        List<OrderDataResponse> ordersDataList;
        ordersDataList = orderClient.getOrderDetails(channelId);
        return ordersDataList;
    }

    @Transactional(readOnly = true)
    public List<OrderItemDataResponse> getChannelOrderItems(Long orderId, Long channelId){
        List<OrderItemDataResponse> itemResponseList;
        List<OrderItemDataResponse> itemList = itemClient.getOrderItemDetails(orderId);
        itemResponseList = ConvertUtil.convert(itemList, OrderItemDataResponse.class);
        if (itemResponseList != null) {
            for(int i=0; i<itemList.size(); i++){
                ChannelListing listing = channelListingApi.getChannelListing(itemList.get(i).getGlobalSkuId(), channelId);
                itemResponseList.get(i).setSkuId(listing.getChannelSkuId());
            }
        }
        return itemResponseList;
    }

    public void generatePdf(OrderDataResponse orderData) {
        logger.info("generate here");
        List<InvoiceOrderItemData> invoiceItemList = itemClient.getOrderItemDataForInvoice(orderData.getId());
        logger.info("here list : "+invoiceItemList.size());
        InvoiceData data = populateInvoiceData(orderData, invoiceItemList);
        logger.info("here data "+data);
        pdfCreator(data);
    }

    private InvoiceData populateInvoiceData(OrderDataResponse order, List<InvoiceOrderItemData> invoiceItemList) {
        InvoiceData data = new InvoiceData();
        InvoiceOrderData orderData = new InvoiceOrderData();
        orderData.setOrderId(order.getId());
        orderData.setChannelOrderId(order.getChannelOrderId());
        orderData.setClientName(order.getClientName());
        orderData.setCustomerName(order.getCustomerName());
        orderData.setChannelId(order.getChannelId());
        data.setOrderData(orderData);
        data.setInvoiceOrderItemDataList(invoiceItemList);
        logger.info("populate here "+ data.getInvoiceOrderItemDataList().size());
        logger.info("populate here chOr " + orderData.getChannelOrderId());
        logger.info("populate here clN "+ orderData.getClientName());
        logger.info("populate here cuN "+ orderData.getCustomerName());
        logger.info("populate here oID "+ orderData.getOrderId());
        return data;
    }

    public void pdfCreator(InvoiceData data) {
        logger.info("pdfcreator here");
        try {
            PDFHandler.createInvoicePdf(data,PDF_PATH, INVOICE_TEMPLATE_XSL);
            logger.info("pdf created here");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getClass());
        }
    }

    @Transactional
    public byte[] getPDfInBytes(String fileName) throws IOException {
        byte[] fileInBytes;
        File file = new File(String.valueOf(Paths.get(PDF_PATH+fileName+".pdf")));
        if(!(file.exists() && file.isFile())) {
            byte[] clientByteResponse = pdfClient.getInvoicePDF(fileName);
            if(clientByteResponse == null) {
                throw new ApiException("Invoice Pdf not found with name : "+ fileName);
            }
            logger.info("got pdf "+ clientByteResponse.length);
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(clientByteResponse);
//            BufferedOutputStream bos = new BufferedOutputStream(fos);
//            bos.write(clientByteResponse);
//            bos.flush();
//            fos.close();
//            logger.info(file.exists());
            return Base64.getEncoder().encode(clientByteResponse);
        }
        fileInBytes = Files.readAllBytes(Paths.get(PDF_PATH+fileName+".pdf"));
        logger.info(fileInBytes.length);
        return Base64.getEncoder().encode(fileInBytes);
    }

    @Transactional(readOnly = true)
    public List<ClientDataResponse> getAllCustomers() {
        return clientClient.getCustomerDetails();
    }

    @Transactional(readOnly = true)
    public List<ClientDataResponse> getAllClients() {
        return clientClient.getClientDetails();
    }

    @Transactional(readOnly = true)
    public List<OrderDataResponse> getOrderDataForClient(long clientId) {
        List<OrderDataResponse> allOrderDetails = orderClient.getAllOrderDetails(clientId);
        List<OrderDataResponse> responses = new ArrayList<>();
        for(OrderDataResponse orderData : allOrderDetails){
            if(!orderData.getChannelName().equals("INTERNAL")){
                responses.add(orderData);
            }
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public Channel getChannelByName(String clientName) {
        return channelApi.getChannelDetails(clientName);
    }

    @Transactional(readOnly = true)
    public Channel getChannelDetail(Long channelId) {
        return channelApi.getChannelById(channelId);
    }
}
