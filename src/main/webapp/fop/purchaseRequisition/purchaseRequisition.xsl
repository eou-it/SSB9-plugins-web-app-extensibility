<?xml version="1.0" encoding="UTF-16"?>
<!-- *******************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************-->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:exsl="http://exslt.org/common"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                exclude-result-prefixes="exsl fo">

    <xsl:include href="../common/common-config.xsl"/>
    <xsl:include href="purchaseRequisition-styles.xsl"/>
    <xsl:include href="purchaseRequisition-styles-custom.xsl"/>
    <xsl:template match="pdfModel">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="requisition-page"
                                       xsl:use-attribute-sets="page">
                    <fo:region-body xsl:use-attribute-sets="body-region"/>
                    <fo:region-before xsl:use-attribute-sets="header-region"/>
                    <fo:region-after xsl:use-attribute-sets="footer-region"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="requisition-page">
                <xsl:if test="exsl:node-set($config)/logoTopBottom = 'TOP'">

                    <fo:static-content flow-name="xsl-region-before">
                        <fo:block-container xsl:use-attribute-sets="container">
                            <fo:block>
                                <fo:table xsl:use-attribute-sets="procurement-table">
                                    <fo:table-body>
                                        <fo:table-row>
                                            <fo:table-cell>
                                                <fo:block>
                                                    <xsl:if test="(exsl:node-set($config)/logoLeftRight = 'LEFT'
                                                        and $base-writing-mode = 'lr')
                                                        or (exsl:node-set($config)/logoLeftRight = 'RIGHT'
                                                        and $base-writing-mode = 'rl')">
                                                        <fo:external-graphic xsl:use-attribute-sets="procurement-logo"/>
                                                    </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </fo:table-body>
                                </fo:table>
                            </fo:block>
                        </fo:block-container>
                    </fo:static-content>
                </xsl:if>
                <fo:flow flow-name="xsl-region-body">
                    <fo:block-container xsl:use-attribute-sets="container">
                        <fo:block text-align='center'>
                            <xsl:value-of select="exsl:node-set($labels)/title"/>
                        </fo:block>
                    </fo:block-container>
                    <fo:block-container xsl:use-attribute-sets="container">
                        <fo:block text-align='center'>
                            <xsl:value-of select="exsl:node-set($labels)/subtitle"/>
                        </fo:block>
                    </fo:block-container>
                    <fo:block xsl:use-attribute-sets="line-break">&#160;
                    </fo:block>
                    <fo:block-container xsl:use-attribute-sets="container">
                        <fo:table xsl:use-attribute-sets="table">
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/requestor"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/requesterName"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/requisitionNo"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/requestCode"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/phone"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/requester/phoneArea"/>-
                                            <xsl:value-of select="requisition/header/requester/requesterPhoneNumber"/>&#160;
                                            <xsl:value-of select="exsl:node-set($labels)/extension"/>&#160;
                                            <xsl:value-of select="requisition/header/requester/requesterPhoneExt"/>

                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/transactionDate"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/transactionDate"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>

                                <fo:table-row>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/email"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:call-template name="zero_width_space">
                                                <xsl:with-param name="data" select="requisition/header/requesterEmailAddress"/>
                                            </xsl:call-template>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/deliveryDate"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/deliveryDate"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/organization"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/organization/orgnTitle"/>&#160;
                                            <fo:bidi-override unicode-bidi="embed">
                                                (<xsl:value-of select="requisition/header/organization/orgnCode"/>)
                                            </fo:bidi-override>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/status"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/status"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>

                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/requisitionType"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:choose>
                                                <xsl:when test="requisition/header/isDocumentLevelAccounting='true'">
                                                    <xsl:value-of
                                                            select="exsl:node-set($labels)/documentRequisitionType"/>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:value-of
                                                            select="exsl:node-set($labels)/commodityRequisitionType"/>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/currency"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/ccy"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>
                    </fo:block-container>
                    <fo:block xsl:use-attribute-sets="line-break">&#160;
                    </fo:block>
                    <fo:block-container xsl:use-attribute-sets="container">
                        <fo:table xsl:use-attribute-sets="table">
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/shipTo"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/shipTo/shipCode"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/vendor"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/vendorLastName"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/address"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/shipTo/addressLine1"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/address"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/vendorAddressLine1"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>

                                <fo:table-row>
                                    <fo:table-cell>
                                        <fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/shipTo/addressLine2"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/vendorAddressLine2"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell>
                                        <fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/shipTo/addressLine3"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/vendorAddressLine3"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell>
                                        <fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/shipTo/city"/>&#160;
                                            <xsl:value-of select="requisition/header/shipTo/state"/>&#160;
                                            <xsl:value-of select="requisition/header/shipTo/zipCode"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/vendorAddressCity"/>&#160;
                                            <xsl:value-of select="requisition/header/vendorAddressStateCode"/>&#160;
                                            <xsl:value-of select="requisition/header/vendorAddressZipCode"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/attentionTo"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/attentionTo"/> &#160;
                                            <xsl:value-of select="requisition/header/shipTo/phoneArea"/>-
                                            <xsl:value-of select="requisition/header/shipTo/phoneNumber"/>&#160;
                                            <xsl:value-of select="exsl:node-set($labels)/extension"/>&#160;
                                            <xsl:value-of select="requisition/header/shipTo/phoneExtension"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/phone"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="margin">
                                            <xsl:value-of select="requisition/header/vendorPhoneArea"/>-
                                            <xsl:value-of select="requisition/header/vendorPhoneNumber"/>&#160;
                                            <xsl:value-of select="exsl:node-set($labels)/vendorPhoneFax"/>&#160;
                                            <xsl:value-of select="requisition/header/vendorPhoneArea"/>-
                                            <xsl:value-of select="requisition/header/vendorPhoneFax"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>

                                <fo:table-row>
                                    <fo:table-cell>
                                        <fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/vendorEmail"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block  xsl:use-attribute-sets="margin">
                                            <xsl:call-template name="zero_width_space">
                                                <xsl:with-param name="data" select="requisition/header/vendorEmailAddress"/>
                                            </xsl:call-template>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>

                            </fo:table-body>
                        </fo:table>
                    </fo:block-container>
                    <fo:block xsl:use-attribute-sets="line-break">&#160;
                    </fo:block>
                    <fo:block-container xsl:use-attribute-sets="container">
                        <fo:table xsl:use-attribute-sets="table">
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="table-column-header">
                                            <xsl:value-of select="exsl:node-set($labels)/headerComment"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                        <fo:block xsl:use-attribute-sets="preserve-line-and-space">
                                            <xsl:value-of select="requisition/header/headerComment"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>

                    </fo:block-container>
                    <fo:block xsl:use-attribute-sets="line-break">&#160;
                    </fo:block>
                    <fo:block-container xsl:use-attribute-sets="container">
                        <fo:block>
                            <xsl:if test="requisition/header/isDocumentLevelAccounting='true'">
                                <xsl:value-of select="exsl:node-set($labels)/commodities"/>
                            </xsl:if>
                        </fo:block>
                    </fo:block-container>
                    <fo:block-container xsl:use-attribute-sets="container">
                        <xsl:choose>
                            <xsl:when test="requisition/header/isDocumentLevelAccounting='true'">
                                <fo:table xsl:use-attribute-sets="table">
                                    <fo:table-column xsl:use-attribute-sets="commotity-item-seq-column"/>
                                    <fo:table-column xsl:use-attribute-sets="commotity-item-desc-column"/>
                                    <fo:table-column xsl:use-attribute-sets="commotity-item-column"/>
                                    <fo:table-column xsl:use-attribute-sets="commotity-item-quantity-column"/>
                                    <fo:table-column xsl:use-attribute-sets="commotity-item-column"/>
                                    <fo:table-column xsl:use-attribute-sets="commotity-item-column"/>
                                    <fo:table-column xsl:use-attribute-sets="commotity-item-column"/>
                                    <fo:table-header>
                                        <fo:table-row>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                <fo:block xsl:use-attribute-sets="table-column-header"
                                                          text-align='center'>
                                                    <xsl:value-of select="exsl:node-set($labels)/commodityItem"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                <fo:block xsl:use-attribute-sets="table-column-header">
                                                    <xsl:value-of select="exsl:node-set($labels)/commodityDesc"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                <fo:block xsl:use-attribute-sets="table-column-header">
                                                    <xsl:value-of select="exsl:node-set($labels)/commoditityUOM"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                <fo:block xsl:use-attribute-sets="table-column-header numeric-align">
                                                    <xsl:value-of select="exsl:node-set($labels)/commodityQuantity"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                <fo:block xsl:use-attribute-sets="table-column-header numeric-align">
                                                    <xsl:value-of select="exsl:node-set($labels)/commodityUnitPrice"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                <fo:block xsl:use-attribute-sets="table-column-header numeric-align">
                                                    <xsl:value-of select="exsl:node-set($labels)/commodityOther"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                <fo:block xsl:use-attribute-sets="table-column-header numeric-align">
                                                    <xsl:value-of select="exsl:node-set($labels)/commodityTotal"/>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>

                                    </fo:table-header>
                                    <fo:table-body>
                                        <fo:table-row>
                                            <fo:table-cell>
                                                <fo:block>
                                                    <!-- Dummy Row -->
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                        <xsl:for-each select="requisition/commodity">
                                            <fo:table-row>
                                                <fo:table-cell xsl:use-attribute-sets="procurement-data"
                                                               text-align='center'>
                                                    <fo:block>
                                                        <xsl:value-of select="commodityItem"/>
                                                    </fo:block>
                                                </fo:table-cell>

                                                <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                    <fo:block>
                                                        <xsl:choose>
                                                            <xsl:when test="commodityCode=''">
                                                                <xsl:value-of select="commodityCodeDesc"/>
                                                            </xsl:when>
                                                            <xsl:otherwise>
                                                                <fo:bidi-override unicode-bidi="embed">
                                                                    <xsl:value-of
                                                                            select="commodityCodeDesc"/>(<xsl:value-of
                                                                        select="commodityCode"/>)
                                                                </fo:bidi-override>
                                                            </xsl:otherwise>
                                                        </xsl:choose>

                                                        <xsl:choose>
                                                            <xsl:when test="commodityText=''">
                                                            </xsl:when>
                                                            <xsl:otherwise>
                                                                <fo:block xsl:use-attribute-sets="procurement-data">
                                                                    <xsl:value-of
                                                                            select="exsl:node-set($labels)/commodityItemText"/>:
                                                                </fo:block>
                                                                <fo:block
                                                                        xsl:use-attribute-sets="preserve-line-and-space">
                                                                    <xsl:value-of select="commodityText"/>
                                                                </fo:block>
                                                            </xsl:otherwise>
                                                        </xsl:choose>
                                                    </fo:block>
                                                </fo:table-cell>

                                                <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                    <fo:block>
                                                        <xsl:value-of select="unitOfMeasure"/>
                                                    </fo:block>
                                                </fo:table-cell>

                                                <fo:table-cell xsl:use-attribute-sets="procurement-data numeric-align"
                                                >
                                                    <fo:block>
                                                        <xsl:value-of select="commodityQuantityDisplay"/>
                                                    </fo:block>
                                                </fo:table-cell>

                                                <fo:table-cell xsl:use-attribute-sets="procurement-data numeric-align"
                                                >
                                                    <fo:block>
                                                        <xsl:value-of select="commodityUnitPriceDisplay"/>
                                                    </fo:block>
                                                </fo:table-cell>

                                                <fo:table-cell xsl:use-attribute-sets="procurement-data numeric-align"
                                                >
                                                    <fo:block>
                                                        <xsl:value-of select="othersDisplay"/>
                                                    </fo:block>
                                                </fo:table-cell>

                                                <fo:table-cell xsl:use-attribute-sets="procurement-data numeric-align"
                                                >
                                                    <fo:block>
                                                        <xsl:value-of select="commodityTotalDisplay"/>
                                                    </fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                        </xsl:for-each>
                                    </fo:table-body>
                                </fo:table>
                            </xsl:when>
                            <xsl:otherwise>
                                <fo:block>
                                    <xsl:value-of select="dummy"/>
                                </fo:block>
                                <xsl:for-each select="requisition/commodity">
                                    <fo:table xsl:use-attribute-sets="table">
                                        <fo:table-column xsl:use-attribute-sets="commotity-item-seq-column"/>
                                        <fo:table-column xsl:use-attribute-sets="commotity-item-desc-column"/>
                                        <fo:table-column xsl:use-attribute-sets="commotity-item-column"/>
                                        <fo:table-column xsl:use-attribute-sets="commotity-item-quantity-column"/>
                                        <fo:table-column xsl:use-attribute-sets="commotity-item-column"/>
                                        <fo:table-column xsl:use-attribute-sets="commotity-item-column"/>
                                        <fo:table-column xsl:use-attribute-sets="commotity-item-column"/>
                                        <fo:table-header>
                                            <fo:table-row>
                                                <fo:table-cell number-columns-spanned="7"
                                                               xsl:use-attribute-sets="procurement-data">
                                                    <fo:block>
                                                        <xsl:value-of select="exsl:node-set($labels)/commodity"/>
                                                    </fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                            <fo:table-row>
                                                <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                    <fo:block xsl:use-attribute-sets="table-column-header"
                                                              text-align="center">
                                                        <xsl:value-of select="exsl:node-set($labels)/commodityItem"/>
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                    <fo:block xsl:use-attribute-sets="table-column-header">
                                                        <xsl:value-of select="exsl:node-set($labels)/commodityDesc"/>
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                    <fo:block xsl:use-attribute-sets="table-column-header">
                                                        <xsl:value-of select="exsl:node-set($labels)/commoditityUOM"/>
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                    <fo:block
                                                            xsl:use-attribute-sets="table-column-header numeric-align">
                                                        <xsl:value-of
                                                                select="exsl:node-set($labels)/commodityQuantity"/>
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                    <fo:block
                                                            xsl:use-attribute-sets="table-column-header numeric-align">
                                                        <xsl:value-of
                                                                select="exsl:node-set($labels)/commodityUnitPrice"/>
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                    <fo:block
                                                            xsl:use-attribute-sets="table-column-header numeric-align">
                                                        <xsl:value-of select="exsl:node-set($labels)/commodityOther"/>
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                    <fo:block
                                                            xsl:use-attribute-sets="table-column-header numeric-align">
                                                        <xsl:value-of select="exsl:node-set($labels)/commodityTotal"/>
                                                    </fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>

                                        </fo:table-header>
                                        <fo:table-body>
                                            <fo:table-row>
                                                <fo:table-cell>
                                                    <fo:block>
                                                        <!-- Dummy Row -->
                                                    </fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                            <fo:table-row>
                                                <fo:table-cell xsl:use-attribute-sets="procurement-data"
                                                               text-align="center">
                                                    <fo:block>
                                                        <xsl:value-of select="commodityItem"/>
                                                    </fo:block>
                                                </fo:table-cell>

                                                <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                    <fo:block>
                                                        <xsl:choose>
                                                            <xsl:when test="commodityCode=''">
                                                                <xsl:value-of select="commodityCodeDesc"/>
                                                            </xsl:when>
                                                            <xsl:otherwise>
                                                                <fo:bidi-override unicode-bidi="embed">
                                                                    <xsl:value-of
                                                                            select="commodityCodeDesc"/>(<xsl:value-of
                                                                        select="commodityCode"/>)
                                                                </fo:bidi-override>
                                                            </xsl:otherwise>
                                                        </xsl:choose>
                                                        <xsl:choose>
                                                            <xsl:when test="commodityText=''">
                                                            </xsl:when>
                                                            <xsl:otherwise>
                                                                <fo:block xsl:use-attribute-sets="procurement-data">
                                                                    <xsl:value-of
                                                                            select="exsl:node-set($labels)/commodityItemText"/>:
                                                                </fo:block>
                                                                <fo:block
                                                                        xsl:use-attribute-sets="preserve-line-and-space">
                                                                    <xsl:value-of select="commodityText"/>
                                                                </fo:block>
                                                            </xsl:otherwise>
                                                        </xsl:choose>
                                                    </fo:block>
                                                </fo:table-cell>

                                                <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                    <fo:block>
                                                        <xsl:value-of select="unitOfMeasure"/>
                                                    </fo:block>
                                                </fo:table-cell>

                                                <fo:table-cell xsl:use-attribute-sets="procurement-data numeric-align"
                                                >
                                                    <fo:block>
                                                        <xsl:value-of select="commodityQuantityDisplay"/>
                                                    </fo:block>
                                                </fo:table-cell>

                                                <fo:table-cell xsl:use-attribute-sets="procurement-data numeric-align"
                                                >
                                                    <fo:block>
                                                        <xsl:value-of select="commodityUnitPriceDisplay"/>
                                                    </fo:block>
                                                </fo:table-cell>

                                                <fo:table-cell xsl:use-attribute-sets="procurement-data numeric-align"
                                                >
                                                    <fo:block>
                                                        <xsl:value-of select="othersDisplay"/>
                                                    </fo:block>
                                                </fo:table-cell>

                                                <fo:table-cell xsl:use-attribute-sets="procurement-data numeric-align"
                                                >
                                                    <fo:block>
                                                        <xsl:value-of select="commodityTotalDisplay"/>
                                                    </fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                            <fo:table-row>
                                                <fo:table-cell number-columns-spanned="7"
                                                               xsl:use-attribute-sets="procurement-cla-data">
                                                    <fo:block xsl:use-attribute-sets="table-column-header">
                                                        &#160;&#160;<xsl:value-of
                                                            select="exsl:node-set($labels)/accountingDistribution"/>
                                                    </fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                            <fo:table-row>
                                                <fo:table-cell number-columns-spanned="7"
                                                               xsl:use-attribute-sets="procurement-data">
                                                    <fo:block>
                                                        <fo:table xsl:use-attribute-sets="table">
                                                            <fo:table-column
                                                                    xsl:use-attribute-sets="accounting-sequence-column"/>
                                                            <fo:table-column
                                                                    xsl:use-attribute-sets="accounting-data-column"/>
                                                            <fo:table-column
                                                                    xsl:use-attribute-sets="accounting-sequence-column"/>
                                                            <fo:table-column
                                                                    xsl:use-attribute-sets="accounting-sequence-column"/>
                                                            <fo:table-header>
                                                                <fo:table-row>
                                                                    <fo:table-cell
                                                                            xsl:use-attribute-sets="procurement-data">
                                                                        <fo:block
                                                                                xsl:use-attribute-sets="table-column-header"
                                                                                text-align="center">
                                                                            <xsl:value-of
                                                                                    select="exsl:node-set($labels)/accountingSequence"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                    <fo:table-cell
                                                                            xsl:use-attribute-sets="procurement-data">
                                                                        <fo:block
                                                                                xsl:use-attribute-sets="table-column-header">
                                                                            <xsl:value-of
                                                                                    select="exsl:node-set($labels)/accountingString"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                    <fo:table-cell
                                                                            xsl:use-attribute-sets="procurement-data">
                                                                        <fo:block
                                                                                xsl:use-attribute-sets="table-column-header numeric-align">
                                                                            <xsl:value-of
                                                                                    select="exsl:node-set($labels)/accountingPercentage"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                    <fo:table-cell
                                                                            xsl:use-attribute-sets="procurement-data">
                                                                        <fo:block
                                                                                xsl:use-attribute-sets="table-column-header numeric-align">
                                                                            <xsl:value-of
                                                                                    select="exsl:node-set($labels)/accountingTotal"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                </fo:table-row>
                                                            </fo:table-header>
                                                            <fo:table-body>
                                                                <fo:table-row>
                                                                    <fo:table-cell>
                                                                        <fo:block>
                                                                            <!-- Dummy Row -->
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                </fo:table-row>
                                                                <xsl:for-each select="accounting">
                                                                    <fo:table-row>
                                                                        <fo:table-cell
                                                                                xsl:use-attribute-sets="procurement-data"
                                                                                text-align="center">
                                                                            <fo:block>
                                                                                <xsl:value-of
                                                                                        select="accountingSequenceNumber"/>
                                                                            </fo:block>
                                                                        </fo:table-cell>

                                                                        <fo:table-cell
                                                                                xsl:use-attribute-sets="procurement-data">
                                                                            <fo:block>
                                                                                <xsl:value-of
                                                                                        select="accountingCoaCode"/>-
                                                                                <xsl:value-of
                                                                                        select="accountingIndexCode"/>-
                                                                                <xsl:value-of
                                                                                        select="accountingFundCode"/>-
                                                                                <xsl:value-of
                                                                                        select="accountingOrgCode"/>-
                                                                                <xsl:value-of
                                                                                        select="accountingAccountCode"/>-
                                                                                <xsl:value-of
                                                                                        select="accountingProgramCode"/>-
                                                                                <xsl:value-of
                                                                                        select="accountingActivityCode"/>-
                                                                                <xsl:value-of
                                                                                        select="accountingLocationCode"/>-
                                                                                <xsl:value-of
                                                                                        select="accountingProjectCode"/>

                                                                            </fo:block>
                                                                        </fo:table-cell>

                                                                        <fo:table-cell
                                                                                xsl:use-attribute-sets="procurement-data numeric-align"
                                                                        >
                                                                            <fo:block>
                                                                                <xsl:value-of
                                                                                        select="accountingPercentageDisplay"/>
                                                                            </fo:block>
                                                                        </fo:table-cell>

                                                                        <fo:table-cell
                                                                                xsl:use-attribute-sets="procurement-data  numeric-align"
                                                                        >
                                                                            <fo:block>
                                                                                <xsl:value-of
                                                                                        select="accountingTotalDisplay"/>
                                                                            </fo:block>
                                                                        </fo:table-cell>
                                                                    </fo:table-row>
                                                                </xsl:for-each>
                                                                <fo:table-row>
                                                                    <fo:table-cell
                                                                            xsl:use-attribute-sets="procurement-data numeric-align"
                                                                            number-columns-spanned="2">
                                                                        <fo:block>
                                                                            <xsl:value-of
                                                                                    select="exsl:node-set($labels)/claAccountingTotalAtCommodity"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                    <fo:table-cell
                                                                            xsl:use-attribute-sets="procurement-data numeric-align"
                                                                    >
                                                                        <fo:block>
                                                                            <xsl:value-of
                                                                                    select="distributionPercentageDisplay"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                    <fo:table-cell
                                                                            xsl:use-attribute-sets="procurement-data numeric-align"
                                                                    >
                                                                        <fo:block>
                                                                            <xsl:value-of
                                                                                    select="allAccountingTotalDisplay"/>
                                                                        </fo:block>
                                                                    </fo:table-cell>
                                                                </fo:table-row>
                                                            </fo:table-body>
                                                        </fo:table>
                                                    </fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                                </xsl:for-each>
                            </xsl:otherwise>
                        </xsl:choose>

                        <!--</fo:table>-->
                    </fo:block-container>
                    <xsl:choose>
                        <xsl:when test="requisition/header/isDocumentLevelAccounting='true'">
                            <fo:block xsl:use-attribute-sets="line-break">&#160;
                            </fo:block>
                            <fo:block-container xsl:use-attribute-sets="container">
                                <fo:table xsl:use-attribute-sets="table">
                                    <fo:table-body>
                                        <fo:table-row>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                <fo:block xsl:use-attribute-sets="table-column-header numeric-align">
                                                    <xsl:value-of select="exsl:node-set($labels)/grandTotalCommodity"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data numeric-align">
                                                <fo:block>
                                                    <xsl:value-of select="requisition/allCommodityTotalDisplay"/>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </fo:table-body>
                                </fo:table>
                            </fo:block-container>
                            <fo:block xsl:use-attribute-sets="line-break">&#160;
                            </fo:block>
                            <fo:block-container xsl:use-attribute-sets="container">
                                <fo:block>
                                    <xsl:value-of select="exsl:node-set($labels)/accountingDistribution"/>
                                </fo:block>

                            </fo:block-container>
                            <fo:block-container xsl:use-attribute-sets="container">
                                <fo:table xsl:use-attribute-sets="table">
                                    <fo:table-column xsl:use-attribute-sets="accounting-sequence-column"/>
                                    <fo:table-column xsl:use-attribute-sets="accounting-data-column"/>
                                    <fo:table-column xsl:use-attribute-sets="accounting-sequence-column"/>
                                    <fo:table-column xsl:use-attribute-sets="accounting-sequence-column"/>
                                    <fo:table-header>
                                        <fo:table-row>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                <fo:block xsl:use-attribute-sets="table-column-header "
                                                          text-align="center">
                                                    <xsl:value-of select="exsl:node-set($labels)/accountingSequence"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                <fo:block xsl:use-attribute-sets="table-column-header">
                                                    <xsl:value-of select="exsl:node-set($labels)/accountingString"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                <fo:block xsl:use-attribute-sets="table-column-header numeric-align">
                                                    <xsl:value-of select="exsl:node-set($labels)/accountingPercentage"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                <fo:block xsl:use-attribute-sets="table-column-header numeric-align">
                                                    <xsl:value-of select="exsl:node-set($labels)/accountingTotal"/>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </fo:table-header>
                                    <fo:table-body>
                                        <fo:table-row>
                                            <fo:table-cell>
                                                <fo:block>
                                                    <!-- Dummy Row -->
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                        <xsl:for-each select="requisition/accounting">
                                            <fo:table-row>
                                                <fo:table-cell xsl:use-attribute-sets="procurement-data"
                                                               text-align="center">
                                                    <fo:block>
                                                        <xsl:value-of select="accountingSequenceNumber"/>
                                                    </fo:block>
                                                </fo:table-cell>

                                                <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                    <fo:block>
                                                        <xsl:value-of select="accountingCoaCode"/>-
                                                        <xsl:value-of select="accountingIndexCode"/>-
                                                        <xsl:value-of select="accountingFundCode"/>-
                                                        <xsl:value-of select="accountingOrgCode"/>-
                                                        <xsl:value-of select="accountingAccountCode"/>-
                                                        <xsl:value-of select="accountingProgramCode"/>-
                                                        <xsl:value-of select="accountingActivityCode"/>-
                                                        <xsl:value-of select="accountingLocationCode"/>-
                                                        <xsl:value-of select="accountingProjectCode"/>

                                                    </fo:block>
                                                </fo:table-cell>

                                                <fo:table-cell xsl:use-attribute-sets="procurement-data numeric-align"
                                                >
                                                    <fo:block>
                                                        <xsl:value-of select="accountingPercentageDisplay"/>
                                                    </fo:block>
                                                </fo:table-cell>

                                                <fo:table-cell xsl:use-attribute-sets="procurement-data numeric-align"
                                                >
                                                    <fo:block>
                                                        <xsl:value-of select="accountingTotalDisplay"/>
                                                    </fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                        </xsl:for-each>
                                    </fo:table-body>
                                </fo:table>
                            </fo:block-container>
                            <fo:block xsl:use-attribute-sets="line-break">&#160;
                            </fo:block>
                            <fo:block-container xsl:use-attribute-sets="container">
                                <fo:table xsl:use-attribute-sets="table">
                                    <fo:table-body>
                                        <fo:table-row>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                <fo:block xsl:use-attribute-sets="table-column-header numeric-align">
                                                    <xsl:value-of select="exsl:node-set($labels)/grandTotalAccounting"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data numeric-align">
                                                <fo:block>
                                                    <xsl:value-of select="requisition/allAccountingTotalDisplay"/>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </fo:table-body>
                                </fo:table>
                            </fo:block-container>
                        </xsl:when>
                        <xsl:otherwise>
                            <fo:block xsl:use-attribute-sets="line-break">&#160;
                            </fo:block>
                            <fo:block-container xsl:use-attribute-sets="container">
                                <fo:table xsl:use-attribute-sets="table">
                                    <fo:table-body>
                                        <fo:table-row>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                <fo:block>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell number-columns-spanned="2"
                                                           xsl:use-attribute-sets="procurement-data">
                                                <fo:block xsl:use-attribute-sets="table-column-header numeric-align">
                                                    <xsl:value-of select="exsl:node-set($labels)/grandTotalCommodity"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data numeric-align">
                                                <fo:block>
                                                    <xsl:value-of select="requisition/grandCommodityTotalDisplay"/>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                        <fo:table-row>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data">
                                                <fo:block>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell number-columns-spanned="2"
                                                           xsl:use-attribute-sets="procurement-data">
                                                <fo:block xsl:use-attribute-sets="table-column-header numeric-align">
                                                    <xsl:value-of select="exsl:node-set($labels)/grandTotalAccounting"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell xsl:use-attribute-sets="procurement-data numeric-align">
                                                <fo:block>
                                                    <xsl:value-of select="requisition/grandAccountingTotalDisplay"/>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </fo:table-body>
                                </fo:table>
                            </fo:block-container>
                            <fo:block xsl:use-attribute-sets="line-break">&#160;
                            </fo:block>
                        </xsl:otherwise>
                    </xsl:choose>
                    <fo:block xsl:use-attribute-sets="line-break">&#160;
                    </fo:block>
                    <fo:block-container xsl:use-attribute-sets="container">
                        <fo:block>
                            <xsl:value-of select="exsl:node-set($labels)/disclaimer"/>
                        </fo:block>
                    </fo:block-container>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <xsl:template name="zero_width_space">
        <xsl:param name="data"/>
        <xsl:param name="counter" select="0"/>
        <xsl:choose>
            <xsl:when test="$counter &lt;= string-length($data)">
                <xsl:value-of select='concat(substring($data,$counter,1),"&#8203;")'/>
                <xsl:call-template name="zero_width_space_1">
                    <xsl:with-param name="data" select="$data"/>
                    <xsl:with-param name="counter" select="$counter+1"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="zero_width_space_1">
        <xsl:param name="data"/>
        <xsl:param name="counter"/>
        <xsl:value-of select='concat(substring($data,$counter,1),"&#8203;")'/>
        <xsl:call-template name="zero_width_space">
            <xsl:with-param name="data" select="$data"/>
            <xsl:with-param name="counter" select="$counter+1"/>
        </xsl:call-template>
    </xsl:template>

</xsl:stylesheet>
