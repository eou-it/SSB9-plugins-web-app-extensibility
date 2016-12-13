/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.common
/**
 * Util class for Finance Procurement module where in this class will holds all common method and variables will be
 * used across the banner-finance-procurement plugin.
 */
class FinanceProcurementConstants {

    public static final String USER_PROFILE_KEY = 'userProfile'
    public static final String DASHBOARD_PAGE = 'financeProcurement'
    public static final String DEFAULT_ACTION = 'dashboard'
    public static final String INSTITUTION_BASE_CCY = 'institutionBaseCcy'

    public static final String DEFAULT_INDICATOR_YES = 'Y'
    public static final String DEFAULT_INDICATOR_NO = 'N'
    public static final int ZERO = 0
    public static final String ZERO_STRING = '0'
    public static final int HUNDRED = 100
    public static final int EIGHT = 8
    public static final int ONE = 1
    public static final String EMPTY_STRING = ''
    public static final String SPACE_STRING = ' '
    public static final int DECIMAL_PRECISION = 2
    public static final int DECIMAL_PRECISION_PERCENTAGE = 8

    /* Constant for Requisition Header Domain*/

    public static final String REQUISITION_HEADER_FINDER_BY_REQUEST_CODE = 'RequisitionHeader.findByRequestCodeAndItem'
    public static final String REQUISITION_HEADER_FINDER_BY_USER = 'RequisitionHeader.fetchByUser'

    public static final String REQUISITION_HEADER_FINDER_BY_REQUEST_CODE_PARAM_REQUEST_CODE = 'requestCode'
    public static final String REQUISITION_HEADER_FINDER_BY_REQUEST_CODE_PARAM_USER_ID = 'userId'
    public static final String REQUISITION_HEADER_VIEW = 'FV_FPBREQH'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_SURROGATE_ID = 'FPBREQH_SURROGATE_ID'
    public static final String REQUISITION_HEADER_SEQ_FPBREQH_SURROGATE_ID_SEQUENCE = 'FPBREQH_SURROGATE_ID_SEQUENCE'
    public static final String REQUISITION_HEADER_SEQ_GENERATOR = 'FPBREQH_SEQ_GEN'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_CODE = 'FPBREQH_CODE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_ACTIVITY_DATE = 'FPBREQH_ACTIVITY_DATE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_USER_ID = 'FPBREQH_USER_ID'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_REQH_DATE = 'FPBREQH_REQH_DATE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_TRANS_DATE = 'FPBREQH_TRANS_DATE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_NAME = 'FPBREQH_NAME'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_PHONE_AREA = 'FPBREQH_PHONE_AREA'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_PHONE_NUM = 'FPBREQH_PHONE_NUM'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_PHONE_EXT = 'FPBREQH_PHONE_EXT'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_VEND_PIDM = 'FPBREQH_VEND_PIDM'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_ATYP_CODE = 'FPBREQH_ATYP_CODE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_ATYP_SEQ_NUM = 'FPBREQH_ATYP_SEQ_NUM'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_COAS_CODE = 'FPBREQH_COAS_CODE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_ORGN_CODE = 'FPBREQH_ORGN_CODE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_REQD_DATE = 'FPBREQH_REQD_DATE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_COMPLETE_IND = 'FPBREQH_COMPLETE_IND'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_PRINT_IND = 'FPBREQH_PRINT_IND'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_ENCUMB_IND = 'FPBREQH_ENCUMB_IND'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_SUSP_IND = 'FPBREQH_SUSP_IND'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_CANCEL_IND = 'FPBREQH_CANCEL_IND'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_CANCEL_DATE = 'FPBREQH_CANCEL_DATE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_POST_DATE = 'FPBREQH_POST_DATE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_APPR_IND = 'FPBREQH_APPR_IND'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_TEXT_IND = 'FPBREQH_TEXT_IND'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_EDIT_DEFER_IND = 'FPBREQH_EDIT_DEFER_IND'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_RECOMM_VEND_NAME = 'FPBREQH_RECOMM_VEND_NAME'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_CURR_CODE = 'FPBREQH_CURR_CODE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_NSF_ON_OFF_IND = 'FPBREQH_NSF_ON_OFF_IND'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_SINGLE_ACCTG_IND = 'FPBREQH_SINGLE_ACCTG_IND'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_CLOSED_IND = 'FPBREQH_CLOSED_IND'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_SHIP_CODE = 'FPBREQH_SHIP_CODE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_RQST_TYPE_IND = 'FPBREQH_RQST_TYPE_IND'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_INVENTORY_REQ_IND = 'FPBREQH_INVENTORY_REQ_IND'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_CRSN_CODE = 'FPBREQH_CRSN_CODE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_DELIVERY_COMMENT = 'FPBREQH_DELIVERY_COMMENT'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_EMAIL_ADDR = 'FPBREQH_EMAIL_ADDR'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_FAX_AREA = 'FPBREQH_FAX_AREA'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_FAX_NUMBER = 'FPBREQH_FAX_NUMBER'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_FAX_EXT = 'FPBREQH_FAX_EXT'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_ATTENTION_TO = 'FPBREQH_ATTENTION_TO'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_VENDOR_CONTACT = 'FPBREQH_VENDOR_CONTACT'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_DISC_CODE = 'FPBREQH_DISC_CODE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_VEND_EMAIL_ADDR = 'FPBREQH_VEND_EMAIL_ADDR'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_COPIED_FROM = 'FPBREQH_COPIED_FROM'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_TGRP_CODE = 'FPBREQH_TGRP_CODE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_REQ_PRINT_DATE = 'FPBREQH_REQ_PRINT_DATE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_CLOSED_DATE = 'FPBREQH_CLOSED_DATE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_MATCH_REQUIRED = 'FPBREQH_MATCH_REQUIRED'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_ORIGIN_CODE = 'FPBREQH_ORIGIN_CODE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_DOC_REF_CODE = 'FPBREQH_DOC_REF_CODE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_CTRY_CODE_PHONE = 'FPBREQH_CTRY_CODE_PHONE'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_CTRY_CODE_FAX = 'FPBREQH_CTRY_CODE_FAX'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_VERSION = 'FPBREQH_VERSION'
    public static final String REQUISITION_HEADER_FIELD_FPBREQH_DATA_ORIGIN = 'FPBREQH_DATA_ORIGIN'

    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_SURROGATE_ID = 'FPBREQH_SURROGATE_ID'
    public static
    final String REQUISITION_HEADER_COPY_SEQ_FPBREQH_SURROGATE_ID_SEQUENCE = 'FPBREQH_SURROGATE_ID_SEQUENCE'
    public static final String REQUISITION_HEADER_COPY_SEQ_GENERATOR = 'FPBREQH_SEQ_GEN'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_CODE = 'FPBREQH_CODE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_ACTIVITY_DATE = 'FPBREQH_ACTIVITY_DATE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_USER_ID = 'FPBREQH_USER_ID'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_REQH_DATE = 'FPBREQH_REQH_DATE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_TRANS_DATE = 'FPBREQH_TRANS_DATE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_NAME = 'FPBREQH_NAME'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_PHONE_AREA = 'FPBREQH_PHONE_AREA'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_PHONE_NUM = 'FPBREQH_PHONE_NUM'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_PHONE_EXT = 'FPBREQH_PHONE_EXT'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_VEND_PIDM = 'FPBREQH_VEND_PIDM'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_ATYP_CODE = 'FPBREQH_ATYP_CODE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_ATYP_SEQ_NUM = 'FPBREQH_ATYP_SEQ_NUM'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_COAS_CODE = 'FPBREQH_COAS_CODE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_ORGN_CODE = 'FPBREQH_ORGN_CODE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_REQD_DATE = 'FPBREQH_REQD_DATE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_COMPLETE_IND = 'FPBREQH_COMPLETE_IND'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_PRINT_IND = 'FPBREQH_PRINT_IND'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_ENCUMB_IND = 'FPBREQH_ENCUMB_IND'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_SUSP_IND = 'FPBREQH_SUSP_IND'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_CANCEL_IND = 'FPBREQH_CANCEL_IND'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_CANCEL_DATE = 'FPBREQH_CANCEL_DATE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_POST_DATE = 'FPBREQH_POST_DATE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_APPR_IND = 'FPBREQH_APPR_IND'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_TEXT_IND = 'FPBREQH_TEXT_IND'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_EDIT_DEFER_IND = 'FPBREQH_EDIT_DEFER_IND'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_RECOMM_VEND_NAME = 'FPBREQH_RECOMM_VEND_NAME'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_CURR_CODE = 'FPBREQH_CURR_CODE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_NSF_ON_OFF_IND = 'FPBREQH_NSF_ON_OFF_IND'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_SINGLE_ACCTG_IND = 'FPBREQH_SINGLE_ACCTG_IND'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_CLOSED_IND = 'FPBREQH_CLOSED_IND'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_SHIP_CODE = 'FPBREQH_SHIP_CODE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_RQST_TYPE_IND = 'FPBREQH_RQST_TYPE_IND'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_INVENTORY_REQ_IND = 'FPBREQH_INVENTORY_REQ_IND'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_CRSN_CODE = 'FPBREQH_CRSN_CODE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_DELIVERY_COMMENT = 'FPBREQH_DELIVERY_COMMENT'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_EMAIL_ADDR = 'FPBREQH_EMAIL_ADDR'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_FAX_AREA = 'FPBREQH_FAX_AREA'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_FAX_NUMBER = 'FPBREQH_FAX_NUMBER'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_FAX_EXT = 'FPBREQH_FAX_EXT'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_ATTENTION_TO = 'FPBREQH_ATTENTION_TO'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_VENDOR_CONTACT = 'FPBREQH_VENDOR_CONTACT'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_DISC_CODE = 'FPBREQH_DISC_CODE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_VEND_EMAIL_ADDR = 'FPBREQH_VEND_EMAIL_ADDR'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_COPIED_FROM = 'FPBREQH_COPIED_FROM'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_TGRP_CODE = 'FPBREQH_TGRP_CODE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_REQ_PRINT_DATE = 'FPBREQH_REQ_PRINT_DATE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_CLOSED_DATE = 'FPBREQH_CLOSED_DATE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_MATCH_REQUIRED = 'FPBREQH_MATCH_REQUIRED'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_ORIGIN_CODE = 'FPBREQH_ORIGIN_CODE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_DOC_REF_CODE = 'FPBREQH_DOC_REF_CODE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_CTRY_CODE_PHONE = 'FPBREQH_CTRY_CODE_PHONE'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_CTRY_CODE_FAX = 'FPBREQH_CTRY_CODE_FAX'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_VERSION = 'FPBREQH_VERSION'
    public static final String REQUISITION_HEADER_COPY_FIELD_FPBREQH_DATA_ORIGIN = 'FPBREQH_DATA_ORIGIN'

