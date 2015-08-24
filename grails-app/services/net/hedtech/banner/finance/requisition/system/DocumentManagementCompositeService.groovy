/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.finance.util.LoggerUtility
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

    /**
     * This action is responsible for listing the documents
     * @param file to be uploaded.
     * @param requisitionCode for which file to be uploaded.
     * @param docType for file upload.
     * @param ownerPidm logged in user pidm.
     * @param vpdiCode session mep code.
     * @param bdmInstalled to check if bdm is installed.
     * @return list of documents of requisition
     */
    def uploadDocument( file, requisitionCode, docType, ownerPidm, vpdiCode, bdmInstalled ) {
        LoggerUtility.debug( LOGGER, 'requisitionCode ' + requisitionCode + ' vpdiCode ' + vpdiCode + 'bdmInstalled ' + bdmInstalled + 'docType ' + docType )
        if (!bdmInstalled) {
            LoggerUtility.error( LOGGER, 'BDM Not installed' )
            throw new ApplicationException( DocumentManagementCompositeService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_BDM_NOT_INSTALLED, [] ) )
        }
        try {
            def map = bdmAttachmentService.createBDMLocation( file )
            def requisition = requisitionHeaderService.findRequisitionHeaderByRequestCode( requisitionCode )
            uploadDocToBdmServer( requisition, docType, ownerPidm, map.fileName, map.absoluteFileName, vpdiCode )
            map.userDir.deleteDir()
            listDocumentsByRequisitionCode( requisitionCode, vpdiCode, bdmInstalled )
        } catch (FileNotFoundException e) {
            throw new ApplicationException( DocumentManagementCompositeService, new BusinessLogicValidationException( e.getMessage(), [] ) )
        }
    }


    private
    def uploadDocToBdmServer( RequisitionHeader requisition, docType, ownerPidm, fileName, absoluteFileName, vpdiCode ) throws ApplicationException {
        DateFormat dateFormat = new SimpleDateFormat( FinanceProcurementConstants.BDM_DATE_FORMAT )
        def documentAttributes = [:]
        //Empty value is set to few fields as we need to change them when we create the fields at BDM forms.
        documentAttributes.put( FinanceProcurementConstants.BDM_DOCUMENT_ID, requisition.requestCode )
        documentAttributes.put( FinanceProcurementConstants.BDM_BANNER_DOC_TYPE, docType )
        documentAttributes.put( FinanceProcurementConstants.BDM_DOCUMENT_TYPE, docType )
        documentAttributes.put( FinanceProcurementConstants.BDM_TRANSACTION_DATE, requisition.transactionDate )
        documentAttributes.put( FinanceProcurementConstants.BDM_VENDOR_ID, requisition.vendorPidm ? requisition.vendorPidm : FinanceProcurementConstants.EMPTY_STRING )
        documentAttributes.put( FinanceProcurementConstants.BDM_VENDOR_NAME, PersonIdentificationName.findByPidm( ownerPidm )?.fullName )
        documentAttributes.put( FinanceProcurementConstants.BDM_FIRST_NAME, fileName )
        documentAttributes.put( FinanceProcurementConstants.BDM_PIDM, ownerPidm )
        documentAttributes.put( FinanceProcurementConstants.BDM_ROUTING_STATUS, FinanceProcurementConstants.EMPTY_STRING )
        documentAttributes.put( FinanceProcurementConstants.BDM_ACTIVITY_DATE, dateFormat.format( new Date() ) )
        documentAttributes.put( FinanceProcurementConstants.BDM_DISPOSITION_DATE, FinanceProcurementConstants.EMPTY_STRING )
        try {
            bdmAttachmentService.createDocument( getBdmParams(), absoluteFileName, documentAttributes, vpdiCode )
        } catch (ApplicationException ae) {
            LoggerUtility.error( LOGGER, 'Error while uploading document' + ae.message )
            throw new ApplicationException( DocumentManagementCompositeService,
                                            new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_BDM_ERROR, [] ) )
        }
    }

    /**
     * this method will delete the documents uploaded to BDM server
     * @param documentId
     * @param vpdiCode
     * @param bdmInstalled
     * @param requisitionCode
     * @return list of documents of requisition
     */
    def deleteDocumentsByRequisitionCode( documentId, vpdiCode, bdmInstalled, requisitionCode ) {
        def docIds = []
        LoggerUtility.debug( LOGGER, 'requisitionCode ' + requisitionCode + ' vpdiCode ' + vpdiCode + 'bdmInstalled ' + bdmInstalled )
        docIds.add( documentId )
        try {
            if (!bdmInstalled) {
                throw new ApplicationException( DocumentManagementCompositeService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_BDM_NOT_INSTALLED, [] ) )
            }
            bdmAttachmentService.deleteDocument( getBdmParams(), docIds, vpdiCode )
            listDocumentsByRequisitionCode( requisitionCode, vpdiCode, bdmInstalled )
        } catch (ApplicationException ae) {
            LoggerUtility.error( LOGGER, 'Error while Deleting document' + ae.message )
            throw new ApplicationException( DocumentManagementCompositeService,
                                            new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_BDM_ERROR, [] ) )
        }
    }

    /**
     * this method will delete the documents uploaded to BDM server
     * @param documentId
     * @param vpdiCode
     * @param requisitionCode
     * @return list of documents of requisition
     */
    def listDocumentsByRequisitionCode( def requisitionCode, vpdiCode, bdmInstalled ) {
        def criteria = [:]
        LoggerUtility.debug( LOGGER, 'requisitionCode ' + requisitionCode + ' vpdiCode ' + vpdiCode + 'bdmInstalled ' + bdmInstalled )
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
        def docWithUsername = []
        SimpleDateFormat dateFormat = new SimpleDateFormat( FinanceProcurementConstants.BDM_DATE_FORMAT )
        documentList.each {doc ->
            String ownerPidm = doc.docAttributes.get( FinanceProcurementConstants.BDM_OWNER_PIDM )
            String ownerName = PersonIdentificationName.findByPidm( ownerPidm )?.fullName
            Map docAttrs = doc.docAttributes
            docAttrs.put( FinanceProcurementConstants.BDM_USER_NAME, ownerName )
            docAttrs.put( FinanceProcurementConstants.BDM_ACTIVITY_DATE, dateFormat.parse( docAttrs[FinanceProcurementConstants.BDM_ACTIVITY_DATE] ) )
            doc.docAttributes = docAttrs
            docWithUsername.add( doc )
        }
        dataMap.documentList = documentList
        dataMap
    }

    /**
     * Gets BDM Parameters
     * @return
     */
    private Map getBdmParams() {
        Map bdmParams = new HashMap()
        ConfigurationHolder.config.bdmserver.each {k, v ->
            bdmParams.put( k, v )
        }
        LoggerUtility.debug( LOGGER, "BDMParams :: " + bdmParams )
        return bdmParams
    }
}
