/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.common

import org.hibernate.annotations.Type

import javax.persistence.Column
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version

/**
 * Util class for Finance Procurement module where in this class will holds all common method and variables will be
 * used across the banner-finance-procurement plugin.
 */
class FinanceProcurementConstants {
    /* Constant for Requisition Header Domain*/

    public static final def REQUISITION_HEADER_FINDER_BY_REQUEST_CODE = 'RequisitionHeader.fetchByRequestCode'
    public static final def REQUISITION_HEADER_FINDER_BY_USER = 'RequisitionHeader.fetchByUser'

    public static final def REQUISITION_HEADER_FINDER_BY_REQUEST_CODE_PARAM_REQUEST_CODE = 'requestCode'
    public static final def REQUISITION_HEADER_FINDER_BY_REQUEST_CODE_PARAM_USER_ID = 'userId'
    public static final def REQUISITION_HEADER_VIEW = 'FPVREQH'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_SURROGATE_ID = 'FPBREQH_SURROGATE_ID'
    public static final def REQUISITION_HEADER_SEQ_FPBREQH_SURROGATE_ID_SEQUENCE = 'FPBREQH_SURROGATE_ID_SEQUENCE'
    public static final def REQUISITION_HEADER_SEQ_GENERATOR = 'FPBREQH_SEQ_GEN'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_CODE = 'FPBREQH_CODE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_ACTIVITY_DATE = 'FPBREQH_ACTIVITY_DATE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_USER_ID = 'FPBREQH_USER_ID'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_REQH_DATE = 'FPBREQH_REQH_DATE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_TRANS_DATE = 'FPBREQH_TRANS_DATE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_NAME = 'FPBREQH_NAME'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_PHONE_AREA = 'FPBREQH_PHONE_AREA'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_PHONE_NUM = 'FPBREQH_PHONE_NUM'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_PHONE_EXT = 'FPBREQH_PHONE_EXT'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_VEND_PIDM = 'FPBREQH_VEND_PIDM'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_ATYP_CODE = 'FPBREQH_ATYP_CODE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_ATYP_SEQ_NUM = 'FPBREQH_ATYP_SEQ_NUM'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_COAS_CODE = 'FPBREQH_COAS_CODE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_ORGN_CODE = 'FPBREQH_ORGN_CODE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_REQD_DATE = 'FPBREQH_REQD_DATE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_COMPLETE_IND = 'FPBREQH_COMPLETE_IND'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_PRINT_IND = 'FPBREQH_PRINT_IND'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_ENCUMB_IND = 'FPBREQH_ENCUMB_IND'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_SUSP_IND = 'FPBREQH_SUSP_IND'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_CANCEL_IND = 'FPBREQH_CANCEL_IND'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_CANCEL_DATE = 'FPBREQH_CANCEL_DATE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_POST_DATE = 'FPBREQH_POST_DATE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_APPR_IND = 'FPBREQH_APPR_IND'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_TEXT_IND = 'FPBREQH_TEXT_IND'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_EDIT_DEFER_IND = 'FPBREQH_EDIT_DEFER_IND'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_RECOMM_VEND_NAME = 'FPBREQH_RECOMM_VEND_NAME'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_CURR_CODE = 'FPBREQH_CURR_CODE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_NSF_ON_OFF_IND = 'FPBREQH_NSF_ON_OFF_IND'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_SINGLE_ACCTG_IND = 'FPBREQH_SINGLE_ACCTG_IND'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_CLOSED_IND = 'FPBREQH_CLOSED_IND'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_SHIP_CODE = 'FPBREQH_SHIP_CODE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_RQST_TYPE_IND = 'FPBREQH_RQST_TYPE_IND'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_INVENTORY_REQ_IND = 'FPBREQH_INVENTORY_REQ_IND'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_CRSN_CODE = 'FPBREQH_CRSN_CODE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_DELIVERY_COMMENT = 'FPBREQH_DELIVERY_COMMENT'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_EMAIL_ADDR = 'FPBREQH_EMAIL_ADDR'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_FAX_AREA = 'FPBREQH_FAX_AREA'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_FAX_NUMBER = 'FPBREQH_FAX_NUMBER'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_FAX_EXT = 'FPBREQH_FAX_EXT'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_ATTENTION_TO = 'FPBREQH_ATTENTION_TO'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_VENDOR_CONTACT = 'FPBREQH_VENDOR_CONTACT'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_DISC_CODE = 'FPBREQH_DISC_CODE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_VEND_EMAIL_ADDR = 'FPBREQH_VEND_EMAIL_ADDR'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_COPIED_FROM = 'FPBREQH_COPIED_FROM'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_TGRP_CODE = 'FPBREQH_TGRP_CODE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_REQ_PRINT_DATE = 'FPBREQH_REQ_PRINT_DATE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_CLOSED_DATE = 'FPBREQH_CLOSED_DATE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_MATCH_REQUIRED = 'FPBREQH_MATCH_REQUIRED'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_ORIGIN_CODE = 'FPBREQH_ORIGIN_CODE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_DOC_REF_CODE = 'FPBREQH_DOC_REF_CODE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_CTRY_CODE_PHONE = 'FPBREQH_CTRY_CODE_PHONE'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_CTRY_CODE_FAX = 'FPBREQH_CTRY_CODE_FAX'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_VERSION = 'FPBREQH_VERSION'
    public static final def REQUISITION_HEADER_FIELD_FPBREQH_DATA_ORIGIN = 'FPBREQH_DATA_ORIGIN'

    public static final def DEFAULT_REQUEST_CODE = 'NEXT'
    public static final def DEFAULT_REQUISITION_ORIGIN = 'SELF_SERVICE'

    public static final def ERROR_MESSAGE_USER_NOT_VALID = 'user.not.valid'

    public static final def ERROR_MESSAGE_MISSING_REQUISITION_HEADER = 'missing.requisition.header'
    public static final
    def SUCCESS_MESSAGE_CREATE_REQUISITION_HEADER = 'net.hedtech.banner.finance.requisition.create.requisition.header.success'
    public static final
    def SUCCESS_MESSAGE_UPDATE_REQUISITION_HEADER = 'net.hedtech.banner.finance.requisition.update.requisition.header.success'
    public static final
    def SUCCESS_MESSAGE_DELETE_REQUISITION_HEADER = 'net.hedtech.banner.finance.requisition.delete.requisition.header.success'

}
