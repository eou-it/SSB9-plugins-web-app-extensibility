<?xml version="1.0" encoding="UTF-8" ?>
<!-- Copyright -->
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:exsl="http://exslt.org/common"
    exclude-result-prefixes="exsl">

    <!--
       This file contains common configuration for all stylesheets in the application.
       Variables defined here can be referenced in other stylesheets. Many of the variables
       use /pdfModel/config node elements from the source XML file.
    -->

    <!-- Utility variables -->
    <xsl:variable name="lowercase" select="'abcdefghijklmnopqrstuvwxyz'"/>
    <xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>

    <!-- Load the entire /pdfModel/config (required node) into a variable to be used later. -->
    <xsl:variable name="config">
        <xsl:copy-of select="/pdfModel/config/*"/>
    </xsl:variable>

    <!-- Load the entire /pdfModel/labels node (required node) into a variable to be used later. -->
    <xsl:variable name="labels">
        <xsl:copy-of select="/pdfModel/labels/*"/>
    </xsl:variable>

    <!-- Set font-family variable based on the given locale. -->
    <xsl:variable name="locale-font-family">
        <xsl:choose>
            <xsl:when test="starts-with(exsl:node-set($config)/locale, 'ar')">
                <xsl:text>Lateef</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>OpenSans</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>

    <!-- Set base-writing-mode variable (left-to-right or right-to-left) based on the given language direction.. -->
    <xsl:variable name="base-writing-mode">
        <xsl:choose>
            <xsl:when test="exsl:node-set($config)/languageDirection = 'rtl'">
                <xsl:text>rl</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>lr</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>

    <!-- Set text-align-left variable based on the given language direction. -->
    <xsl:variable name="text-align-left">
        <xsl:choose>
            <xsl:when test="exsl:node-set($config)/languageDirection = 'rtl'">
                <xsl:text>right</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>left</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>

    <!-- Set text-align-right variable based on the given language direction. -->
    <xsl:variable name="text-align-right">
        <xsl:choose>
            <xsl:when test="exsl:node-set($config)/languageDirection = 'rtl'">
                <xsl:text>left</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>right</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>

</xsl:stylesheet>
