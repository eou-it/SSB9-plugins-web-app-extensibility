/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.system

import net.hedtech.banner.finance.requisition.common.FinanceProcurementConstants
import org.apache.log4j.Logger
import net.hedtech.banner.general.person.PersonIdentificationName
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import org.codehaus.groovy.grails.commons.ConfigurationHolder

import java.text.DateFormat
import java.text.SimpleDateFormat

class DocumentManagementCompositeService {

    private static final def LOGGER = Logger.getLogger( this.getClass() )
    boolean transactional = true

    def bdmAttachmentService
    def requisitionHeaderService

    def uploadDocument( file, requisitionCode, docType, ownerPidm, vpdiCode ) {
        try {
            def map = bdmAttachmentService.createBDMLocation( file )
            def requisition = requisitionHeaderService.findRequisitionHeaderByRequestCode(requisitionCode)
            uploadDocToBdmServer( requisition, docType, ownerPidm, map.fileName, map.absoluteFileName, vpdiCode )
            map.userDir.deleteDir()
        } catch (FileNotFoundException e) {
            throw new ApplicationException( DocumentManagementCompositeService, new BusinessLogicValidationException( e.getMessage(), [] ) )
        } catch (ApplicationException ae) {
            throw ae
        }
    }

    def uploadDocToBdmServer(RequisitionHeader requisition, docType, ownerPidm, fileName, absoluteFileName, vpdiCode) throws ApplicationException{

        DateFormat dateFormat = new SimpleDateFormat(FinanceProcurementConstants.BDM_DATE_FORMAT)

        def documentAttributes = [:]
        //This empty string is just to verify all fields data is set
        String emptyString = ''
        documentAttributes.put(FinanceProcurementConstants.BDM_DOCUMENT_ID,requisition.requestCode)
        documentAttributes.put(FinanceProcurementConstants.BDM_BANNER_DOC_TYPE,docType)
        documentAttributes.put(FinanceProcurementConstants.BDM_DOCUMENT_TYPE,docType)
        documentAttributes.put(FinanceProcurementConstants.BDM_TRANSACTION_DATE,requisition?.transactionDate)
        documentAttributes.put(FinanceProcurementConstants.BDM_VENDOR_ID,requisition?.vendorPidm)
        documentAttributes.put(FinanceProcurementConstants.BDM_VENDOR_NAME,PersonIdentificationName.findByPidm(requisition?.vendorPidm)?.fullName)
        documentAttributes.put(FinanceProcurementConstants.BDM_FIRST_NAME,fileName)
        documentAttributes.put(FinanceProcurementConstants.BDM_PIDM,ownerPidm)
        documentAttributes.put(FinanceProcurementConstants.BDM_ROUTING_STATUS,emptyString)
        documentAttributes.put(FinanceProcurementConstants.BDM_ACTIVITY_DATE,dateFormat.format(new Date()))
        documentAttributes.put(FinanceProcurementConstants.BDM_DISPOSITION_DATE,emptyString)

        bdmAttachmentService.createDocument(getBdmParams(), absoluteFileName, documentAttributes, vpdiCode)
    }


    def listDocumentsByRequisitionCode(def requisitionCode, vpdiCode){
           def criteria = [:]
           criteria.put(FinanceProcurementConstants.BDM_DOCUMENT_ID, requisitionCode)
           def documentList = []
           def resultList
           try {
               resultList = bdmAttachmentService.viewDocument(getBdmParams(), criteria, vpdiCode)
           }catch (ApplicationException ae) {
               throw ae
           }
        def dataMap =[:]
           def requisition = requisitionHeaderService.findRequisitionHeaderByRequestCode(requisitionCode)
          // def docWithUsername = []
           SimpleDateFormat dateFormat = new SimpleDateFormat(FinanceProcurementConstants.BDM_DATE_FORMAT)
        resultList.each{doc->
            def document = [:]
            def docAttributes =[:]
               String ownerPidm = doc.docAttributes.get(FinanceProcurementConstants.BDM_PIDM)
               String ownerName = PersonIdentificationName.findByPidm(ownerPidm)?.fullName
/*              // Map docAttrs = doc.docAttributes
               docAttrs.put('USER_NAME', ownerName)
               docAttrs.put(FinanceProcurementConstants.BDM_ACTIVITY_DATE, dateFormat.parse(docAttrs[FinanceProcurementConstants.BDM_ACTIVITY_DATE]))
               doc.docAttributes = docAttrs*/
               //docWithUsername.add(doc)
               docAttributes.put("DOCUMENT_ID",doc.docAttributes.get('DOCUMENT ID'))
               docAttributes.put("BANNER DOC TYPE",doc.docAttributes.get('BANNER DOC TYPE'))
               docAttributes.put("DOCUMENT_TYPE",doc.docAttributes.get('DOCUMENT TYPE'))
               docAttributes.put("TRANSACTION_DATE",doc.docAttributes.get('TRANSACTION DATE'))
               docAttributes.put("VENDOR_ID",doc.docAttributes.get('VENDOR ID'))
               docAttributes.put("VENDOR_NAME",doc.docAttributes.get('VENDOR NAME'))
               docAttributes.put("FIRST_NAME",doc.docAttributes.get('FIRST NAME'))
               docAttributes.put("ROUTING_STATUS",doc.docAttributes.get('ROUTING STATUS'))
               docAttributes.put("ACTIVITY_DATE",doc.docAttributes.get('ACTIVITY DATE'))
               docAttributes.put("DISPOSITION_DATE",doc.docAttributes.get('DISPOSITION DATE'))
               docAttributes.put('DOCID', doc.docAttributes.get('DOCID'))
               docAttributes.put('USER_NAME', ownerName)
               document.put('docAttributes',docAttributes)
               document.put('viewURL',doc.viewURL)
               document.put('viewURLNoCredential',doc.viewURLNoCredential)
            documentList.add(document)
           }
        dataMap.documentList = documentList
        return dataMap
       }

    /**
     * this method will delete the documents uploaded to BDM server
     * @param documentId
     * @param vpdiCode
     */
    def deleteDocumentsByRequisitionCode(documentId, vpdiCode){
        def docIds = []
        docIds.add(documentId)
        try {
            bdmAttachmentService.deleteDocument(getBdmParams(), docIds, vpdiCode)
        } catch (ApplicationException ae) {
            throw ae
        }
    }

    /**
     *
     * @return
     */
    private Map getBdmParams(){
        Map bdmParams = new HashMap()
        ConfigurationHolder.config.bdmserver.each{k,v->
            bdmParams.put(k,v)
        }
        //bdmParams.put('DataSource',ConfigurationHolder.config.bdmserver.BdmDataSource)
        log.info("BDMParams :: "+bdmParams)

        return bdmParams
    }

}
