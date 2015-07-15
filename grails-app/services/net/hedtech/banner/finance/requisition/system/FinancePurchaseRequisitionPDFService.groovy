/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import grails.converters.JSON
import grails.util.Holders
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.util.LoggerUtility
import net.hedtech.banner.i18n.MessageHelper
import net.hedtech.banner.pdf.exceptions.PdfGeneratorException
import net.hedtech.banner.pdf.impl.PdfGenerator
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder

/**
 * Service class for Finance Purchase Requisition PDF.
 */
class FinancePurchaseRequisitionPDFService {
    private static final Logger LOGGER = Logger.getLogger( this.class )
    def requisitionSummaryService
    private static final FOP_CONFIG_FILENAME_DEFAULT = 'fop-config.xml'
    private static final XSL_FILE_EXTENSION = 'xsl'
    private static final BASE_DIR = 'fop'
    private static final pdfName = 'purchaseRequisition'
    private static final logoFile = 'ellucian-logo.png'

    /**
     * Generates PDf stream
     * @param requisitionCode
     * @return
     */
    def generatePdfStream( requisitionCode ) {
        renderPDFResponse( requisitionSummaryService.fetchRequisitionSummaryForRequestCode( requisitionCode, false ) )
    }

    /**
     * Gets pdfFile Name
     * @param requisitionCode
     * @return
     */
    def getPdfFileName( requisitionCode ) {
        MessageHelper.message( code: 'banner.finance.procurement.requisition.pdf.filename',
                               args: [requisitionCode] ).replaceAll( "\\s+", "" )
    }

    /**
     * Process PDF Response
     * @param summaryModel
     * @return
     */
    private renderPDFResponse( summaryModel ) {
        try {
            def fopBasePath = getFopBasePath()
            PdfGenerator pdfGenerator = PdfGenerator.getInstance();
            return pdfGenerator.generatePdfFromXmlString( pdfGenerator.toXmlString( (toPDFModel( summaryModel ) as JSON).toString() )
                                                                  .replaceAll( ">null</", "></" ), getXslFilePath( fopBasePath ), getConfigFilePath( fopBasePath ), [:] )
        } catch (PdfGeneratorException e) {
            LoggerUtility.error( LOGGER, 'Error while generating PDF' + e )
            throw new ApplicationException( FinancePurchaseRequisitionPDFService, "generatorError" )
        }
    }

    /**
     * Gets FOB Base path
     * @return
     */
    private getFopBasePath() {
        def fopBasePath = getApplicationContext().getResource( BASE_DIR ).file.absolutePath
        if (!new File( fopBasePath ).exists()) {
            fopBasePath = null
        }
        println 'fopBasePath' + fopBasePath
        LoggerUtility.debug( LOGGER, 'fopBasePath' + fopBasePath )
        fopBasePath
    }

    /**
     * Get Application Context
     * @return
     */
    private ApplicationContext getApplicationContext() {
        return (ApplicationContext) ServletContextHolder.getServletContext().getAttribute( GrailsApplicationAttributes.APPLICATION_CONTEXT )
    }

    /**
     * Get Config files path
     * @param fopBasePath
     * @return
     */
    private String getConfigFilePath( String fopBasePath ) {
        String configPath = new File( fopBasePath, FOP_CONFIG_FILENAME_DEFAULT ).absolutePath
        println 'configPath ' + configPath
        LoggerUtility.debug( LOGGER, 'configPath' + configPath )
        configPath
    }

    /**
     * Get XSl file path
     * @param fopBasePath
     * @return
     */
    private String getXslFilePath( String fopBasePath ) {
        println 'inside getXslFilePath'
        println 'pdfName ' + pdfName

        println 'fopBasePath ' + fopBasePath
        String xslPath = new File( new File( fopBasePath, pdfName ), pdfName.concat( "." ).concat( XSL_FILE_EXTENSION ) ).absolutePath

        println 'xslPath ' + xslPath
        LoggerUtility.debug( LOGGER, 'xslPath' + xslPath )
        xslPath
    }
    /**
     * Convert To Pdf Model
     * @param model
     * @return
     */
    private toPDFModel( model ) {

        def labels = [:]
        collectLabels( labels )
        def pdfModel = [:]
        pdfModel.pdfModel = [:]
        pdfModel.pdfModel.pdfFileName = getPdfFileName( model.header.requestCode )
        pdfModel.pdfModel.labels = labels
        def languageDirection = MessageHelper.message( code: 'default.language.direction' )
        pdfModel.pdfModel.config = [logoTopBottom: 'TOP', logoLeftRight: (languageDirection == 'ltr' ? 'LEFT' : 'RIGHT')]
        pdfModel.pdfModel.config.languageDirection = languageDirection
        pdfModel.pdfModel.config.locale = LocaleContextHolder.getLocale()
        pdfModel.pdfModel.logoPath = Holders.config.banner.finance.procurementLogoPath
        pdfModel.pdfModel.logoFilename = logoFile
        pdfModel.pdfModel.requisition = model
        pdfModel
    }

    /**
     * Collects all labels
     */
    def collectLabels = {labels ->
        labels.title = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.title" )
        labels.requisitionNo = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.number" )
        labels.requestor = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.requestor" )
        labels.transactionDate = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.transactionDate" )
        labels.phone = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.phone" )
        labels.extension = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.extension" )
        labels.email = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.email" )
        labels.deliveryDate = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.deliveryDate" )
        labels.organization = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.organization" )
        labels.status = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.status" )
        labels.shipTo = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.shipTo" )
        labels.address = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.address" )
        labels.vendor = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.vendor" )
        labels.attentionTo = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.attentionTo" )
        labels.headerComment = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.header.comment" )
        labels.commodities = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.commodities" )
        labels.commodityItem = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.commodity.item" )
        labels.commodityDesc = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.commodity.item.description" )
        labels.commoditityUOM = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.commodity.uom" )
        labels.commodityQuantity = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.commodity.quantity" )
        labels.commodityUnitPrice = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.commodity.unitPrice" )
        labels.commodityOther = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.commodity.other" )
        labels.commodityTax = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.commodity.tax" )
        labels.commodityTotal = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.commodity.itemTotal" )
        labels.requisitionType = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.type" )
        labels.documentRequisitionType = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.document.type" )
        labels.commodityRequisitionType = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.commodity.type" )
        labels.commodityItemText = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.commodity.item.text" )
        labels.disclaimer = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.disclaimer" )
        labels.grandTotalCommodity = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.grand.total.commodity" )
        labels.accountingDistribution = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.accounting.distribution" )
        labels.accountingSequence = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.accounting.sequence" )
        labels.accountingString = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.accounting.account.string" )
        labels.accountingPercentage = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.accounting.distribution.percentage" )
        labels.accountingTotal = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.accounting.distribution.total" )
        labels.grandTotalAccounting = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.grand.total.accounting" )
        labels.claAccountingTotalAtCommodity = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.accounting.total" )
        labels.commodity = MessageHelper.message( code: "banner.finance.procurement.requisition.pdf.label.commodity" )
        labels
    }
}
