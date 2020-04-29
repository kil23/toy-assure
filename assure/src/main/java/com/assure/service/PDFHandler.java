package com.assure.service;

import com.assure.model.response.InvoiceOrderItemData;
import com.assure.model.response.InvoiceData;
import com.commons.service.ApiException;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDate;

public class PDFHandler {

    private static final String styleSheetTemplate = "src/main/resources/com/assure/invoiceTemplate.xsl";
    public static final String OUTPUT_DIR = "src//main//resources//com//assure//";

    public static byte[] createInvoicePdf(InvoiceData invoiceData) throws ApiException {

        if (invoiceData == null) {
            throw new ApiException("Order has no items so pdf cannot be created");
        }
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ignored) {
        }
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("InvoiceData");
        document.appendChild(root);
        double sum = 0.0;
        int sno = 1;
        for (InvoiceOrderItemData data : invoiceData.getInvoiceOrderItemDataList()) {
            Element product = document.createElement("invoice");
            root.appendChild(product);
            Element count = document.createElement("sno");
            count.appendChild(document.createTextNode(Integer.toString(sno)));
            product.appendChild(count);
            Element brandName = document.createElement("brandId");
            brandName.appendChild(document.createTextNode(data.getBrandId()));
            product.appendChild(brandName);
            Element productName = document.createElement("name");
            productName.appendChild(document.createTextNode(data.getProductName()));
            product.appendChild(productName);
            Element quantity = document.createElement("orderedQuantity");
            quantity.appendChild(document.createTextNode(Long.toString(data.getFulfilledQuantity())));
            product.appendChild(quantity);
            Element sellingPricePerUnit = document.createElement("sellingPricePerUnit");
            sellingPricePerUnit.appendChild(document.createTextNode(Double.toString(data.getSellingPricePerUnit())));
            product.appendChild(sellingPricePerUnit);
            Element totalPrice = document.createElement("totalPrice");
            totalPrice.appendChild(document.createTextNode(Double.toString(data.getFulfilledQuantity() * data.getSellingPricePerUnit())));
            product.appendChild(totalPrice);
            sum += (data.getSellingPricePerUnit() * data.getFulfilledQuantity());
            sno++;
        }
        Element totalPrice = document.createElement("totalAmount");
        totalPrice.appendChild(document.createTextNode(Double.toString(sum)));
        root.appendChild(totalPrice);

        Element orderId = document.createElement("ID");
        orderId.appendChild(document.createTextNode(invoiceData.getOrder().getChannelOrderId()));
        root.appendChild(orderId);

        Element clientName = document.createElement("clientName");
        clientName.appendChild(document.createTextNode(invoiceData.getOrder().getClientId().toString()));
        root.appendChild(clientName);

//        Element customerName = document.createElement("customerName");
//        customerName.appendChild(document.createTextNode(invoiceData.getOrder().getCustomerId()));
//        root.appendChild(customerName);

        Element createDate = document.createElement("createDate");
        createDate.appendChild(document.createTextNode(LocalDate.now().toString()));
        root.appendChild(createDate);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        DOMSource domSource = new DOMSource(document);

        ByteArrayOutputStream xmlBaos = new ByteArrayOutputStream();
        StreamResult streamResult = new StreamResult(xmlBaos);
        try {
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        ByteArrayOutputStream pdfBaos = new ByteArrayOutputStream();
        Fop fop = null;
        try {
            fop = fopFactory.newFop(MimeConstants.MIME_PDF, pdfBaos);
        } catch (FOPException e) {
            e.printStackTrace();
        }
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer2 = null;
        try {
            transformer2 = factory.newTransformer(new StreamSource(styleSheetTemplate));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        Result res = null;
        try {
            res = new SAXResult(fop.getDefaultHandler());
        } catch (FOPException e) {
            e.printStackTrace();
        }
        Source src = new StreamSource(new ByteArrayInputStream(xmlBaos.toByteArray()));
        try {
            transformer2.transform(src, res);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return pdfBaos.toByteArray();
    }
}