    public static final String DEFAULT_REQUEST_CODE = 'NEXT'
    public static final String DEFAULT_REQUISITION_ORIGIN = 'SELF_SERVICE'
    public static final String DEFAULT_REQUISITION_TYPE_IND = 'P'
    public static final String DEFAULT_TEST_ORACLE_LOGIN_USER_NAME = 'FORSED21'
    public static final String DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD = 'u_pick_it'

    public static final String ERROR_MESSAGE_USER_NOT_VALID = 'user.not.valid'
    public static final String ERROR_MESSAGE_BDM_ERROR = 'finance.bdm.error'
    public static final String ERROR_MESSAGE_REQ_RECORD_COUNT = 'error.record.count'
    public static final String ERROR_MESSAGE_NO_MORE_ACCOUNTING = 'no.more.new.accounting'
    public static final String ERROR_MESSAGE_DOCUMENT_CHANGE = 'document.type.cannot.modified'

    public static final String ERROR_MESSAGE_REQUISITION_ALREADY_COMPLETED = 'requisition.already.completed'

    public static final String ERROR_MESSAGE_MISSING_REQUISITION_HEADER = 'missing.requisition.header'
    public static final
    String SUCCESS_MESSAGE_CREATE_REQUISITION_HEADER = 'net.hedtech.banner.finance.requisition.create.requisition.header.success'
    public static final
    String SUCCESS_MESSAGE_COMPLETE_REQUISITION_HEADER = 'net.hedtech.banner.finance.requisition.complete.requisition.header.success'
    public static final
    String SUCCESS_MESSAGE_UPDATE_REQUISITION_HEADER = 'net.hedtech.banner.finance.requisition.update.requisition.header.success'
    public static final
    String SUCCESS_MESSAGE_DELETE_REQUISITION_HEADER = 'net.hedtech.banner.finance.requisition.delete.requisition.header.success'
    public static
    final String SUCCESS_MESSAGE_DELETE_REQUISITION = 'net.hedtech.banner.finance.requisition.delete.requisition.success'
    public static
    final String ERROR_MESSAGE_DELETE_REQUISITION_WARNING_DOCUMENT_PRESENT = 'net.hedtech.banner.finance.requisition.delete.warning.document.present'

    /** Constant variable for Purchase Requisition Detail Domain Property column name **/
    public static final String FPVREQD_VIEW = 'FV_FPRREQD'
    public static final String FIELD_FPRREQD_SURROGATE_ID = 'FPRREQD_SURROGATE_ID'
    public static final String FIELD_FPRREQD_SEQ_GEN = 'FPRREQD_SEQ_GEN'
    public static final String FIELD_FPRREQD_SURROGATE_ID_SEQUENCE = 'FPRREQD_SURROGATE_ID_SEQUENCE'
    public static final String FIELD_FPRREQD_REQH_CODE = 'FPRREQD_REQH_CODE'
    public static final String FIELD_FPRREQD_ITEM = 'FPRREQD_ITEM'
    public static final String FIELD_FPRREQD_ACTIVITY_DATE = 'FPRREQD_ACTIVITY_DATE'
    public static final String FIELD_FPRREQD_USER_ID = 'FPRREQD_USER_ID'
    public static final String FIELD_FPRREQD_COMM_CODE = 'FPRREQD_COMM_CODE'
    public static final String FIELD_FPRREQD_COMM_DESC = 'FPRREQD_COMM_DESC'
    public static final String FIELD_FPRREQD_COAS_CODE = 'FPRREQD_COAS_CODE'
    public static final String FIELD_FPRREQD_ORGN_CODE = 'FPRREQD_ORGN_CODE'
    public static final String FIELD_FPRREQD_BUYR_CODE = 'FPRREQD_BUYR_CODE'
    public static final String FIELD_FPRREQD_QTY = 'FPRREQD_QTY'
    public static final String FIELD_FPRREQD_UOMS_CODE = 'FPRREQD_UOMS_CODE'
    public static final String FIELD_FPRREQD_UNIT_PRICE = 'FPRREQD_UNIT_PRICE'
    public static final String FIELD_FPRREQD_AGRE_CODE = 'FPRREQD_AGRE_CODE'
    public static final String FIELD_FPRREQD_REQD_DATE = 'FPRREQD_REQD_DATE'
    public static final String FIELD_FPRREQD_SHIP_CODE = 'FPRREQD_SHIP_CODE'
    public static final String FIELD_FPRREQD_VEND_PIDM = 'FPRREQD_VEND_PIDM'
    public static final String FIELD_FPRREQD_VEND_REF_NUM = 'FPRREQD_VEND_REF_NUM'
    public static final String FIELD_FPRREQD_PROJ_CODE = 'FPRREQD_PROJ_CODE'
    public static final String FIELD_FPRREQD_POHD_CODE = 'FPRREQD_POHD_CODE'
    public static final String FIELD_FPRREQD_POHD_ITEM = 'FPRREQD_POHD_ITEM'
    public static final String FIELD_FPRREQD_BIDS_CODE = 'FPRREQD_BIDS_CODE'
    public static final String FIELD_FPRREQD_COMPLETE_IND = 'FPRREQD_COMPLETE_IND'
    public static final String FIELD_FPRREQD_SUSP_IND = 'FPRREQD_SUSP_IND'
    public static final String FIELD_FPRREQD_CANCEL_IND = 'FPRREQD_CANCEL_IND'
    public static final String FIELD_FPRREQD_CANCEL_DATE = 'FPRREQD_CANCEL_DATE'
    public static final String FIELD_FPRREQD_CLOSED_IND = 'FPRREQD_CLOSED_IND'
    public static final String FIELD_FPRREQD_POST_DATE = 'FPRREQD_POST_DATE'
    public static final String FIELD_FPRREQD_TEXT_USAGE = 'FPRREQD_TEXT_USAGE'
    public static final String FIELD_FPRREQD_ATYP_CODE = 'FPRREQD_ATYP_CODE'
    public static final String FIELD_FPRREQD_ATYP_SEQ_NUM = 'FPRREQD_ATYP_SEQ_NUM'
    public static final String FIELD_FPRREQD_RECOMM_VEND_NAME = 'FPRREQD_RECOMM_VEND_NAME'
    public static final String FIELD_FPRREQD_CURR_CODE = 'FPRREQD_CURR_CODE'
    public static final String FIELD_FPRREQD_CONVERTED_UNIT_PRICE = 'FPRREQD_CONVERTED_UNIT_PRICE'
    public static final String FIELD_FPRREQD_DISC_AMT = 'FPRREQD_DISC_AMT'
    public static final String FIELD_FPRREQD_TAX_AMT = 'FPRREQD_TAX_AMT'
    public static final String FIELD_FPRREQD_ADDL_CHRG_AMT = 'FPRREQD_ADDL_CHRG_AMT'
    public static final String FIELD_FPRREQD_CONVERT_DISC_AMT = 'FPRREQD_CONVERT_DISC_AMT'
    public static final String FIELD_FPRREQD_CONVERT_TAX_AMT = 'FPRREQD_CONVERT_TAX_AMT'
    public static final String FIELD_FPRREQD_CONVERT_ADDL_CHRG_AMT = 'FPRREQD_CONVERT_ADDL_CHRG_AMT'
    public static final String FIELD_FPRREQD_TGRP_CODE = 'FPRREQD_TGRP_CODE'
    public static final String FIELD_FPRREQD_AMT = 'FPRREQD_AMT'
    public static final String FIELD_FPRREQD_DESC_CHGE_IND = 'FPRREQD_DESC_CHGE_IND'
    public static final String FIELD_FPRREQD_VERSION = 'FPRREQD_VERSION'
    public static final String FIELD_FPRREQD_DATA_ORIGIN = 'FPRREQD_DATA_ORIGIN'

