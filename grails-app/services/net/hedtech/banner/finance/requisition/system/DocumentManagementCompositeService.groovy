/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.general.person.PersonIdentificationName
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.commons.ConfigurationHolder

import java.text.DateFormat
import java.text.SimpleDateFormat

class DocumentManagementCompositeService {

    private static final def LOGGER = Logger.getLogger( this.getClass() )
    boolean transactional = true

    def bdmAttachmentService
    def requisitionHeaderService


    def uploadDocument( file, requisitionCode, docType, ownerPidm, vpdiCode, bdmInstalled ) {
        if (!bdmInstalled) {
            throw new ApplicationException( DocumentManagementCompositeService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_BDM_NOT_INSTALLED, [] ) )
        }
        try {
            def map = bdmAttachmentService.createBDMLocation( file )
            def requisition = requisitionHeaderService.findRequisitionHeaderByRequestCode( requisitionCode )
            uploadDocToBdmServer( requisition, docType, ownerPidm, map.fileName, map.absoluteFileName, vpdiCode )
            map.userDir.deleteDir()
            return listDocumentsByRequisitionCode( requisitionCode, vpdiCode, bdmInstalled )
        } catch (FileNotFoundException e) {
            throw new ApplicationException( DocumentManagementCompositeService, new BusinessLogicValidationException( e.getMessage(), [] ) )
        } catch (ApplicationException ae) {
            throw ae
        }
    }

    /**
     * this method will delete the documents uploaded to BDM server
     * @param documentId
     * @param vpdiCode
     */
    def deleteDocumentsByRequisitionCode( documentId, vpdiCode, bdmInstalled, requisitionCode ) {
        def docIds = []
        docIds.add( documentId )
        try {
            if (!bdmInstalled) {
                throw new ApplicationException( DocumentManagementCompositeService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_BDM_NOT_INSTALLED, [] ) )
            }
            bdmAttachmentService.deleteDocument( getBdmParams(), docIds, vpdiCode )
            return listDocumentsByRequisitionCode( requisitionCode, vpdiCode, bdmInstalled )
        } catch (ApplicationException ae) {
            throw ae
        }
    }

    /**
     * this method will delete the documents uploaded to BDM server
     * @param documentId
     * @param vpdiCode
     */
    def listDocumentsByRequisitionCode( def requisitionCode, vpdiCode, bdmInstalled ) {
        def criteria = [:]
        criteria.put( FinanceProcurementConstants.BDM_DOCUMENT_ID, requisitionCode )
        def documentList
        try {
            if (!bdmInstalled) {
                throw new ApplicationException( DocumentManagementCompositeService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_BDM_NOT_INSTALLED, [] ) )
            }
            documentList = bdmAttachmentService.viewDocument( getBdmParams(), criteria, vpdiCode )
        } catch (Throwable ae) {
            throw new ApplicationException( DocumentManagementCompositeService,
                                            new BusinessLogicValidationException(
                                                    FinanceProcurementConstants.ERROR_MESSAGE_BDM_ERROR, [] ) )
        }
        def dataMap = [:]
        def requisition = requisitionHeaderService.findRequisitionHeaderByRequestCode( requisitionCode )
        def docWithUsername = []
        SimpleDateFormat dateFormat = new SimpleDateFormat( FinanceProcurementConstants.BDM_DATE_FORMAT )
        documentList.each {doc ->
            String ownerPidm = doc.docAttributes.get( 'OWNER_PIDM' )
            String ownerName = PersonIdentificationName.findByPidm( ownerPidm )?.fullName
            Map docAttrs = doc.docAttributes
            docAttrs.put( 'USER_NAME', ownerName )
            docAttrs.put( FinanceProcurementConstants.BDM_ACTIVITY_DATE, dateFormat.parse( docAttrs[FinanceProcurementConstants.BDM_ACTIVITY_DATE] ) )
            doc.docAttributes = docAttrs
            docWithUsername.add( doc )
        }
        dataMap.documentList = documentList
        return dataMap
    }


    private
    def uploadDocToBdmServer( RequisitionHeader requisition, docType, ownerPidm, fileName, absoluteFileName, vpdiCode ) throws ApplicationException {
        DateFormat dateFormat = new SimpleDateFormat( FinanceProcurementConstants.BDM_DATE_FORMAT )
        def documentAttributes = [:]
        //This empty string is just to verify all fields data is set
        String emptyString = ''
        documentAttributes.put( FinanceProcurementConstants.BDM_DOCUMENT_ID, requisition.requestCode )
        documentAttributes.put( FinanceProcurementConstants.BDM_BANNER_DOC_TYPE, docType )
        documentAttributes.put( FinanceProcurementConstants.BDM_DOCUMENT_TYPE, docType )
        documentAttributes.put( FinanceProcurementConstants.BDM_TRANSACTION_DATE, requisition.transactionDate )
        documentAttributes.put( FinanceProcurementConstants.BDM_VENDOR_ID, requisition?.vendorPidm ? requisition.vendorPidm : emptyString )
        documentAttributes.put( FinanceProcurementConstants.BDM_VENDOR_NAME, requisition?.vendorPidm ? PersonIdentificationName.findByPidm( requisition?.vendorPidm )?.fullName : emptyString )
        documentAttributes.put( FinanceProcurementConstants.BDM_FIRST_NAME, fileName )
        documentAttributes.put( FinanceProcurementConstants.BDM_PIDM, ownerPidm )
        documentAttributes.put( FinanceProcurementConstants.BDM_ROUTING_STATUS, emptyString )
        documentAttributes.put( FinanceProcurementConstants.BDM_ACTIVITY_DATE, dateFormat.format( new Date() ) )
        documentAttributes.put( FinanceProcurementConstants.BDM_DISPOSITION_DATE, emptyString )
        bdmAttachmentService.createDocument( getBdmParams(), absoluteFileName, documentAttributes, vpdiCode )
    }

    /**
     *
     * @return
     */
    private Map getBdmParams() {
        Map bdmParams = new HashMap()
        ConfigurationHolder.config.bdmserver.each {k, v ->
            bdmParams.put( k, v )
        }
        //bdmParams.put('DataSource',ConfigurationHolder.config.bdmserver.BdmDataSource)
        log.info( "BDMParams :: " + bdmParams )

        return bdmParams
    }
}