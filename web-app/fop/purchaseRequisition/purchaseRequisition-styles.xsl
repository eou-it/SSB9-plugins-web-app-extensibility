<?xml version="1.0" encoding="UTF-8" ?>
<!-- Copyright -->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:exsl="http://exslt.org/common"
                exclude-result-prefixes="exsl">

    <!-- Overall page layout. -->
    <xsl:attribute-set name="page">
        <xsl:attribute name="page-height">11in</xsl:attribute>
        <xsl:attribute name="page-width">8.5in</xsl:attribute>
        <xsl:attribute name="margin">.25in, .25in, .25in, .25in</xsl:attribute>
    </xsl:attribute-set>

    <!-- Header region for the procurement logo and address when printed at top of the page. -->
    <xsl:attribute-set name="header-region">
        <xsl:attribute name="extent">.5in</xsl:attribute>
    </xsl:attribute-set>

    <!-- Footer region for the procurement logo and address when printed at bottom of the page. -->
    <xsl:attribute-set name="footer-region">
        <xsl:attribute name="extent">.5in</xsl:attribute>
    </xsl:attribute-set>

    <!-- Body region for main content of the page. -->
    <xsl:attribute-set name="body-region">
        <xsl:attribute name="margin-top">
            <xsl:choose>
                <xsl:when test="exsl:node-set($config)/logoTopBottom = 'TOP'">
                    .5in
                </xsl:when>
                <xsl:otherwise>
                    0in
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
        <xsl:attribute name="margin-bottom">
            <xsl:choose>
                <xsl:when test="exsl:node-set($config)/logoTopBottom = 'BOTTOM'">
                    .7in
                </xsl:when>
                <xsl:otherwise>
                    0in
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:attribute-set>

    <!-- Container content within a region - font styles, writing direction -->
    <xsl:attribute-set name="container">
        <xsl:attribute name="font-family">
            <xsl:value-of select="$locale-font-family"/>
        </xsl:attribute>
        <xsl:attribute name="font-size">10pt</xsl:attribute>
        <xsl:attribute name="font-weight">normal</xsl:attribute>
        <xsl:attribute name="writing-mode">
            <xsl:value-of select="$base-writing-mode"/>
        </xsl:attribute>
        <xsl:attribute name="text-align">
            <xsl:value-of select="$text-align-left"/>
        </xsl:attribute>
    </xsl:attribute-set>

    <!-- Section content within a container -->
    <xsl:attribute-set name="section">
        <xsl:attribute name="margin-top">4mm</xsl:attribute>
        <xsl:attribute name="keep-together.within-column">always</xsl:attribute>
    </xsl:attribute-set>

    <!-- Table styles. -->
    <xsl:attribute-set name="table">
        <xsl:attribute name="table-layout">fixed</xsl:attribute>
        <xsl:attribute name="width">100%</xsl:attribute>
        <xsl:attribute name="border">.75pt solid rgb(221, 221, 221)</xsl:attribute>
        <xsl:attribute name="border-collapse">collapse</xsl:attribute>
        <xsl:attribute name="background-color">transparent</xsl:attribute>
    </xsl:attribute-set>

    <!-- Table caption styles. -->
    <xsl:attribute-set name="table-caption">
        <xsl:attribute name="font-size">8pt</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
        <xsl:attribute name="margin-bottom">2pt</xsl:attribute>
    </xsl:attribute-set>

    <!-- Line break. -->
    <xsl:attribute-set name="line-break">
        <xsl:attribute name="white-space-collapse">false</xsl:attribute>
        <xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
        <xsl:attribute name="font-size">15px</xsl:attribute>
    </xsl:attribute-set>

    <!-- Margin. -->
    <xsl:attribute-set name="margin">
        <xsl:attribute name="margin">4pt, 0pt, 4pt, 0pt</xsl:attribute>
    </xsl:attribute-set>
    
    
    <!-- Margin. -->
    <xsl:attribute-set name="numeric-align">
        <xsl:attribute name="text-align">
            <xsl:choose>
                <xsl:when test="exsl:node-set($config)/languageDirection = 'ltr'">
                    right
                </xsl:when>
                <xsl:when test="exsl:node-set($config)/languageDirection = 'rtl'">
                    left
                </xsl:when>
            </xsl:choose>
        </xsl:attribute>
    </xsl:attribute-set>
    
    
    <!-- Table cell styles  -->
    <xsl:attribute-set name="table-cell">
        <xsl:attribute name="background-color">transparent</xsl:attribute>
        <xsl:attribute name="margin">0pt, 0pt, 0pt, 0pt</xsl:attribute>
        <xsl:attribute name="padding">3pt, 2pt, 2pt, 2pt</xsl:attribute>
        <xsl:attribute name="text-align">
            <xsl:value-of select="$text-align-left"/>
        </xsl:attribute>
    </xsl:attribute-set>

    <!-- Table column header cell styling -->
    <xsl:attribute-set name="table-column-header" use-attribute-sets="table-cell">
        <xsl:attribute name="border">.75pt solid rgb(221, 221, 221)</xsl:attribute>
        <xsl:attribute name="background-color">rgb(242, 242, 242)</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
    </xsl:attribute-set>

    <!-- Table row header cell styling -->
    <xsl:attribute-set name="table-row-header" use-attribute-sets="table-cell">
        <xsl:attribute name="border">.75pt solid rgb(221, 221, 221)</xsl:attribute>
        <xsl:attribute name="background-color">rgb(242, 242, 242)</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
    </xsl:attribute-set>

    <!-- Table data cell style -->
    <xsl:attribute-set name="table-data" use-attribute-sets="table-cell">
        <xsl:attribute name="border-top">.75pt solid rgb(221, 221, 221)</xsl:attribute>
        <xsl:attribute name="border-bottom">.75pt solid rgb(221, 221, 221)</xsl:attribute>
    </xsl:attribute-set>

    <!-- Table data cell with row header style -->
    <xsl:attribute-set name="table-data-with-row-header" use-attribute-sets="table-cell">
        <xsl:attribute name="border">.75pt solid rgb(221, 221, 221)</xsl:attribute>
    </xsl:attribute-set>

    <!-- Modifies the table cell style for numeric fields the need to decimal align. -->
    <xsl:attribute-set name="table-cell.fixed-length">
        <xsl:attribute name="text-align">center</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="table-cell.fixed-decimal">
        <xsl:attribute name="text-align">right</xsl:attribute>
    </xsl:attribute-set>

    <!-- Modifies the table cell style for total cells. -->
    <xsl:attribute-set name="table-cell.total">
        <xsl:attribute name="font-weight">bold</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="no-data-msg">
        <xsl:attribute name="margin">0pt</xsl:attribute>
        <xsl:attribute name="padding">3pt</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
        <xsl:attribute name="width">100%</xsl:attribute>
        <xsl:attribute name="border">1pt solid rgb(221, 221, 221)</xsl:attribute>
    </xsl:attribute-set>

    <!--
    =====================================================================================================
         The following attribute definitions are for section specific elements.
         Many of the attributes inherit common styles using use-attribute-sets which produces a similar
         effect to cascading stylesheets.
    =====================================================================================================
     -->
    <!-- procurement section styles -->
    <xsl:attribute-set name="procurement-table" use-attribute-sets="table">
        <xsl:attribute name="border">.75pt solid rgb(221, 221, 221)</xsl:attribute>
        <xsl:attribute name="writing-mode">
            <xsl:choose>
                <xsl:when test="exsl:node-set($config)/logoLeftRight = 'LEFT'">
                    lr
                </xsl:when>
                <xsl:otherwise>
                    rl
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="procurement-logo-column">
        <xsl:attribute name="column-width">17%</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="procurement-contact-info-column">
        <xsl:attribute name="column-width">10%</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="label">
        <xsl:attribute name="font-weight">bold</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="procurement-data" use-attribute-sets="table-cell">
        <xsl:attribute name="font-size">80%</xsl:attribute>
        <xsl:attribute name="padding">2pt, 1pt, 1pt, 1pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="procurement-cla-data" use-attribute-sets="table-cell">
        <xsl:attribute name="font-size">80%</xsl:attribute>
        <xsl:attribute name="padding">2pt, 1pt, 1pt, 1pt</xsl:attribute>
        <xsl:attribute name="background-color">rgb(242, 242, 242)</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="procurement-logo">
        <xsl:attribute name="src">
            <xsl:value-of
                    select="concat('url(', /pdfModel/logoPath, '/', /pdfModel/logoFilename, ')')"/>
        </xsl:attribute>
        <xsl:attribute name="text-align">left</xsl:attribute>
        <xsl:attribute name="content-width">scale-to-fit</xsl:attribute>
        <xsl:attribute name="content-height">auto</xsl:attribute>
        <xsl:attribute name="width">auto</xsl:attribute>
        <xsl:attribute name="scaling">uniform</xsl:attribute>
    </xsl:attribute-set>

    <!-- Pay Summary section styles -->
    <xsl:attribute-set name="commotity-item-column">
        <xsl:attribute name="column-width">10%</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="commotity-item-seq-column">
        <xsl:attribute name="column-width">10%</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="commotity-item-quantity-column">
        <xsl:attribute name="column-width">12%</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="commotity-item-desc-column">
        <xsl:attribute name="column-width">38%</xsl:attribute>
    </xsl:attribute-set>
    
    <xsl:attribute-set name="accounting-sequence-column">
        <xsl:attribute name="column-width">20%</xsl:attribute>
    </xsl:attribute-set>
    
    <xsl:attribute-set name="accounting-data-column">
        <xsl:attribute name="column-width">40%</xsl:attribute>
    </xsl:attribute-set>
   
    <!-- Filing Statuses section styles -->

</xsl:stylesheet>