    public static final String FIELD_FPRREQD_COPY_SURROGATE_ID = 'FPRREQD_SURROGATE_ID'
    public static final String FIELD_FPRREQD_COPY_SEQ_GEN = 'FPRREQD_SEQ_GEN'
    public static final String FIELD_FPRREQD_COPY_SURROGATE_ID_SEQUENCE = 'FPRREQD_SURROGATE_ID_SEQUENCE'
    public static final String FIELD_FPRREQD_COPY_REQH_CODE = 'FPRREQD_REQH_CODE'
    public static final String FIELD_FPRREQD_COPY_ITEM = 'FPRREQD_ITEM'
    public static final String FIELD_FPRREQD_COPY_ACTIVITY_DATE = 'FPRREQD_ACTIVITY_DATE'
    public static final String FIELD_FPRREQD_COPY_USER_ID = 'FPRREQD_USER_ID'
    public static final String FIELD_FPRREQD_COPY_COMM_CODE = 'FPRREQD_COMM_CODE'
    public static final String FIELD_FPRREQD_COPY_COMM_DESC = 'FPRREQD_COMM_DESC'
    public static final String FIELD_FPRREQD_COPY_COAS_CODE = 'FPRREQD_COAS_CODE'
    public static final String FIELD_FPRREQD_COPY_ORGN_CODE = 'FPRREQD_ORGN_CODE'
    public static final String FIELD_FPRREQD_COPY_BUYR_CODE = 'FPRREQD_BUYR_CODE'
    public static final String FIELD_FPRREQD_COPY_QTY = 'FPRREQD_QTY'
    public static final String FIELD_FPRREQD_COPY_UOMS_CODE = 'FPRREQD_UOMS_CODE'
    public static final String FIELD_FPRREQD_COPY_UNIT_PRICE = 'FPRREQD_UNIT_PRICE'
    public static final String FIELD_FPRREQD_COPY_AGRE_CODE = 'FPRREQD_AGRE_CODE'
    public static final String FIELD_FPRREQD_COPY_REQD_DATE = 'FPRREQD_REQD_DATE'
    public static final String FIELD_FPRREQD_COPY_SHIP_CODE = 'FPRREQD_SHIP_CODE'
    public static final String FIELD_FPRREQD_COPY_VEND_PIDM = 'FPRREQD_VEND_PIDM'
    public static final String FIELD_FPRREQD_COPY_VEND_REF_NUM = 'FPRREQD_VEND_REF_NUM'
    public static final String FIELD_FPRREQD_COPY_PROJ_CODE = 'FPRREQD_PROJ_CODE'
    public static final String FIELD_FPRREQD_COPY_POHD_CODE = 'FPRREQD_POHD_CODE'
    public static final String FIELD_FPRREQD_COPY_POHD_ITEM = 'FPRREQD_POHD_ITEM'
    public static final String FIELD_FPRREQD_COPY_BIDS_CODE = 'FPRREQD_BIDS_CODE'
    public static final String FIELD_FPRREQD_COPY_COMPLETE_IND = 'FPRREQD_COMPLETE_IND'
    public static final String FIELD_FPRREQD_COPY_SUSP_IND = 'FPRREQD_SUSP_IND'
    public static final String FIELD_FPRREQD_COPY_CANCEL_IND = 'FPRREQD_CANCEL_IND'
    public static final String FIELD_FPRREQD_COPY_CANCEL_DATE = 'FPRREQD_CANCEL_DATE'
    public static final String FIELD_FPRREQD_COPY_CLOSED_IND = 'FPRREQD_CLOSED_IND'
    public static final String FIELD_FPRREQD_COPY_POST_DATE = 'FPRREQD_POST_DATE'
    public static final String FIELD_FPRREQD_COPY_TEXT_USAGE = 'FPRREQD_TEXT_USAGE'
    public static final String FIELD_FPRREQD_COPY_ATYP_CODE = 'FPRREQD_ATYP_CODE'
    public static final String FIELD_FPRREQD_COPY_ATYP_SEQ_NUM = 'FPRREQD_ATYP_SEQ_NUM'
    public static final String FIELD_FPRREQD_COPY_RECOMM_VEND_NAME = 'FPRREQD_RECOMM_VEND_NAME'
    public static final String FIELD_FPRREQD_COPY_CURR_CODE = 'FPRREQD_CURR_CODE'
    public static final String FIELD_FPRREQD_COPY_CONVERTED_UNIT_PRICE = 'FPRREQD_CONVERTED_UNIT_PRICE'
    public static final String FIELD_FPRREQD_COPY_DISC_AMT = 'FPRREQD_DISC_AMT'
    public static final String FIELD_FPRREQD_COPY_TAX_AMT = 'FPRREQD_TAX_AMT'
    public static final String FIELD_FPRREQD_COPY_ADDL_CHRG_AMT = 'FPRREQD_ADDL_CHRG_AMT'
    public static final String FIELD_FPRREQD_COPY_CONVERT_DISC_AMT = 'FPRREQD_CONVERT_DISC_AMT'
    public static final String FIELD_FPRREQD_COPY_CONVERT_TAX_AMT = 'FPRREQD_CONVERT_TAX_AMT'
    public static final String FIELD_FPRREQD_COPY_CONVERT_ADDL_CHRG_AMT = 'FPRREQD_CONVERT_ADDL_CHRG_AMT'
    public static final String FIELD_FPRREQD_COPY_TGRP_CODE = 'FPRREQD_TGRP_CODE'
    public static final String FIELD_FPRREQD_COPY_AMT = 'FPRREQD_AMT'
    public static final String FIELD_FPRREQD_COPY_DESC_CHGE_IND = 'FPRREQD_DESC_CHGE_IND'
    public static final String FIELD_FPRREQD_COPY_VERSION = 'FPRREQD_VERSION'
    public static final String FIELD_FPRREQD_COPY_DATA_ORIGIN = 'FPRREQD_DATA_ORIGIN'


    public static final String DEFAULT_FPBREQD_TEXT_USAGE = 'S'

    public static final String QUERY_PARAM_REQUEST_CODE = 'requestCode'
    public static final String QUERY_PARAM_REQUISITION_DETAIL_ITEM = 'item'
    public static final String QUERY_PARAM_USER_ID = 'userId'
    public static final String NAMED_QUERY_REQUEST_DETAIL_GET_LAST_ITEM = 'RequestDetail.getLastItem'
    public static final String REQ_ACC_NAMED_QUERY_GET_SUM_PERCENTAGE = 'RequestDetail.getSumOfDistributionPercentage'

    public static final String NAMED_QUERY_REQUEST_DETAIL_BY_USER = 'RequestDetail.fetchByUser'
    public static final
    String NAMED_QUERY_REQUEST_DETAIL_BY_REQ_CODE_AND_ITEM = 'RequestDetail.fetchByrequestCodeAndCommodityCode'
    public static final String ERROR_MESSAGE_MISSING_REQUISITION_DETAIL = 'missing.requisition.detail'
    public static final
    String NAMED_QUERY_REQUEST_DETAIL_BY_REQ_CODE = 'RequestDetail.fetchByrequestCode'

    public static final
    String SUCCESS_MESSAGE_CREATE_REQUISITION_DETAIL = 'net.hedtech.banner.finance.requisition.create.requisition.detail.success'
    public static final
    String SUCCESS_MESSAGE_DELETE_REQUISITION_DETAIL = 'net.hedtech.banner.finance.requisition.delete.requisition.detail.success'
    public static final
    String SUCCESS_MESSAGE_UPDATE_REQUISITION_DETAIL = 'net.hedtech.banner.finance.requisition.update.requisition.detail.success'
    public static final String ERROR_MESSAGE_ITEM_IS_REQUIRED = 'item.is.required'

    /** Requisition Accounting **/
    public static final String REQ_ACC_NAMED_QUERY_GET_LAST_SEQ = 'RequestAccounting.getLastSequenceNumberByRequestCode'
    public static final String REQ_ACC_NAMED_QUERY_GET_LAST_ITEM = 'RequestAccounting.getLastItemNumberByRequestCode'
    public static final String REQ_ACC_NAMED_QUERY_BY_CODE = 'RequestAccounting.fetchByRequestCodeItemAndSeq'
    public static final String REQ_ACC_NAMED_QUERY_BY_USER = 'RequestAccounting.fetchByUser'
    public static final String REQ_ACC_NAMED_QUERY_BY_REQUEST_CODE = 'RequestAccounting.fetchAccountingByRequestCode'
    public static
    final String REQ_ACC_NAMED_QUERY_BY_REQUEST_CODE_AND_ITEM = 'RequestAccounting.fetchAccountingByRequestCodeAndItem'
    public static final String QUERY_PARAM_REQUISITION_ACCOUNTING_ITEM = 'item'
    public static final String QUERY_PARAM_REQUISITION_ACCOUNTING_SEQ_NUM = 'sequenceNumber'
    public static final String REQ_ACC_ENTITY_FV_REQ_ACCOUNTING = 'FV_FPRREQA'
    public static final String REQ_ACC_FIELD_FPRREQA_SURROGATE_ID = 'FPRREQA_SURROGATE_ID'
    public static final String REQ_ACC_GEN_FPRREQA_SEQ_GEN = 'FPRREQA_SEQ_GEN'
    public static final String REQ_ACC_SEQ_FPRREQA_SURROGATE_ID_SEQUENCE = 'FPRREQA_SURROGATE_ID_SEQUENCE'
    public static final String REQ_ACC_FIELD_FPRREQA_REQH_CODE = 'FPRREQA_REQH_CODE'
    public static final String REQ_ACC_FIELD_FPRREQA_ITEM = 'FPRREQA_ITEM'
    public static final String REQ_ACC_FIELD_FPRREQA_SEQ_NUM = 'FPRREQA_SEQ_NUM'
    public static final String REQ_ACC_FIELD_FPRREQA_ACTIVITY_DATE = 'FPRREQA_ACTIVITY_DATE'
    public static final String REQ_ACC_FIELD_FPRREQA_USER_ID = 'FPRREQA_USER_ID'
    public static final String REQ_ACC_FIELD_FPRREQA_PCT = 'FPRREQA_PCT'
    public static final String REQ_ACC_FIELD_FPRREQA_AMT = 'FPRREQA_AMT'
    public static final String REQ_ACC_FIELD_FPRREQA_FSYR_CODE = 'FPRREQA_FSYR_CODE'
    public static final String REQ_ACC_FIELD_FPRREQA_PERIOD = 'FPRREQA_PERIOD'
    public static final String REQ_ACC_FIELD_FPRREQA_RUCL_CODE = 'FPRREQA_RUCL_CODE'
    public static final String REQ_ACC_FIELD_FPRREQA_COAS_CODE = 'FPRREQA_COAS_CODE'
    public static final String REQ_ACC_FIELD_FPRREQA_ACCI_CODE = 'FPRREQA_ACCI_CODE'
    public static final String REQ_ACC_FIELD_FPRREQA_FUND_CODE = 'FPRREQA_FUND_CODE'
    public static final String REQ_ACC_FIELD_FPRREQA_ORGN_CODE = 'FPRREQA_ORGN_CODE'
    public static final String REQ_ACC_FIELD_FPRREQA_ACCT_CODE = 'FPRREQA_ACCT_CODE'
    public static final String REQ_ACC_FIELD_FPRREQA_PROG_CODE = 'FPRREQA_PROG_CODE'
    public static final String REQ_ACC_FIELD_FPRREQA_ACTV_CODE = 'FPRREQA_ACTV_CODE'
    public static final String REQ_ACC_FIELD_FPRREQA_LOCN_CODE = 'FPRREQA_LOCN_CODE'
    public static final String REQ_ACC_FIELD_FPRREQA_SUSP_IND = 'FPRREQA_SUSP_IND'
    public static final String REQ_ACC_FIELD_FPRREQA_NSF_SUSP_IND = 'FPRREQA_NSF_SUSP_IND'
    public static final String REQ_ACC_FIELD_FPRREQA_CANCEL_IND = 'FPRREQA_CANCEL_IND'
    public static final String REQ_ACC_FIELD_FPRREQA_CANCEL_DATE = 'FPRREQA_CANCEL_DATE'
    public static final String REQ_ACC_FIELD_FPRREQA_PROJ_CODE = 'FPRREQA_PROJ_CODE'
    public static final String REQ_ACC_FIELD_FPRREQA_APPR_IND = 'FPRREQA_APPR_IND'
    public static final String REQ_ACC_FIELD_FPRREQA_NSF_OVERRIDE_IND = 'FPRREQA_NSF_OVERRIDE_IND'
    public static final String REQ_ACC_FIELD_FPRREQA_ABAL_IND = 'FPRREQA_ABAL_IND'
    public static final String REQ_ACC_FIELD_FPRREQA_CONVERTED_AMT = 'FPRREQA_CONVERTED_AMT'
    public static final String REQ_ACC_FIELD_FPRREQA_CLOSED_IND = 'FPRREQA_CLOSED_IND'
    public static final String REQ_ACC_FIELD_FPRREQA_DISC_AMT = 'FPRREQA_DISC_AMT'
    public static final String REQ_ACC_FIELD_FPRREQA_TAX_AMT = 'FPRREQA_TAX_AMT'
    public static final String REQ_ACC_FIELD_FPRREQA_ADDL_CHRG_AMT = 'FPRREQA_ADDL_CHRG_AMT'
    public static final String REQ_ACC_FIELD_FPRREQA_CONVERT_DISC_AMT = 'FPRREQA_CONVERT_DISC_AMT'
    public static final String REQ_ACC_FIELD_FPRREQA_CONVERT_TAX_AMT = 'FPRREQA_CONVERT_TAX_AMT'
    public static final String REQ_ACC_FIELD_FPRREQA_CONVERT_ADDL_CHRG_AMT = 'FPRREQA_CONVERT_ADDL_CHRG_AMT'
    public static final String REQ_ACC_FIELD_FPRREQA_DISC_AMT_PCT = 'FPRREQA_DISC_AMT_PCT'
    public static final String REQ_ACC_FIELD_FPRREQA_ADDL_AMT_PCT = 'FPRREQA_ADDL_AMT_PCT'
    public static final String REQ_ACC_FIELD_FPRREQA_TAX_AMT_PCT = 'FPRREQA_TAX_AMT_PCT'
    public static final String REQ_ACC_FIELD_FPRREQA_DISC_RUCL_CODE = 'FPRREQA_DISC_RUCL_CODE'
    public static final String REQ_ACC_FIELD_FPRREQA_TAX_RUCL_CODE = 'FPRREQA_TAX_RUCL_CODE'
    public static final String REQ_ACC_FIELD_FPRREQA_ADDL_RUCL_CODE = 'FPRREQA_ADDL_RUCL_CODE'
    public static final String REQ_ACC_FIELD_FPRREQA_RUCL_CODE_LIQ = 'FPRREQA_RUCL_CODE_LIQ'
    public static final String REQ_ACC_FIELD_FPRREQA_VERSION = 'FPRREQA_VERSION'
    public static final String REQ_ACC_FIELD_FPRREQA_DATA_ORIGIN = 'FPRREQA_DATA_ORIGIN'

