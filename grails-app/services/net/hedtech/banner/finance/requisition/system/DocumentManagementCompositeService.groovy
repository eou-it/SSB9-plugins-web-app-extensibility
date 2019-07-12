/*******************************************************************************
 Copyright 2018-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import net.hedtech.banner.general.person.PersonIdentificationName
import grails.util.Holders
import net.hedtech.banner.imaging.BdmUtility
import net.hedtech.bdm.exception.BdmsException

import javax.xml.ws.WebServiceException
import java.text.DateFormat
import java.text.SimpleDateFormat
import grails.gorm.transactions.Transactional

@Transactional
class DocumentManagementCompositeService {


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
        log.debug('requisitionCode {}  vpdiCode {} bdmInstalled {} docType {}' ,requisitionCode,vpdiCode,bdmInstalled,docType )
        if (!bdmInstalled) {
            log.error( 'BDM Not installed' )
            throw new ApplicationException( DocumentManagementCompositeService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_BDM_NOT_INSTALLED, [] ) )
        }
        if (file?.isEmpty()) {
            log.error('Document File to upload for requisition {}  Doc type {} and PIDM {} is empty',requisitionCode,docType,ownerPidm )
            throw new ApplicationException( DocumentManagementCompositeService, new BusinessLogicValidationException( FinanceProcurementConstants.BDM_FILE_UPLOAD_ERROR_MESSAGE, [] ) )
        }
        try {
            def map = bdmAttachmentService.createBDMLocation( file )
            log.debug('BDM temp file details are :{}', map)
            def requisition = requisitionHeaderService.findRequisitionHeaderByRequestCode( requisitionCode )
            uploadDocToBdmServer( requisition, docType, ownerPidm, map.fileName, map.absoluteFileName, vpdiCode )
            map.userDir.deleteDir()
            listDocumentsByRequisitionCode( requisitionCode, vpdiCode, bdmInstalled )
        } catch (FileNotFoundException e) {
            log.error('File Not found' )
            throw new ApplicationException( DocumentManagementCompositeService, new BusinessLogicValidationException( e.getMessage(), [] ) )
        } catch (BdmsException bdmsException){
            log.error('BdmsException: {}',bdmsException.getMessage() )
            throw new ApplicationException( DocumentManagementCompositeService, bdmsException )
        } catch (Exception exp){
            log.error('ERROR : {}',exp.getMessage())
            throw new ApplicationException( DocumentManagementCompositeService, new BusinessLogicValidationException( exp.getMessage(), [] ) )
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
        if (!bdmInstalled) {
            throw new ApplicationException( DocumentManagementCompositeService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_BDM_NOT_INSTALLED, [] ) )
        }
        def docIds = []
        log.debug('requisitionCode {} vpdiCode {} bdmInstalled {}',requisitionCode,vpdiCode,bdmInstalled )
        docIds.add( documentId )
        try {
            bdmAttachmentService.deleteDocument( getBdmParams(), docIds, vpdiCode )
            listDocumentsByRequisitionCode( requisitionCode, vpdiCode, bdmInstalled )
        } catch (ApplicationException ae) {
            log.error('Error while Deleting document {}',ae.message )
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
        if (!bdmInstalled) {
            throw new ApplicationException( DocumentManagementCompositeService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_BDM_NOT_INSTALLED, [] ) )
        }
        def criteria = [:]
        log.debug('requisitionCode {} vpdiCode {} bdmInstalled {}' ,requisitionCode,vpdiCode,bdmInstalled)
        criteria.put( FinanceProcurementConstants.BDM_DOCUMENT_ID, requisitionCode )
        def documentList
        try {
            documentList = bdmAttachmentService.viewDocument( getBdmParams(), criteria, vpdiCode )
        } catch (ApplicationException ae) {
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
        Holders.config.bdmserver.each {k, v ->
            bdmParams.put( k, v )
        }
        bdmParams.put( "KeyPassword", fetchBdmCryptoKey() )
        return bdmParams
    }

    /**
     * Gets BDM Crypto Key
     * @return
     */
    private fetchBdmCryptoKey() {
        try {
            BdmUtility.fetchBdmCryptoKey()
        }
        catch (ApplicationException ae) {
            log.error('Error while getting BDM Crypto key.{}', ae.message )
            throw new ApplicationException( DocumentManagementCompositeService,
                                            new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_BDM_ERROR, [] ) )
        }
    }

    /**
     * Uploads document to BDM server
     * @param requisition
     * @param docType
     * @param ownerPidm
     * @param fileName
     * @param absoluteFileName
     * @param vpdiCode
     * @return
     * @throws ApplicationException
     */
    private uploadDocToBdmServer( RequisitionHeader requisition, docType, ownerPidm, fileName, absoluteFileName, vpdiCode ) throws ApplicationException {
        DateFormat dateFormat = new SimpleDateFormat( FinanceProcurementConstants.BDM_DATE_FORMAT )
        def documentAttributes = [:]
        //Empty value is set to few fields as we need to change them when we create the fields at BDM forms.
        documentAttributes.put( FinanceProcurementConstants.BDM_DOCUMENT_ID, requisition.requestCode )
        documentAttributes.put( FinanceProcurementConstants.BDM_BANNER_DOC_TYPE, docType )
        documentAttributes.put( FinanceProcurementConstants.BDM_DOCUMENT_TYPE, docType )
        documentAttributes.put( FinanceProcurementConstants.BDM_TRANSACTION_DATE, requisition.transactionDate )
        documentAttributes.put( FinanceProcurementConstants.BDM_VENDOR_ID, requisition.vendorPidm ? requisition.vendorPidm : FinanceProcurementConstants.EMPTY_STRING )

        def personObj
        if (requisition.vendorPidm) {
            personObj = PersonIdentificationName.findByPidm( requisition.vendorPidm )
        }
        documentAttributes.put( FinanceProcurementConstants.BDM_VENDOR_NAME, personObj?.lastName ? personObj.lastName :FinanceProcurementConstants.EMPTY_STRING )
        documentAttributes.put( FinanceProcurementConstants.BDM_FIRST_NAME, personObj?.firstName ? personObj.firstName :FinanceProcurementConstants.EMPTY_STRING )
        documentAttributes.put( FinanceProcurementConstants.BDM_PIDM, ownerPidm )
        documentAttributes.put( FinanceProcurementConstants.BDM_DOCUMENT_NAME, fileName )
        documentAttributes.put( FinanceProcurementConstants.BDM_CREATE_NAME, PersonIdentificationName.findByPidm( ownerPidm )?.fullName )
        documentAttributes.put( FinanceProcurementConstants.BDM_ROUTING_STATUS, FinanceProcurementConstants.EMPTY_STRING )
        documentAttributes.put( FinanceProcurementConstants.BDM_ACTIVITY_DATE, dateFormat.format( new Date() ) )
        documentAttributes.put( FinanceProcurementConstants.BDM_DISPOSITION_DATE, FinanceProcurementConstants.EMPTY_STRING )
        try {
            bdmAttachmentService.createDocument( getBdmParams(), absoluteFileName, encodeDocumentAttributes(documentAttributes), vpdiCode )
        } catch (ApplicationException | WebServiceException ae) {
            log.error('Error while uploading document {}', ae.message )
            throw new ApplicationException( DocumentManagementCompositeService,
                                            new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_BDM_ERROR, [] ) )
        }
    }

    /*To check if BDM is installed*/
    def checkIfBDMInstalled(bdmInstalled){
        if (!bdmInstalled) {
            throw new ApplicationException( DocumentManagementCompositeService, new BusinessLogicValidationException( FinanceProcurementConstants.ERROR_MESSAGE_BDM_NOT_INSTALLED, [] ) )
        }
        return bdmInstalled
    }

	def encodeDocumentAttributes(documentAttributes){
		documentAttributes.each {entry ->
                if(entry.value && entry.value instanceof String){
                    entry.value = entry.value.encodeAsHTML()
                }
            }
		return documentAttributes
	}

}
