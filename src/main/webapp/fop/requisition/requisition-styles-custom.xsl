<?xml version="1.0" encoding="UTF-8" ?>
<!-- Copyright 2016 Ellucian Company L.P. and its affiliates. -->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:exsl="http://exslt.org/common"
                exclude-result-prefixes="exsl">

    <!--
    Purchase Requisition styling customizations can be placed here.
    Attribute sets defined in this file that have the same name as attributes sets in the default
    styling file, requisition-styles.xsl, will override the default settings. New attribute sets (styles) can be
    defined here as well; however, they must be added to the element's "user-attribute-sets" in requisition.xsl.
    -->

    <!-- Examples.

    Override the default font size of the Purchase Requisition.
    <xsl:attribute-set name="container">
        <xsl:attribute name="font-size">6pt</xsl:attribute>
    </xsl:attribute-set>

    Override the default table cell padding.
    <xsl:attribute-set name="table-cell">
    <xsl:attribute name="padding">6pt, 3pt, 3pt, 3pt</xsl:attribute>
    </xsl:attribute-set>

    Override the default location of the employer logos.
    <xsl:attribute-set name="employer-logo">
    <xsl:attribute name="src">
        <xsl:value-of select="concat('url(/C:/Users/mfisher/Documents/Banner_XE_HR/test_logos/hr_logo_', translate(/root/employer/code, $uppercase, $lowercase), '.png)')"/>
    </xsl:attribute>
    </xsl:attribute-set>

    -->

</xsl:stylesheet>