    public static final String REQ_ACC_FIELD_COPY_FPRREQA_SURROGATE_ID = 'FPRREQA_SURROGATE_ID'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_REQH_CODE = 'FPRREQA_REQH_CODE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_ITEM = 'FPRREQA_ITEM'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_SEQ_NUM = 'FPRREQA_SEQ_NUM'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_ACTIVITY_DATE = 'FPRREQA_ACTIVITY_DATE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_USER_ID = 'FPRREQA_USER_ID'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_PCT = 'FPRREQA_PCT'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_AMT = 'FPRREQA_AMT'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_FSYR_CODE = 'FPRREQA_FSYR_CODE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_PERIOD = 'FPRREQA_PERIOD'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_RUCL_CODE = 'FPRREQA_RUCL_CODE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_COAS_CODE = 'FPRREQA_COAS_CODE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_ACCI_CODE = 'FPRREQA_ACCI_CODE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_FUND_CODE = 'FPRREQA_FUND_CODE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_ORGN_CODE = 'FPRREQA_ORGN_CODE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_ACCT_CODE = 'FPRREQA_ACCT_CODE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_PROG_CODE = 'FPRREQA_PROG_CODE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_ACTV_CODE = 'FPRREQA_ACTV_CODE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_LOCN_CODE = 'FPRREQA_LOCN_CODE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_SUSP_IND = 'FPRREQA_SUSP_IND'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_NSF_SUSP_IND = 'FPRREQA_NSF_SUSP_IND'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_CANCEL_IND = 'FPRREQA_CANCEL_IND'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_CANCEL_DATE = 'FPRREQA_CANCEL_DATE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_PROJ_CODE = 'FPRREQA_PROJ_CODE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_APPR_IND = 'FPRREQA_APPR_IND'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_NSF_OVERRIDE_IND = 'FPRREQA_NSF_OVERRIDE_IND'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_ABAL_IND = 'FPRREQA_ABAL_IND'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_CONVERTED_AMT = 'FPRREQA_CONVERTED_AMT'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_CLOSED_IND = 'FPRREQA_CLOSED_IND'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_DISC_AMT = 'FPRREQA_DISC_AMT'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_TAX_AMT = 'FPRREQA_TAX_AMT'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_ADDL_CHRG_AMT = 'FPRREQA_ADDL_CHRG_AMT'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_CONVERT_DISC_AMT = 'FPRREQA_CONVERT_DISC_AMT'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_CONVERT_TAX_AMT = 'FPRREQA_CONVERT_TAX_AMT'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_CONVERT_ADDL_CHRG_AMT = 'FPRREQA_CONVERT_ADDL_CHRG_AMT'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_DISC_AMT_PCT = 'FPRREQA_DISC_AMT_PCT'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_ADDL_AMT_PCT = 'FPRREQA_ADDL_AMT_PCT'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_TAX_AMT_PCT = 'FPRREQA_TAX_AMT_PCT'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_DISC_RUCL_CODE = 'FPRREQA_DISC_RUCL_CODE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_TAX_RUCL_CODE = 'FPRREQA_TAX_RUCL_CODE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_ADDL_RUCL_CODE = 'FPRREQA_ADDL_RUCL_CODE'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_RUCL_CODE_LIQ = 'FPRREQA_RUCL_CODE_LIQ'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_VERSION = 'FPRREQA_VERSION'
    public static final String REQ_ACC_FIELD_COPY_FPRREQA_DATA_ORIGIN = 'FPRREQA_DATA_ORIGIN'

    public static final String ERROR_MESSAGE_MISSING_REQUISITION_ACCOUNTING = 'missing.requisition.accounting'

    public static final boolean TRUE = true
    public static final boolean FALSE = false

    public static final String ERROR_MESSAGE_ITEM_SEQUENCE_REQUIRED = 'item.sequence.required'

    public static final
    String SUCCESS_MESSAGE_CREATE_REQUISITION_ACCOUNTING = 'net.hedtech.banner.finance.requisition.create.requisition.accounting.success'
    public static final
    String SUCCESS_MESSAGE_DELETE_REQUISITION_ACCOUNTING = 'net.hedtech.banner.finance.requisition.delete.requisition.accounting.success'
    public static final
    String SUCCESS_MESSAGE_UPDATE_REQUISITION_ACCOUNTING = 'net.hedtech.banner.finance.requisition.update.requisition.accounting.success'

    /** Constants for Requisition Information */
    public static
    final String REQUISITION_COUNT_QRY_1 = 'select count(reqInfo.surrogate_id) as numberOfRows, status  from '
    public static final String REQUISITION_COUNT_QRY_2 = ' reqInfo where reqInfo.status in ( '
    public static final String REQUISITION_COUNT_QRY_3 = ") and reqinfo.user_id = '"
    public static final String REQUISITION_COUNT_QRY_4 = " group by reqInfo.status"

    public static final String SINGLE_QUOTES = "'"
    public static final String COMMA = ','
    public static final String VIEW_FVQ_REQ_DASHBOARD_INFO = 'FVQ_REQ_DASHBOARD_INFO'
    public static final String ERROR_MESSAGE_INVALID_BUCKET_TYPE = 'invalid.bucket.type'

