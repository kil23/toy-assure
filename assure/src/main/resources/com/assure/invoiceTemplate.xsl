<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <!-- Attribute used for table border -->
    <xsl:attribute-set name="tableBorder">
        <xsl:attribute name="border">solid 0.1mm black</xsl:attribute>
    </xsl:attribute-set>
    <xsl:template match="InvoiceData">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simpleA4"
                                       page-height="29.7cm" page-width="25.0cm" margin="1cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simpleA4">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block>
                        <fo:external-graphic
                            src="C:\\Users\\Lenovo\\Downloads\\employee-spring\\assure\\src\\main\\webapp\\static\\logo.png"
                            content-height="1.5cm" content-width="4.5cm" display-align="left"></fo:external-graphic>
                    </fo:block>
                    <fo:block font-size="10pt" text-align="left" font-family="Helvetica" space-after="2mm">
                        Invoice ID:
                        <xsl:value-of select="ID"/>
                    </fo:block>
                    <fo:block font-size="10pt" text-align="left" font-family="Helvetica" space-after="2mm">
                        Client :
                        <xsl:value-of select="clientName"/>
                    </fo:block>
<!--                    <fo:block font-size="10pt" text-align="left" font-family="Helvetica" space-after="2mm">-->
<!--                        Customer :-->
<!--                        <xsl:value-of select="customerName"/>-->
<!--                    </fo:block>-->
                    <fo:block font-size="10pt" text-align="left" font-family="Helvetica" space-after="2mm">
                        Date :
                        <xsl:value-of select="createDate"/>
                    </fo:block>
                    <fo:block font-size="10pt">
                        <fo:table table-layout="fixed" width="100%" border-collapse="separate">
                            <fo:table-column column-width="2cm"/>
                            <fo:table-column column-width="3cm"/>
                            <fo:table-column column-width="5cm"/>
                            <fo:table-column column-width="3cm"/>
                            <fo:table-column column-width="5cm"/>
                            <fo:table-column column-width="5cm"/>
                            <fo:table-header font-weight="bold">
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">#</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">Brand</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">Product</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">Quantity
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">MRP</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">Total Price
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-header>
                            <fo:table-body>
                                <xsl:apply-templates
                                        select="invoice"/> <!-- branch tag is taken and pasted in below used template 46 line-->
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                    <fo:block text-align="right" font-size="15pt" font-family="Helvetica" color="black"
                              font-weight="bold" space-after="5mm">
                        Total Amount : Rs.
                        <xsl:value-of select="totalAmount"/> /-
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="invoice">
        <fo:table-row space-after="5mm">
            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="sno"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="brandId"/>
                </fo:block>
            </fo:table-cell>

            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="name"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="orderedQuantity"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="sellingPricePerUnit"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="totalPrice"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>
</xsl:stylesheet>