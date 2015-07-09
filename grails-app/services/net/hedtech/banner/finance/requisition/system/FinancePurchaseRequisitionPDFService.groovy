/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.pdf.exceptions.PdfGeneratorException
import net.hedtech.banner.pdf.impl.PdfGenerator
import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib as g
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder

/**
 * Service class for FinanceApprovalHistory.
 */
class FinancePurchaseRequisitionPDFService {
    PdfGenerator pdfGenerator = PdfGenerator.getInstance();
    def requisitionSummaryService
    private static final FOP_CONFIG_FILENAME_DEFAULT = "fop-config.xml"
    static final XSL_FILE_EXTENSION = "xsl"
    static final pdfName = "purchaseRequisition"


    def generatePdfStream( requisitionCode ) {
        // get PDF Model
        def summaryModel = requisitionSummaryService.fetchRequisitionSummaryForRequestCode( requisitionCode )
        renderPDFResponse( summaryModel )
    }


    private renderPDFResponse( summaryModel ) {
        def pdfModel = toPDFModel( summaryModel )
        try {
            // Convert the summaryModel to XML
            String xmlString = pdfGenerator.toXmlString( pdfModel )
            // Eliminate the resulting JSON null objects literals
            xmlString = xmlString.replaceAll( ">null</", "></" )

            def File configFile = getConfigFile( fopBasePath )
            def fopBasePath = getFopBasePath()
            def File xslFile = getXslFile( fopBasePath )
            return pdfGenerator.generatePdfFromXmlString( xmlString, xslFile, configFile )
        } catch (PdfGeneratorException pdfe) {
            pdfe.printStackTrace()
            throw new ApplicationException( "", 'Exception' );
        }
    }


    private getFopBasePath() {
        def fopBasePath = getApplicationContext().getResource( 'fop' ).file.absolutePath
        File fopBaseFile = new File( fopBasePath )

        if (!fopBaseFile.exists()) {
            fopBasePath = null
        }
        return fopBasePath
    }


    private ApplicationContext getApplicationContext() {
        return (ApplicationContext) ServletContextHolder.getServletContext().getAttribute( GrailsApplicationAttributes.APPLICATION_CONTEXT )
    }


    private File getXslFile( String fopBasePath ) {
        def File xslFile = new File( new File( fopBasePath, pdfName ), pdfName.concat( "." ).concat( XSL_FILE_EXTENSION ) )
        return xslFile
    }


    private toPDFModel( model ) {
        // The JSON summaryModel will eventually be converted to an XML structure for PDF generation.
        // The XML must be well-formed in order for the PDF generator to work properly.
        // Here, we construct a new JSON object from the existing one and make minor adjustments
        // to ensure the converted XML file is well-formed.

        def pdfModel = [:]

        // Labels must be localized and loaded to the JSON summaryModel for PDF rendering
        def labels = [:]   // This is a standard JSON element for PDF models
        labels.requisitionNo = g.message( code: 'banner.finance.procurement.requisition.number' )

        // Copy existing summaryModel elements to the new JSON summaryModel for a well-formed XML conversion.
        pdfModel.pdfFileName = getPdfFileName( model.header.requestCode )
        pdfModel.labels = labels
        pdfModel.config.languageDirection = g.message( code: 'default.language.direction' )
        pdfModel.config.locale = LocaleContextHolder.getLocale()
        pdfModel.requisitionNo = model.header.requestCode
        pdfModel
    }


    private File getConfigFile( String fopBasePath ) {
        def File configFile = new File( fopBasePath, FOP_CONFIG_FILENAME_DEFAULT )
        return configFile
    }


    def getPdfFileName( requisitionCode ) {
        def pdfFileName
        if (requisitionCode) {
            pdfFileName = g.message( code: 'banner.finance.procurement.requisition.pdf.filename',
                                     args: [requisitionCode] )
        } else {
            pdfFileName = g.message( code: 'banner.finance.procurement.requisition.pdf.filename' )
        }
        pdfFileName = pdfFileName.replaceAll( "\\s+", "" )
        return pdfFileName
    }
}