    public static final String REQUISITION_INFO_FIELD_SURROGATE_ID = 'SURROGATE_ID'
    public static final String REQUISITION_INFO_FIELD_AMOUNT = 'AMOUNT'
    public static final String REQUISITION_INFO_FIELD_VENDOR_NAME = 'VENDOR_NAME'
    public static final String REQUISITION_INFO_FIELD_FPBREQH_TRANS_DATE = 'TRANS_DATE'
    public static final String REQUISITION_INFO_FIELD_FPBREQH_USER_ID = 'USER_ID'
    public static final String REQUISITION_INFO_FIELD_FPBREQH_ORIGIN_CODE = 'ORIGIN_CODE'
    public static final String REQUISITION_INFO_FIELD_FPBREQH_CURR_CODE = 'CURR_CODE'
    public static final String REQUISITION_INFO_FIELD_VERSION = 'VERSION'
    public static final String REQUISITION_INFO_FIELD_FPBREQH_VEND_PIDM = 'VEND_PIDM'
    public static final String REQUISITION_INFO_FIELD_FPBREQH_ACTIVITY_DATE = 'ACTIVITY_DATE'
    public static final String REQUISITION_INFO_FIELD_FPBREQH_FPBREQH_CODE = 'CODE'
    public static final String REQUISITION_INFO_FIELD_STATUS = 'STATUS'
    public static final String REQUISITION_INFO_FINDER_BY_STATUS = 'RequisitionInformation.listRequisitionByStatus'
    public static
    final String REQUISITION_INFO_COUNT_FINDER_BY_STATUS = 'RequisitionInformation.countRequisitionsByStatus'
    public static
    final String REQUISITION_INFO_FINDER_BY_CODE_USER = 'RequisitionInformation.countRequisitionsByCodeAndUser'
    public static final String REQUISITION_INFO_FINDER_PARAM_STATUS = 'status'
    public static final String REQUISITION_INFO_FINDER_PARAM_REQ_CODE = 'requisitionCode'
    public static final String REQUISITION_INFO_FINDER_PARAM_STATUS_PARAM_USER_ID = 'userId'
    public static final String REQUEST_PARAM_BUCKET_TYPES = 'bucketTypes'
    public static
    final String REQUISITION_INFO_FINDER_BY_SEARCH_PARAM = 'RequisitionInformation.searchRequisitionsBySearchParam'
    public static
    final String REQUISITION_INFO_COUNT_FINDER_BY_SEARCH_PARAM = 'RequisitionInformation.countRequisitionsBySearchParam'
    public static final String REQUISITION_INFO_SEARCH_PARAM = 'searchParam'
    public static
    final String REQUISITION_INFO_SEARCH_BY_TRANSACTION_DATE = 'RequisitionInformation.listRequisitionsByTransactionDate'
    public static
    final String REQUISITION_INFO_SEARCH_COUNT_FINDER_BY_TRANSACTION_DATE = 'RequisitionInformation.countRequisitionsByTransactionDate'
    public static final String REQUISITION_INFO_USER_NAME = 'GRAILS'
    public static final String REQUISITION_INFO_SEARCH_PARAM_TRIMMED_SPACES = 'trimmedSearchParam'
    /** Properties keys for Requisition Information status  **/
    public static final String REQUISITION_INFO_STATUS_DRAFT = 'draft'
    public static final String REQUISITION_INFO_STATUS_DISAPPROVED = 'disapproved'
    public static final String REQUISITION_INFO_STATUS_PENDING = 'pending'
    public static final String REQUISITION_INFO_STATUS_COMPLETED = 'completed'
    public static final String REQUISITION_INFO_STATUS_ASSIGNED_TO_BUYER = 'assignedToBuyer'
    public static final String REQUISITION_INFO_STATUS_CONVERTED_TO_PO = 'convertedToPo'
    public static
    final String REQUISITION_INFO_FINDER_BY_SEARCH_PARAM_AND_STATUS = 'RequisitionInformation.searchRequisitionsByStatusAndSearchParam'
    public static
    final String REQUISITION_INFO_COUNT_FINDER_BY_SEARCH_PARAM_AND_STATUS = 'RequisitionInformation.countRequisitionsByStatusAndSearchParam'
    public static
    final String REQUISITION_INFO_SEARCH_BY_TRANSACTION_DATE_AND_STATUS = 'RequisitionInformation.listRequisitionsByTransactionDateAndStatus'
    public static
    final String REQUISITION_INFO_SEARCH_COUNT_FINDER_BY_TRANSACTION_DATE_AND_STATUS = 'RequisitionInformation.countRequisitionsByTransactionDateAndStatus'
    public static
    final String EMPTY_SEARCH_REQUISITION_INFO_MESSAGE = 'net.hedtech.banner.finance.requisition.search.requisition.info.failure'

    /** Constants for listing and search Requisitions */
    public static final String REQUISITION_LIST_BUCKET_DRAFT = 'draft'
    public static final String REQUISITION_LIST_BUCKET_PENDING = 'pending'
    public static final String REQUISITION_LIST_BUCKET_COMPLETE = 'completed'
    public static final String REQUISITION_LIST_BUCKET_ALL = 'all'
    public static final String REQUEST_PARAM_ACCOUNTING_ITEM = 'item'
    public static final String REQUEST_PARAM_ACCOUNTING_SEQUENCE = 'sequence'

    /** Constants for Requisition Summary */
    public static final String REQUISITION_SUMMARY_VIEW = 'FVQ_REQ_SUMMARY'
    public static final String REQUISITION_SUMMARY_FIELD_FPBREQH_VERSION = 'FPBREQH_VERSION'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_ITEM = 'FPRREQA_ITEM'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_SEQ_NUM = 'FPRREQA_SEQ_NUM'
    public static final String REQUISITION_SUMMARY_FIELD_FPBREQH_SINGLE_ACCTG_IND = 'FPBREQH_SINGLE_ACCTG_IND'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_PCT = 'FPRREQA_PCT'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_AMT = 'FPRREQA_AMT'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_COAS_CODE = 'FPRREQA_COAS_CODE'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_ACCI_CODE = 'FPRREQA_ACCI_CODE'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_FUND_CODE = 'FPRREQA_FUND_CODE'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_ORGN_CODE = 'FPRREQA_ORGN_CODE'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_ACCT_CODE = 'FPRREQA_ACCT_CODE'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_ACTV_CODE = 'FPRREQA_ACTV_CODE'
    public static final String REQUISITION_SUMMARY_FIELD_SHIP_TO_CODE = 'FPBREQH_SHIP_CODE'
    public static final String REQUISITION_SUMMARY_FIELD_FPBREQH_USER_ID = 'FPBREQH_USER_ID'
    public static final String REQUISITION_SUMMARY_FIELD_FPBREQH_CURR_CODE = 'FPBREQH_CURR_CODE'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_PROJ_CODE = 'FPRREQA_PROJ_CODE'
    public static final String REQUISITION_SUMMARY_FIELD_FPBREQH_ATTENTION_TO = 'FPBREQH_ATTENTION_TO'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_PROG_CODE = 'FPRREQA_PROG_CODE'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_LOCN_CODE = 'FPRREQA_LOCN_CODE'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_DISC_AMT = 'FPRREQA_DISC_AMT'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_ADDL_CHRG_AMT = 'FPRREQA_ADDL_CHRG_AMT'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_TAX_AMT = 'FPRREQA_TAX_AMT'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQD_COMM_CODE = 'FPRREQD_COMM_CODE'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQD_COMM_DESC = 'FPRREQD_COMM_DESC'
    public static final String REQUISITION_SUMMARY_FIELD_FTVCOMM_DESC = 'FTVCOMM_DESC'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQD_QTY = 'FPRREQD_QTY'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQD_ITEM = 'FPRREQD_ITEM'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQD_DISC_AMT = 'FPRREQD_DISC_AMT'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQD_ADDL_CHRG_AMT = 'FPRREQD_ADDL_CHRG_AMT'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQD_TAX_AMT = 'FPRREQD_TAX_AMT'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQD_UNIT_PRICE = 'FPRREQD_UNIT_PRICE'
    public static final String REQUISITION_SUMMARY_FIELD_FPBREQH_CODE = 'FPBREQH_CODE'
    public static final String REQUISITION_SUMMARY_FIELD_FPBREQH_VEND_PIDM = 'FPBREQH_VEND_PIDM'
    public static final String REQUISITION_SUMMARY_FIELD_FPBREQH_ATYP_SEQ_NUM = 'FPBREQH_ATYP_SEQ_NUM'
    public static final String REQUISITION_SUMMARY_FIELD_FPBREQH_ATYP_CODE = 'FPBREQH_ATYP_CODE'
    public static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRIDEN_ID = 'FPVVEND_SPRIDEN_ID'
    public static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRIDEN_LAST_NAME = 'FPVVEND_SPRIDEN_LAST_NAME'
    public static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_STREET_LINE1 = 'FPVVEND_SPRADDR_STREET_LINE1'
    public static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_STREET_LINE2 = 'FPVVEND_SPRADDR_STREET_LINE2'
    public static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_STREET_LINE3 = 'FPVVEND_SPRADDR_STREET_LINE3'
    public static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_ZIP = 'FPVVEND_SPRADDR_ZIP'
    public static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_STAT_CODE = 'FPVVEND_SPRADDR_STAT_CODE'
    public static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_CITY = 'FPVVEND_SPRADDR_CITY'
    public static final String REQUISITION_SUMMARY_FIELD_FPBREQH_SURROGATE_ID = 'FPBREQH_SURROGATE_ID'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQD_SURROGATE_ID = 'FPRREQD_SURROGATE_ID'
    public static final String REQUISITION_SUMMARY_FIELD_FPRREQA_SURROGATE_ID = 'FPRREQA_SURROGATE_ID'
    public static final String REQUISITION_SUMMARY_FIELD_FPVVEND_PHONE_NUMBER = 'FPVVEND_PHONE_NUMBER'
    public static final String REQUISITION_SUMMARY_FIELD_FPVVEND_PHONE_EXT = 'FPVVEND_PHONE_EXT'
    public static final String REQUISITION_SUMMARY_FIELD_FPVVEND_FAX = 'FPVVEND_FAX'
    public static final String REQUISITION_SUMMARY_FIELD_FPVVEND_PHONE_AREA = 'FPVVEND_PHONE_AREA'

    public static
    final String REQUISITION_SUMMARY_FINDER_BY_REQUEST_CODE = 'RequisitionSummary.FetchRequisitionSummaryForRequestCode'

    /** Copy requisition **/
    public static final String NEXT_DOC_CODE = 'nextDocCode'
    public static final String OLD_DOC_CODE = 'oldDocCode'
    public static
    final String ERROR_MESSAGE_COMPLETED_REQUISITION_IS_REQUIRED = 'missing.requisition.complemented.requisition.required'
    public static
    final String ERROR_MESSAGE_ERROR_WHILE_COPY = 'error.copy.requisition'
    public static final String COPY_REQUISITION_HEADER_COPIED_FROM = 'COPIED FROM '
    public static
    final String SUCCESS_MESSAGE_COPY_REQUISITION_HEADER = 'net.hedtech.banner.finance.requisition.copy.requisition.header.success'

    /** Date format error **/
    public static final String ERROR_MESSAGE_INVALID_DATE_FORMAT = 'default.invalid.date.format'

    /** Recall requisition **/
    public static
    final String ERROR_MESSAGE_RECALL_REQUISITION_PENDING_REQ_IS_REQUIRED = 'missing.requisition.recall.requisition.pendingReq.required'
    public static
    final String SUCCESS_MESSAGE_RECALL_REQUISITION = 'net.hedtech.banner.finance.requisition.recall.requisition.success'

