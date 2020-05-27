package com.commons.util;

import com.commons.response.InvoiceData;
import com.commons.response.InvoiceOrderItemData;
import com.commons.service.ApiException;
import org.apache.fop.afp.util.StringUtils;
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
import java.io.*;
import java.time.LocalDate;

public class PDFHandler {

//    public static final String OUTPUT_DIR = "src/main/resources/com/assure/";

    public static void createInvoicePdf(InvoiceData invoiceData, String OUTPUT_DIR,
                                        String INVOICE_TEMPLATE_XSL) throws ParserConfigurationException,
                                        TransformerException, FOPException, IOException {

        if (invoiceData == null) {
            throw new ApiException("Order has no items so pdf cannot be created");
        }
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
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
            quantity.appendChild(document.createTextNode(Long.toString(data.getOrderedQuantity())));
            product.appendChild(quantity);
            Element sellingPricePerUnit = document.createElement("sellingPricePerUnit");
            sellingPricePerUnit.appendChild(document.createTextNode(Double.toString(data.getSellingPricePerUnit())));
            product.appendChild(sellingPricePerUnit);
            Element totalPrice = document.createElement("totalPrice");
            totalPrice.appendChild(document.createTextNode(Double.toString(data.getOrderedQuantity() * data.getSellingPricePerUnit())));
            product.appendChild(totalPrice);
            sum += (data.getSellingPricePerUnit() * data.getOrderedQuantity());
            sno++;
        }
        Element totalPrice = document.createElement("totalAmount");
        totalPrice.appendChild(document.createTextNode(Double.toString(sum)));
        root.appendChild(totalPrice);
        Element orderId = document.createElement("ID");
        String invoiceId = "OMS"
                + StringUtils.lpad(""+ invoiceData.getOrderData().getChannelId(),'0',3)
                + "-" + StringUtils.lpad(""+invoiceData.getOrderData().getOrderId(),'0',4);
        orderId.appendChild(document.createTextNode(invoiceId));
        root.appendChild(orderId);

        Element clientName = document.createElement("clientName");
        clientName.appendChild(document.createTextNode(invoiceData.getOrderData().getClientName()));
        root.appendChild(clientName);
        Element customerName = document.createElement("customerName");
        customerName.appendChild(document.createTextNode(invoiceData.getOrderData().getCustomerName()));
        root.appendChild(customerName);

        Element createDate = document.createElement("createDate");
        createDate.appendChild(document.createTextNode(LocalDate.now().toString()));
        root.appendChild(createDate);

        File file = new File(OUTPUT_DIR+"order"+invoiceData.getOrderData().getOrderId()+".pdf");
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);

        ByteArrayOutputStream xmlBaos = new ByteArrayOutputStream();
        StreamResult streamResult = new StreamResult(xmlBaos);
        transformer.transform(domSource, streamResult);

        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        ByteArrayOutputStream pdfBaos = new ByteArrayOutputStream();
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, pdfBaos);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer2 = factory.newTransformer(new StreamSource(INVOICE_TEMPLATE_XSL));
        Result res = new SAXResult(fop.getDefaultHandler());

        Source src = new StreamSource(new ByteArrayInputStream(xmlBaos.toByteArray()));

        transformer2.transform(src, res);
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(pdfBaos.toByteArray());
    }
}