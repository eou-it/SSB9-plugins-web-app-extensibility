/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import grails.converters.JSON
import grails.util.Holders
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
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

    /**
     * Generates PDf stream
     * @param requisitionCode
     * @return
     */
    def generatePdfStream( requisitionCode, baseCcy ) {
        generateByteStream( requisitionSummaryService.fetchRequisitionSummaryForRequestCode( requisitionCode, baseCcy, false ) )
    }

    /**
     * Gets pdfFile Name
     * @param requisitionCode
     * @return
     */
    def getPdfFileName( requisitionCode ) {
        MessageHelper.message( code: FinanceProcurementConstants.PDF_FILE_NAME,
                               args: [requisitionCode] ).replaceAll( FinanceProcurementConstants.PDF_REPLACE_STRING, FinanceProcurementConstants.EMPTY_STRING )
    }

    /**
     * Process PDF Response
     * @param summaryModel
     * @return
     */
    private generateByteStream( summaryModel ) {
        try {
            def fopBasePath = getFopBasePath()
            PdfGenerator pdfGenerator = PdfGenerator.getInstance();
            return pdfGenerator.generatePdfFromXmlString( pdfGenerator.toXmlString( (toPDFModel( summaryModel ) as JSON).toString() )
                                                                  .replaceAll( FinanceProcurementConstants.PDF_NULL_VALUE, FinanceProcurementConstants.PDF_NULL_REPLACE_VALUE ),
                                                          getXslFilePath( fopBasePath ), getConfigFilePath( fopBasePath ), [:] )
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
        def fopBasePath = getApplicationContext().getResource( FinanceProcurementConstants.BASE_DIR ).file.absolutePath
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
        String configPath = new File( fopBasePath, FinanceProcurementConstants.FOP_CONFIG_FILENAME_DEFAULT ).absolutePath
        LoggerUtility.debug( LOGGER, 'configPath' + configPath )
        configPath
    }

    /**
     * Get XSl file path
     * @param fopBasePath
     * @return
     */
    private String getXslFilePath( String fopBasePath ) {
        String xslPath = new File( new File( fopBasePath, FinanceProcurementConstants.PDF_NAME ), FinanceProcurementConstants.PDF_NAME.concat( FinanceProcurementConstants.DOT )
                .concat( FinanceProcurementConstants.XSL_FILE_EXTENSION ) ).absolutePath
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
        def languageDirection = MessageHelper.message( code: FinanceProcurementConstants.LANGUAGE_DIRECTION )
        pdfModel.pdfModel.config = [logoTopBottom: FinanceProcurementConstants.PDF_HEADER_TOP, logoLeftRight: (languageDirection == FinanceProcurementConstants.PDF_WRITE_DIRECTION_LEFT ?
                FinanceProcurementConstants.PDF_DIRECTION_LEFT : FinanceProcurementConstants.PDF_DIRECTION_RIGHT)]
        pdfModel.pdfModel.config.languageDirection = languageDirection
        pdfModel.pdfModel.config.locale = LocaleContextHolder.getLocale()
        pdfModel.pdfModel.logoPath = Holders.config.banner.finance.procurementLogoPath
        pdfModel.pdfModel.logoFilename = FinanceProcurementConstants.LOG_FILE
        pdfModel.pdfModel.requisition = model
        pdfModel
    }

    /**
     * Collects all labels
     */
    def collectLabels = {labels ->
        labels.title = MessageHelper.message( code: FinanceProcurementConstants.LABELS_TITLE )
        labels.requisitionNo = MessageHelper.message( code: FinanceProcurementConstants.LABELS_REQUISITION_NO )
        labels.requestor = MessageHelper.message( code: FinanceProcurementConstants.LABELS_REQUESTOR )
        labels.transactionDate = MessageHelper.message( code: FinanceProcurementConstants.LABELS_TRANSACTION_DATE )
        labels.phone = MessageHelper.message( code: FinanceProcurementConstants.LABELS_PHONE )
        labels.extension = MessageHelper.message( code: FinanceProcurementConstants.LABELS_EXTENSION )
        labels.email = MessageHelper.message( code: FinanceProcurementConstants.LABELS_EMAIL )
        labels.deliveryDate = MessageHelper.message( code: FinanceProcurementConstants.LABELS_DELIVERY_DATE )
        labels.organization = MessageHelper.message( code: FinanceProcurementConstants.LABELS_ORGANIZATION )
        labels.status = MessageHelper.message( code: FinanceProcurementConstants.LABELS_STATUS )
        labels.shipTo = MessageHelper.message( code: FinanceProcurementConstants.LABELS_SHIP_TO )
        labels.address = MessageHelper.message( code: FinanceProcurementConstants.LABELS_ADDRESS )
        labels.vendor = MessageHelper.message( code: FinanceProcurementConstants.LABELS_VENDOR )
        labels.attentionTo = MessageHelper.message( code: FinanceProcurementConstants.LABELS_ATTENTION_TO )
        labels.headerComment = MessageHelper.message( code: FinanceProcurementConstants.LABELS_HEADER_COMMENT )
        labels.commodities = MessageHelper.message( code: FinanceProcurementConstants.LABELS_COMMODITIES )
        labels.commodityItem = MessageHelper.message( code: FinanceProcurementConstants.LABELS_COMMODITY_ITEM )
        labels.commodityDesc = MessageHelper.message( code: FinanceProcurementConstants.LABELS_COMMODITY_DESC )
        labels.commoditityUOM = MessageHelper.message( code: FinanceProcurementConstants.LABELS_COMMODITY_UOM )
        labels.commodityQuantity = MessageHelper.message( code: FinanceProcurementConstants.LABELS_COMMODITY_QUANTITY )
        labels.commodityUnitPrice = MessageHelper.message( code: FinanceProcurementConstants.LABELS_COMMODITY_UNIT_PRICE )
        labels.commodityOther = MessageHelper.message( code: FinanceProcurementConstants.LABELS_COMMODITY_OTHER )
        labels.commodityTax = MessageHelper.message( code: FinanceProcurementConstants.LABELS_COMMODITY_TAX )
        labels.commodityTotal = MessageHelper.message( code: FinanceProcurementConstants.LABELS_COMMODITY_TOTAL )
        labels.requisitionType = MessageHelper.message( code: FinanceProcurementConstants.LABELS_REQUISITION_TYPE )
        labels.documentRequisitionType = MessageHelper.message( code: FinanceProcurementConstants.LABELS_DOCUMENT_REQUISITION_TYPE )
        labels.commodityRequisitionType = MessageHelper.message( code: FinanceProcurementConstants.LABELS_COMMODITY_REQUISITION_TYPE )
        labels.commodityItemText = MessageHelper.message( code: FinanceProcurementConstants.LABELS_COMMODITY_ITEM_TEXT )
        labels.disclaimer = MessageHelper.message( code: FinanceProcurementConstants.LABELS_DISCLAIMER )
        labels.grandTotalCommodity = MessageHelper.message( code: FinanceProcurementConstants.LABELS_GRAND_TOTAL_COMMODITY )
        labels.accountingDistribution = MessageHelper.message( code: FinanceProcurementConstants.LABELS_ACCOUNTING_DISTRIBUTION )
        labels.accountingSequence = MessageHelper.message( code: FinanceProcurementConstants.LABELS_ACCOUNTING_SEQUENCE )
        labels.accountingString = MessageHelper.message( code: FinanceProcurementConstants.LABELS_ACCOUNTING_STRING )
        labels.accountingPercentage = MessageHelper.message( code: FinanceProcurementConstants.LABELS_ACCOUNTING_PERCENTAGE )
        labels.accountingTotal = MessageHelper.message( code: FinanceProcurementConstants.LABELS_ACCOUNTING_TOTAL )
        labels.grandTotalAccounting = MessageHelper.message( code: FinanceProcurementConstants.LABELS_GRAND_TOTAL_ACCOUNTING )
        labels.claAccountingTotalAtCommodity = MessageHelper.message( code: FinanceProcurementConstants.LABELS_CLA_ACCOUNTING_TOTAL_AT_COMMODITY )
        labels.commodity = MessageHelper.message( code: FinanceProcurementConstants.LABELS_COMMODITY )
        labels.subtitle = MessageHelper.message( code: FinanceProcurementConstants.LABELS_SUBTITLE )
        labels.currency = MessageHelper.message( code: FinanceProcurementConstants.LABELS_CURRENCY )

        labels
    }
}