    /** Constant variable for Domain class for Unapproved Documents Table FOBUAPP **/
    public static final String FOBUAPP = 'FOBUAPP'
    public static final String FINANCE_UNAPPROVED_DOCUMENT_SEQ_GEN = 'FOBUAPP_SEQ_GEN'
    public static final String FINANCE_UNAPPROVED_DOCUMENT_SURROGATE_ID_SEQUENCE = 'FOBUAPP_SURROGATE_ID_SEQUENCE'
    public static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_SURROGATE_ID = 'FOBUAPP_SURROGATE_ID'
    public static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_DOC_CODE = 'FOBUAPP_DOC_CODE'
    public static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_ACTIVITY_DATE = 'FOBUAPP_ACTIVITY_DATE'
    public static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_CHG_SEQ_NUM = 'FOBUAPP_CHG_SEQ_NUM'
    public static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_DATA_ORIGIN = 'FOBUAPP_DATA_ORIGIN'
    public static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_DOC_AMT = 'FOBUAPP_DOC_AMT'
    public static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_ORIG_USER = 'FOBUAPP_ORIG_USER'
    public static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_STATUS_IND = 'FOBUAPP_STATUS_IND'
    public static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_SUBMISSION_NUMBER = 'FOBUAPP_SUBMISSION_NUMBER'
    public static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_USER_CODE = 'FOBUAPP_USER_CODE'
    public static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_USER_ID = 'FOBUAPP_USER_ID'
    public static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_VERSION = 'FOBUAPP_VERSION'
    public static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_VPDI_CODE = 'FOBUAPP_VPDI_CODE'
    public static
    final String FINANCE_UNAPPROVED_DOCUMENT_QUERY_NAME_FIND_BY_DOCUMENT_CODE = 'FinanceUnapprovedDocument.fetchByDocumentCode'
    public static final String FINANCE_QUERY_PARAM_DOCUMENT_CODE = 'documentCode'
    public static final String FINANCE_QUERY_PARAM_DOCUMENT_TYPE_CODE = 'documentType'

    /** Constant variable for Domain class for Unapproved Documents Table FOBAPPH **/
    public static final String FOBAPPH = 'FOBAPPH'
    public static final String FINANCE_APPROVAL_HISTORY_SEQ_GEN = 'FOBAPPH_SEQ_GEN'
    public static final String FINANCE_APPROVAL_HISTORY_SURROGATE_ID_SEQUENCE = 'FOBAPPH_SURROGATE_ID_SEQUENCE'
    public static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_SURROGATE_ID = 'FOBAPPH_SURROGATE_ID'
    public static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_ACTIVITY_DATE = 'FOBAPPH_ACTIVITY_DATE'
    public static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_CHG_SEQ_NUM = 'FOBAPPH_CHG_SEQ_NUM'
    public static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_DATA_ORIGIN = 'FOBAPPH_DATA_ORIGIN'
    public static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_DOC_CODE = 'FOBAPPH_DOC_CODE'
    public static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_QUEUE_ID = 'FOBAPPH_QUEUE_ID'
    public static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_QUEUE_LEVEL = 'FOBAPPH_QUEUE_LEVEL'
    public static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_SEQ_NUM = 'FOBAPPH_SEQ_NUM'
    public static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_SUBMISSION_NUMBER = 'FOBAPPH_SUBMISSION_NUMBER'
    public static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_USER_ID = 'FOBAPPH_USER_ID'
    public static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_VERSION = 'FOBAPPH_VERSION'
    public static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_VPDI_CODE = 'FOBAPPH_VPDI_CODE'
    public static
    final String FINANCE_APPROVAL_HISTORY_QUERY_NAME_FIND_BY_DOCUMENT_CODE = 'FinanceApprovalHistory.fetchByDocumentCode'
    public static final String FINANCE_APPROVAL_HISTORY_QUERY_NAME_FIND_BY_DOC_CODE_DOC_TYPE='FinanceApprovalHistory.fetchByDocumentCodeAndDocType'
    public static final String FINANCE_APPROVAL_HISTORY_QUERY_ID_DENY = 'DENY'
    public static final int FINANCE_APPROVAL_HISTORY_QUERY_LEVEL_ZERO = 0

    /** Constant variable for Domain class for Unapproved Documents Table FOBAINP **/
    public static final String FOBAINP = 'FOBAINP'
    public static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_SURROGATE_ID = 'FOBAINP_SURROGATE_ID'
    public static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_ACTIVITY_DATE = 'FOBAINP_ACTIVITY_DATE'
    public static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_APPROVAL_SEQUENCE = 'FOBAINP_APPROVAL_SEQUENCE'
    public static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_CHG_SEQ_NUM = 'FOBAINP_CHG_SEQ_NUM'
    public static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_DATA_ORIGIN = 'FOBAINP_DATA_ORIGIN'
    public static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_DOC_NUM = 'FOBAINP_DOC_NUM'
    public static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_DOC_TYPE = 'FOBAINP_DOC_TYPE'
    public static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_LEVEL = 'FOBAINP_LEVEL'
    public static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_QUEUE_ID = 'FOBAINP_QUEUE_ID'
    public static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_SUBMISSION_NUM = 'FOBAINP_SUBMISSION_NUM'
    public static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_USER_ID = 'FOBAINP_USER_ID'
    public static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_VERSION = 'FOBAINP_VERSION'
    public static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_VPDI_CODE = 'FOBAINP_VPDI_CODE'
    public static
    final String FINANCE_APPROVAL_IN_PROCESS_QUERY_NAME_FIND_BY_DOCUMENT_NUMBER = 'FinanceApprovalsInProcess.findByDocumentNumber'
    public static
    final String FINANCE_APPROVAL_IN_PROCESS_QUERY_NAME_FIND_BY_DOC_NUMBER_DOC_TYPE = 'FinanceApprovalInProcess.findByDocumentNumberAndDocumentType'
    public static final String FINANCE_APPROVAL_IN_PROCESS_QUERY_PARAM_DOCUMENT_NUMBER = 'documentNumber'

    /** FOBTEXT - FinanceText Domain constants **/
    public static final String FINANCE_TEXT_TABLE = 'FOBTEXT'
    public static final String FINANCE_TEXT_SEQ_GEN = 'FOBTEXT_SEQ_GEN'
    public static final String FINANCE_TEXT_SURROGATE_ID_SEQUENCE = 'FOBTEXT_SURROGATE_ID_SEQUENCE'

    public static final String FINANCE_TEXT_FIELD_FOBTEXT_SURROGATE_ID = 'FOBTEXT_SURROGATE_ID'
    public static final String FINANCE_TEXT_FIELD_FOBTEXT_ACTIVITY_DATE = 'FOBTEXT_ACTIVITY_DATE'
    public static final String FINANCE_TEXT_FIELD_FOBTEXT_CHG_SEQ_NUM = 'FOBTEXT_CHG_SEQ_NUM'
    public static final String FINANCE_TEXT_FIELD_FOBTEXT_CLA_NUM = 'FOBTEXT_CLA_NUM'
    public static final String FINANCE_TEXT_FIELD_FOBTEXT_CODE = 'FOBTEXT_CODE'
    public static final String FINANCE_TEXT_FIELD_FOBTEXT_DATA_ORIGIN = 'FOBTEXT_DATA_ORIGIN'
    public static final String FINANCE_TEXT_FIELD_FOBTEXT_DTYP_SEQ_NUM = 'FOBTEXT_DTYP_SEQ_NUM'
    public static final String FINANCE_TEXT_FIELD_FOBTEXT_ITEM = 'FOBTEXT_ITEM'
    public static final String FINANCE_TEXT_FIELD_FOBTEXT_PIDM = 'FOBTEXT_PIDM'
    public static final String FINANCE_TEXT_FIELD_FOBTEXT_PRT_IND = 'FOBTEXT_PRT_IND'
    public static final String FINANCE_TEXT_FIELD_FOBTEXT_SEQ_NUM = 'FOBTEXT_SEQ_NUM'
    public static final String FINANCE_TEXT_FIELD_FOBTEXT_TEXT = 'FOBTEXT_TEXT'
    public static final String FINANCE_TEXT_FIELD_FOBTEXT_USER_ID = 'FOBTEXT_USER_ID'
    public static final String FINANCE_TEXT_FIELD_FOBTEXT_VERSION = 'FOBTEXT_VERSION'
    public static final String FINANCE_TEXT_FIELD_FOBTEXT_VPDI_CODE = 'FOBTEXT_VPDI_CODE'

    static final int FINANCE_DTYP_SEQ_NUM_COMMODITY = 14

    public static
    final String FINANCE_TEXT_NAMED_QUERY_GET_FINANCE_TEXT_BY_CODE_AND_SEQUENCE_NO = 'FinanceText.getFinanceTextByCodeAndItemNumber'
    public static final String FINANCE_TEXT_QUERY_PARAM_DTYP_SEQ_NUM = 'dtypSeqNum'
    public static final String FINANCE_TEXT_QUERY_PARAM_TEXT_CODE = 'textCode'
    public static final String FINANCE_TEXT_QUERY_PARAM_TEXT_ITEM = 'textItem'
    public static final String FINANCE_TEXT_QUERY_PARAM_PRINT_INDICATOR = 'printOptionIndicator'
    public static
    final String FINANCE_TEXT_NAMED_QUERY_LIST_ALL_FINANCE_TEXT_BY_CODE = 'FinanceText.listAllFinanceTextByCode'
    public static
    final String FINANCE_TEXT_NAMED_QUERY_GET_FINANCE_TEXT_BY_CODE_AND_SEQUENCE_NO_AND_PRINT_IND = 'FinanceText.getFinanceTextByCodeAndItemNumberAndPrintInd'
    public static
    final String FINANCE_TEXT_NAMED_QUERY_LIST_HEADER_LEVEL_TEXT_BY_CODE_AND_PRINT_OPTION_IND = 'FinanceText.listHeaderLevelTextByCodeAndPrintOptionInd'
    public static final String FINANCE_TEXT_NAMED_QUERY_LIST_HEADER_LEVEL_TEXT_BY_CODE_AND_PRINT_OPTION_IND_DTYP_SEQ='FinanceText.listHeaderLevelTextByCodeAndPrintOptionIndAndDtypeSeq'
    public static
    final String FINANCE_TEXT_NAMED_QUERY_LIST_HEADER_LEVEL_TEXT_BY_CODE = 'FinanceText.listHeaderLevelTextByCode'
    public static
    final String FINANCE_TEXT_NAMED_QUERY_GET_FINANCE_TEXT_BY_DTYP_SEQ_AND_CODE_AND_PRINT_IND = 'FinanceText.getFinanceTextByDtypSeqAndCodeAndPrintInd'

    public static final int FINANCE_TEXT_TEXT_LENGTH = 50
    public static final int FINANCE_TEXT_SEQUENCE_NUMBER_INCREMENT = 10
    public static final int FINANCE_TEXT_DOCUMENT_TYPE_SEQ_NUMBER_REQUISITION = 1

    /** Constants for Domain FinanceBuyerVerification **/
    public static final String FTVBUYR_TABLE = 'FVQ_REQ_BUYER_VERIFICATION'
    public static
    final String FINANCE_BUYER_VERIFICATION_NAMED_QUERY_FIND_BY_REQUEST_CODE = 'FinanceBuyerVerification.findByDocumentCode'
    public static final String FINANCE_BUYER_VERIFICATION_FTVBUYR_REQUEST_CODE = 'REQUEST_CODE'
    public static final String FINANCE_BUYER_VERIFICATION_FTVBUYR_CODE = 'BUYER_CODE'
    public static final String FINANCE_BUYER_VERIFICATION_FTVBUYR_NAME = 'BUYER_NAME'
    public static final String FINANCE_BUYER_VERIFICATION_FTVBUYR_VERSION = 'VERSION'
    public static final String FINANCE_BUYER_VERIFICATION_BUYER = 'buyer'
    public static final String FINANCE_BUYER_VERIFICATION_QUERY_PARAM_REQUEST_CODE = 'requestCode'

    /** Constants for Domain FinancePOStatusExtension **/
    static final String FPBPOHD_TABLE = 'FPBPOHD'
    static
    final String FINANCE_PO_STATUS_EXTENSION_NAMED_QUERY_FIND_BY_POHD_CODE = 'FinancePOStatusExtension.fetchByPOHDCode'
    static final String FINANCE_PO_STATUS_EXTENSION_FPBPOHD_SURROGATE_ID = 'FPBPOHD_SURROGATE_ID'
    static final String FINANCE_PO_STATUS_EXTENSION_FPBPOHD_APPR_IND = 'FPBPOHD_APPR_IND'
    static final String FINANCE_PO_STATUS_EXTENSION_FPBPOHD_COMPLETE_IND = 'FPBPOHD_COMPLETE_IND'
    static final String FINANCE_PO_STATUS_EXTENSION_FPBPOHD_POHD_CODE = 'FPBPOHD_CODE'
    static final String FINANCE_PO_STATUS_EXTENSION_FPBPOHD_VERSION = 'FPBPOHD_VERSION'
    static final String FINANCE_PO_STATUS_EXTENSION_QUERY_PARAM_POHD_CODE = 'pohdCode'
    static
    final String FINANCE_PO_STATUS_EXTENSION_CONVERTED_TO_PO_PENDING = 'purchaseRequisition.status.convertedToPo.pendingApproval'
    static
    final String FINANCE_PO_STATUS_EXTENSION_CONVERTED_TO_PO_COMPLETED = 'purchaseRequisition.status.convertedToPo.completed'
    static
    final String FINANCE_PO_STATUS_EXTENSION_CONVERTED_TO_PO_DRAFT = 'purchaseRequisition.status.convertedToPo.draft'
    /** Constants for Domain FinanceRequestPOVerification **/
    public static final String FTVRQPO_TABLE = 'FTVRQPO'
    public static
    final String FINANCE_REQUEST_PO_VERIFICATION_NAMED_QUERY_FIND_BY_REQ_CODE = 'FinanceRequestPOVerification.fetchByRequestCode'
    public static final String FINANCE_REQUEST_PO_VERIFICATION_FTVRQPO_SURROGATE_ID = 'FTVRQPO_SURROGATE_ID'
    public static final String FINANCE_REQUEST_PO_VERIFICATION_FTVRQPO_REQD_CODE = 'FTVRQPO_REQD_CODE'
    public static final String FINANCE_REQUEST_PO_VERIFICATION_FTVRQPO_POHD_CODE = 'FTVRQPO_POHD_CODE'
    public static final String FINANCE_REQUEST_PO_VERIFICATION_FTVRQPO_VERSION = 'FTVRQPO_VERSION'
    public static final String FINANCE_REQUEST_PO_VERIFICATION_QUERY_PARAM_REQUEST_CODE = 'requestCode'
    public static final String FINANCE_PROCUREMENT_PDF_CONTENT_TYPE = 'application/pdf'
    public static final String FINANCE_PROCUREMENT_PDF_CONTENT = 'Content-disposition'
    public static final String FINANCE_PROCUREMENT_PDF_INLINE = 'inline;filename='
    public static final String FINANCE_PROCUREMENT_USER_AGENT = 'User-Agent'
    public static final String FINANCE_PROCUREMENT_ANDROID_DEVICE = 'Android'
    public static final String FINANCE_PROCUREMENT_PDF_ATTACHMENT = 'attachment;filename='
    public static final String FINANCE_PROCUREMENT_PDF_HEADER_EXPIRES = 'Expires'
    public static final String FINANCE_PROCUREMENT_PDF_HEADER_PRAGMA = 'Pragma'
    public static final int HTTP_ERROR = 404

    public static final String FINANCE_PROCUREMENT_PDF_HEADER_PRAGMA_PUBLIC = 'public'
    public static
    final String FINANCE_PROCUREMENT_PDF_HEADER_CACHE_CONTROL = 'Cache-Control'
    public static
    final String FINANCE_PROCUREMENT_PDF_HEADER_CACHE_CONTROL_MUST_RE_VALIDATE = 'must-revalidate, post-check=0, pre-check=0'

    /** Recall requisition **/
    public static
    final String ERROR_MESSAGE_DELETE_REQUISITION_DRAFT_OR_DISAPPROVED_REQ_IS_REQUIRED = 'missing.requisition.delete.requisition.draftOrDisapproved.required'

    /** Constants for FVQ_REQ_APPROVER_LIST View **/
    public static final String FVQ_REQ_APPROVER_LIST_VIEW = 'FVQ_REQ_APPROVER_LIST'
    public static
    final String FVQ_REQ_APPROVER_LIST_QUERY_FIND_BY_DOC_CODE = 'FinancePendingApproverList.findByDocumentCode'
    public static final String FINANCE_PENDING_APPROVER_LIST_APPROVER_NAME = 'APPROVER_NAME'
    public static final String FINANCE_PENDING_APPROVER_LIST_DESCRIPTION = 'DESCRIPTION'
    public static final String FINANCE_PENDING_APPROVER_LIST_DOC_CODE = 'DOC_CODE'
    public static final String FINANCE_PENDING_APPROVER_LIST_QUEUE_ID = 'QUEUE_ID'
    public static final String FINANCE_PENDING_APPROVER_LIST_VERSION = 'VERSION'
    public static final String FINANCE_PENDING_APPROVER_LIST_QUERY_PARAM_DOC_CODE = 'documentCode'

    /** Constants for document **/
    public static final String BDM_DATE_FORMAT = 'yyyy-MM-dd HH:mm:ss'
    public static final String BDM_DOCUMENT_ID = 'DOCUMENT ID'
    public static final String BDM_BANNER_DOC_TYPE = 'BANNER DOC TYPE'
    public static final String BDM_DOCUMENT_TYPE = 'DOCUMENT TYPE'
    public static final String BDM_TRANSACTION_DATE = 'TRANSACTION DATE'
    public static final String BDM_VENDOR_ID = 'VENDOR ID'
    public static final String BDM_VENDOR_NAME = 'VENDOR NAME'
    public static final String BDM_FIRST_NAME = 'FIRST NAME'
    public static final String BDM_PIDM = 'PIDM'
    public static final String BDM_ROUTING_STATUS = 'ROUTING STATUS'
    public static final String BDM_ACTIVITY_DATE = 'ACTIVITY DATE'
    public static final String USER_NAME = 'USER_NAME'
    public static final String BDM_DISPOSITION_DATE = 'DISPOSITION DATE'
    public static final String IS_BDM_INSTALLED = 'isBdmInstalled'
    public static final String BDM_ERROR_MESSAGE = 'isBdmInstalled'
    public static final String BDM_FILE = 'file'
    public static final String BDM_FILE_UPLOAD_ERROR_MESSAGE = 'uploaded.document.is.empty'
    public static final String BDM_MEP = 'mep'
    public static final String BDM_JSON_DEEP = 'deep'
    public static final String BDM_USER_NAME = 'USER_NAME'
    public static final String BDM_OWNER_PIDM = 'OWNER_PIDM'
    public static final String BDM_DOC_TYPE_CODE = 'docTypeCode'
    public static final String BDM_DOC_TYPE_DESC = 'docTypeDesc'
    public static final String BDM_DOCUMENT_NAME = 'DOCUMENT NAME'
    public static final String BDM_CREATE_NAME = 'CREATE NAME'
    public static final String COMMON_MATCH_SQL = "SELECT e.ETVDTYP_CODE AS code, e.ETVDTYP_DESC AS DESCRIPTION \
                          FROM ETVDTYP e, otgmgr.ul506_2 \
                          WHERE e.ETVDTYP_CODE = otgmgr.ul506_2.item \
                          AND (UPPER(e.ETVDTYP_CODE) LIKE :docTypeCode OR  UPPER(e.ETVDTYP_DESC) LIKE :docTypeDesc)"

    public static final String ERROR_MESSAGE_BDM_NOT_INSTALLED = 'bdm.not.installed'

    /** Constants for PDF*/
    public static final String FOP_CONFIG_FILENAME_DEFAULT = 'fop-config.xml'
    public static final String XSL_FILE_EXTENSION = 'xsl'
    public static final String BASE_DIR = 'fop'
    public static final String PDF_NAME = 'purchaseRequisition'
    public static final String LOG_FILE = 'ellucian-logo.png'
    public static final String PDF_FILE_NAME = 'banner.finance.procurement.requisition.pdf.filename'
    public static final String PDF_REPLACE_STRING = '\\s+'
    public static final String PDF_NULL_VALUE = '>null</'
    public static final String PDF_NULL_REPLACE_VALUE = '></'
    public static final String DOT = '.'
    public static final String LANGUAGE_DIRECTION = 'default.language.direction'
    public static final String PDF_HEADER_TOP = 'TOP'
    public static final String PDF_WRITE_DIRECTION_LEFT = 'ltr'
    public static final String PDF_DIRECTION_LEFT = 'LEFT'
    public static final String PDF_DIRECTION_RIGHT = 'RIGHT'

    public static final String LABELS_TITLE = 'banner.finance.procurement.requisition.pdf.label.title'
    public static final String LABELS_REQUISITION_NO = 'banner.finance.procurement.requisition.pdf.label.number'
    public static final String LABELS_REQUESTOR = 'banner.finance.procurement.requisition.pdf.label.requestor'
    public static
    final String LABELS_TRANSACTION_DATE = 'banner.finance.procurement.requisition.pdf.label.transactionDate'
    public static final String LABELS_PHONE = 'banner.finance.procurement.requisition.pdf.label.phone'
    public static final String LABELS_EXTENSION = 'banner.finance.procurement.requisition.pdf.label.extension'
    public static final String LABELS_EMAIL = 'banner.finance.procurement.requisition.pdf.label.email'
    public static final String LABELS_DELIVERY_DATE = 'banner.finance.procurement.requisition.pdf.label.deliveryDate'
    public static final String LABELS_ORGANIZATION = 'banner.finance.procurement.requisition.pdf.label.organization'
    public static final String LABELS_STATUS = 'banner.finance.procurement.requisition.pdf.label.status'
    public static final String LABELS_SHIP_TO = 'banner.finance.procurement.requisition.pdf.label.shipTo'
    public static final String LABELS_ADDRESS = 'banner.finance.procurement.requisition.pdf.label.address'
    public static final String LABELS_VENDOR = 'banner.finance.procurement.requisition.pdf.label.vendor'
    public static final String LABELS_ATTENTION_TO = 'banner.finance.procurement.requisition.pdf.label.attentionTo'
    public static final String LABELS_HEADER_COMMENT = 'banner.finance.procurement.requisition.pdf.label.header.comment'
    public static final String LABELS_COMMODITIES = 'banner.finance.procurement.requisition.pdf.label.commodities'
    public static final String LABELS_COMMODITY_ITEM = 'banner.finance.procurement.requisition.pdf.label.commodity.item'
    public static
    final String LABELS_COMMODITY_DESC = 'banner.finance.procurement.requisition.pdf.label.commodity.item.description'
    public static final String LABELS_COMMODITY_UOM = 'banner.finance.procurement.requisition.pdf.label.commodity.uom'
    public static
    final String LABELS_COMMODITY_QUANTITY = 'banner.finance.procurement.requisition.pdf.label.commodity.quantity'
    public static
    final String LABELS_COMMODITY_UNIT_PRICE = 'banner.finance.procurement.requisition.pdf.label.commodity.unitPrice'
    public static
    final String LABELS_COMMODITY_OTHER = 'banner.finance.procurement.requisition.pdf.label.commodity.other'
    public static final String LABELS_COMMODITY_TAX = 'banner.finance.procurement.requisition.pdf.label.commodity.tax'
    public static
    final String LABELS_COMMODITY_TOTAL = 'banner.finance.procurement.requisition.pdf.label.commodity.itemTotal'
    public static final String LABELS_REQUISITION_TYPE = 'banner.finance.procurement.requisition.pdf.label.type'
    public static
    final String LABELS_DOCUMENT_REQUISITION_TYPE = 'banner.finance.procurement.requisition.pdf.label.document.type'
    public static
    final String LABELS_COMMODITY_REQUISITION_TYPE = 'banner.finance.procurement.requisition.pdf.label.commodity.type'
    public static
    final String LABELS_COMMODITY_ITEM_TEXT = 'banner.finance.procurement.requisition.pdf.label.commodity.item.text'
    public static final String LABELS_DISCLAIMER = 'banner.finance.procurement.requisition.pdf.label.disclaimer'
    public static
    final String LABELS_GRAND_TOTAL_COMMODITY = 'banner.finance.procurement.requisition.pdf.label.grand.total.commodity'
    public static
    final String LABELS_ACCOUNTING_DISTRIBUTION = 'banner.finance.procurement.requisition.pdf.label.accounting.distribution'
    public static
    final String LABELS_ACCOUNTING_SEQUENCE = 'banner.finance.procurement.requisition.pdf.label.accounting.sequence'
    public static
    final String LABELS_ACCOUNTING_STRING = 'banner.finance.procurement.requisition.pdf.label.accounting.account.string'
    public static
    final String LABELS_ACCOUNTING_PERCENTAGE = 'banner.finance.procurement.requisition.pdf.label.accounting.distribution.percentage'
    public static
    final String LABELS_ACCOUNTING_TOTAL = 'banner.finance.procurement.requisition.pdf.label.accounting.distribution.total'
    public static
    final String LABELS_GRAND_TOTAL_ACCOUNTING = 'banner.finance.procurement.requisition.pdf.label.grand.total.accounting'
    public static
    final String LABELS_CLA_ACCOUNTING_TOTAL_AT_COMMODITY = 'banner.finance.procurement.requisition.pdf.label.accounting.total'
    public static final String LABELS_COMMODITY = 'banner.finance.procurement.requisition.pdf.label.commodity'
    public static final String LABELS_SUBTITLE = 'banner.finance.procurement.requisition.pdf.label.subtitle'
    public static final String LABELS_CURRENCY = 'banner.finance.procurement.requisition.pdf.label.currency'
    public static final String LABELS_VENDOR_FAX = 'banner.finance.procurement.requisition.pdf.label.vendor.fax'

    /** Constants for General Tickler Table GURTKLR **/
    public static final String GURTKLR_TABLE = 'GURTKLR'
    public static
    final String GURTKLR_NAMED_QUERY_FIND_BY_ITEM_REFERENCE = 'FinanceGeneralTickler.fetchByReferenceNumber'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_SURROGATE_ID = 'GURTKLR_SURROGATE_ID'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_USER_ID = 'GURTKLR_USER_ID'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_CREATOR = 'GURTKLR_CREATOR'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_SEQNO = 'GURTKLR_SEQNO'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_ACTIVITY_DATE = 'GURTKLR_ACTIVITY_DATE'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_COMMENT = 'GURTKLR_COMMENT'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_CONFID_IND = 'GURTKLR_CONFID_IND'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_DATA_ORIGIN = 'GURTKLR_DATA_ORIGIN'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_IDEN_CODE = 'GURTKLR_IDEN_CODE'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_ITEM_REFNO = 'GURTKLR_ITEM_REFNO'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_PIDM = 'GURTKLR_PIDM'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_SOURCE = 'GURTKLR_SOURCE'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_STATUS = 'GURTKLR_STATUS'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_SYSTEM_IND = 'GURTKLR_SYSTEM_IND'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_TODO_DATE = 'GURTKLR_TODO_DATE'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_TODO_TIME = 'GURTKLR_TODO_TIME'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_VERSION = 'GURTKLR_VERSION'
    public static final String FINANCE_GENERAL_TICKLER_GURTKLR_VPDI_CODE = 'GURTKLR_VPDI_CODE'
    public static final String FINANCE_GENERAL_TICKLER_QUERY_PARAM_ITEM_REF_NO = 'itemReferenceNo'

    /** Constants for Finance object sequence Table FOBSEQN **/
    public static final String FINANCE_OBJECT_SEQ = 'FOBSEQN'
    public static final String FINANCE_OBJECT_SEQ_SEQ_GENERATOR = 'FOBSEQN_SEQ_GEN'
    public static final String FINANCE_OBJECT_SEQ_SEQ_FOBSEQN_SURROGATE_ID_SEQUENCE = 'FOBSEQN_SURROGATE_ID_SEQUENCE'
    public static final String FINANCE_OBJECT_SEQ_FIELD_FOBSEQN_SURROGATE_ID = 'FOBSEQN_SURROGATE_ID'
    public static final String FINANCE_OBJECT_SEQ_FIELD_FOBSEQN_SEQNO_TYPE = 'FOBSEQN_SEQNO_TYPE'
    public static final String FINANCE_OBJECT_SEQ_FIELD_FOBSEQN_SEQNO_PREFIX = 'FOBSEQN_SEQNO_PREFIX'
    public static final String FINANCE_OBJECT_SEQ_FIELD_FOBSEQN_MAXSEQNO_7 = 'FOBSEQN_MAXSEQNO_7'
    public static final String FINANCE_OBJECT_SEQ_FIELD_FOBSEQN_VERSION = 'FOBSEQN_VERSION'
    public static final String FINANCE_OBJECT_SEQ_FOBSEQN_SEQNO_TYPE = 'R'

    /** Table name constants for tables **/
    public static final String FPBREQH_TABLE = 'FPBREQH'
    public static final String FPRREQA_TABLE = 'FPRREQA'

    /** Constants for Finance object sequence Table FPRRQTX **/
    public static final String FPRREQD_TABLE = 'FPRREQD'
    public static final String FPRRQTX_TABLE = 'FPRRQTX'
    public static final String FIELD_FPRRQTX_SURROGATE_ID = 'FPRRQTX_SURROGATE_ID'
    public static final String FIELD_FPRRQTX_SEQ_GEN = 'FPRRQTX_SEQ_GEN'
    public static final String FIELD_FPRRQTX_SURROGATE_ID_SEQUENCE = 'FPRRQTX_SURROGATE_ID_SEQUENCE'
    public static final String FIELD_FPRRQTX_REQH_CODE = 'FPRRQTX_REQH_CODE'
    public static final String FIELD_FPRRQTX_ITEM = 'FPRRQTX_ITEM'
    public static final String FIELD_FPRRQTX_PRIORITY_NUM = 'FPRRQTX_PRIORITY_NUM'
    public static final String FIELD_FPRRQTX_TRAT_CODE = 'FPRRQTX_TRAT_CODE'
    public static final String FIELD_FPRRQTX_TAX_AMT = 'FPRRQTX_TAX_AMT'
    public static final String FIELD_FPRRQTX_TAXABLE_AMT = 'FPRRQTX_TAXABLE_AMT'
    public static final String FIELD_FPRRQTX_PAY_TAX_TO = 'FPRRQTX_PAY_TAX_TO'
    public static final String FIELD_FPRRQTX_ACTIVITY_DATE = 'FPRRQTX_ACTIVITY_DATE'
    public static final String FIELD_FPRRQTX_USER_ID = 'FPRRQTX_USER_ID'
    public static final String FIELD_FPRRQTX_DATA_ORIGIN = 'FPRRQTX_DATA_ORIGIN'
    public static final String FIELD_FPRRQTX_VERSION = 'FPRRQTX_VERSION'
}
