/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.common
/**
 * Util class for Finance Procurement module where in this class will holds all common method and variables will be
 * used across the banner-finance-procurement plugin.
 */
class FinanceProcurementConstants {

    static final String USER_PROFILE_KEY = 'userProfile'
    static final String DASHBOARD_PAGE = 'financeDashboard'
    static final String DEFAULT_ACTION = 'dashboard'
    static final String INSTITUTION_BASE_CCY = 'institutionBaseCcy'

    static final String DEFAULT_INDICATOR_YES = 'Y'
    static final String DEFAULT_INDICATOR_NO = 'N'
    static final int ZERO = 0
    static final int HUNDRED = 100
    static final int ONE = 1
    static final String EMPTY_STRING = ''

    static final int DECIMAL_PRECISION = 2
    static final int DECIMAL_PRECISION_PERCENTAGE = 8

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

    public static final String DEFAULT_REQUEST_CODE = 'NEXT'
    public static final String DEFAULT_REQUISITION_ORIGIN = 'SELF_SERVICE'
    public static final String DEFAULT_REQUISITION_TYPE_IND = 'P'
    public static final String DEFAULT_TEST_ORACLE_LOGIN_USER_NAME = 'FIMSPRD'
    public static final String DEFAULT_TEST_ORACLE_LOGIN_USER_PASSWORD = 'u_pick_it'

    public static final String ERROR_MESSAGE_USER_NOT_VALID = 'user.not.valid'
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
    static final String SUCCESS_MESSAGE_DELETE_REQUISITION = 'net.hedtech.banner.finance.requisition.delete.requisition.success'

    /** Constant variable for Purchase Requisition Detail Domain Property column name **/
    static final String FPVREQD_VIEW = 'FV_FPRREQD'
    static final String FIELD_FPRREQD_SURROGATE_ID = 'FPRREQD_SURROGATE_ID'
    static final String FIELD_FPRREQD_SEQ_GEN = 'FPRREQD_SEQ_GEN'
    static final String FIELD_FPRREQD_SURROGATE_ID_SEQUENCE = 'FPRREQD_SURROGATE_ID_SEQUENCE'
    static final String FIELD_FPRREQD_REQH_CODE = 'FPRREQD_REQH_CODE'
    static final String FIELD_FPRREQD_ITEM = 'FPRREQD_ITEM'
    static final String FIELD_FPRREQD_ACTIVITY_DATE = 'FPRREQD_ACTIVITY_DATE'
    static final String FIELD_FPRREQD_USER_ID = 'FPRREQD_USER_ID'
    static final String FIELD_FPRREQD_COMM_CODE = 'FPRREQD_COMM_CODE'
    static final String FIELD_FPRREQD_COMM_DESC = 'FPRREQD_COMM_DESC'
    static final String FIELD_FPRREQD_COAS_CODE = 'FPRREQD_COAS_CODE'
    static final String FIELD_FPRREQD_ORGN_CODE = 'FPRREQD_ORGN_CODE'
    static final String FIELD_FPRREQD_BUYR_CODE = 'FPRREQD_BUYR_CODE'
    static final String FIELD_FPRREQD_QTY = 'FPRREQD_QTY'
    static final String FIELD_FPRREQD_UOMS_CODE = 'FPRREQD_UOMS_CODE'
    static final String FIELD_FPRREQD_UNIT_PRICE = 'FPRREQD_UNIT_PRICE'
    static final String FIELD_FPRREQD_AGRE_CODE = 'FPRREQD_AGRE_CODE'
    static final String FIELD_FPRREQD_REQD_DATE = 'FPRREQD_REQD_DATE'
    static final String FIELD_FPRREQD_SHIP_CODE = 'FPRREQD_SHIP_CODE'
    static final String FIELD_FPRREQD_VEND_PIDM = 'FPRREQD_VEND_PIDM'
    static final String FIELD_FPRREQD_VEND_REF_NUM = 'FPRREQD_VEND_REF_NUM'
    static final String FIELD_FPRREQD_PROJ_CODE = 'FPRREQD_PROJ_CODE'
    static final String FIELD_FPRREQD_POHD_CODE = 'FPRREQD_POHD_CODE'
    static final String FIELD_FPRREQD_POHD_ITEM = 'FPRREQD_POHD_ITEM'
    static final String FIELD_FPRREQD_BIDS_CODE = 'FPRREQD_BIDS_CODE'
    static final String FIELD_FPRREQD_COMPLETE_IND = 'FPRREQD_COMPLETE_IND'
    static final String FIELD_FPRREQD_SUSP_IND = 'FPRREQD_SUSP_IND'
    static final String FIELD_FPRREQD_CANCEL_IND = 'FPRREQD_CANCEL_IND'
    static final String FIELD_FPRREQD_CANCEL_DATE = 'FPRREQD_CANCEL_DATE'
    static final String FIELD_FPRREQD_CLOSED_IND = 'FPRREQD_CLOSED_IND'
    static final String FIELD_FPRREQD_POST_DATE = 'FPRREQD_POST_DATE'
    static final String FIELD_FPRREQD_TEXT_USAGE = 'FPRREQD_TEXT_USAGE'
    static final String FIELD_FPRREQD_ATYP_CODE = 'FPRREQD_ATYP_CODE'
    static final String FIELD_FPRREQD_ATYP_SEQ_NUM = 'FPRREQD_ATYP_SEQ_NUM'
    static final String FIELD_FPRREQD_RECOMM_VEND_NAME = 'FPRREQD_RECOMM_VEND_NAME'
    static final String FIELD_FPRREQD_CURR_CODE = 'FPRREQD_CURR_CODE'
    static final String FIELD_FPRREQD_CONVERTED_UNIT_PRICE = 'FPRREQD_CONVERTED_UNIT_PRICE'
    static final String FIELD_FPRREQD_DISC_AMT = 'FPRREQD_DISC_AMT'
    static final String FIELD_FPRREQD_TAX_AMT = 'FPRREQD_TAX_AMT'
    static final String FIELD_FPRREQD_ADDL_CHRG_AMT = 'FPRREQD_ADDL_CHRG_AMT'
    static final String FIELD_FPRREQD_CONVERT_DISC_AMT = 'FPRREQD_CONVERT_DISC_AMT'
    static final String FIELD_FPRREQD_CONVERT_TAX_AMT = 'FPRREQD_CONVERT_TAX_AMT'
    static final String FIELD_FPRREQD_CONVERT_ADDL_CHRG_AMT = 'FPRREQD_CONVERT_ADDL_CHRG_AMT'
    static final String FIELD_FPRREQD_TGRP_CODE = 'FPRREQD_TGRP_CODE'
    static final String FIELD_FPRREQD_AMT = 'FPRREQD_AMT'
    static final String FIELD_FPRREQD_DESC_CHGE_IND = 'FPRREQD_DESC_CHGE_IND'
    static final String FIELD_FPRREQD_VERSION = 'FPRREQD_VERSION'
    static final String FIELD_FPRREQD_DATA_ORIGIN = 'FPRREQD_DATA_ORIGIN'
    static final String DEFAULT_FPBREQD_TEXT_USAGE = 'S'

    static final String QUERY_PARAM_REQUEST_CODE = 'requestCode'
    static final String QUERY_PARAM_REQUISITION_DETAIL_ITEM = 'item'
    static final String QUERY_PARAM_USER_ID = 'userId'
    static final String NAMED_QUERY_REQUEST_DETAIL_GET_LAST_ITEM = 'RequestDetail.getLastItem'
    static final String NAMED_QUERY_REQUEST_DETAIL_BY_CODE = 'RequestDetail.fetchByrequestCode'
    static final String REQ_ACC_NAMED_QUERY_GET_SUM_PERCENTAGE = 'RequestDetail.getSumOfDistributionPercentage'

    static final String NAMED_QUERY_REQUEST_DETAIL_BY_USER = 'RequestDetail.fetchByUser'
    static final
    String NAMED_QUERY_REQUEST_DETAIL_BY_REQ_CODE_AND_ITEM = 'RequestDetail.fetchByrequestCodeAndCommodityCode'
    static final String ERROR_MESSAGE_MISSING_REQUISITION_DETAIL = 'missing.requisition.detail'
    static final
    String NAMED_QUERY_REQUEST_DETAIL_BY_REQ_CODE = 'RequestDetail.fetchByrequestCode'

    static final
    String SUCCESS_MESSAGE_CREATE_REQUISITION_DETAIL = 'net.hedtech.banner.finance.requisition.create.requisition.detail.success'
    static final
    String SUCCESS_MESSAGE_DELETE_REQUISITION_DETAIL = 'net.hedtech.banner.finance.requisition.delete.requisition.detail.success'
    static final
    String SUCCESS_MESSAGE_UPDATE_REQUISITION_DETAIL = 'net.hedtech.banner.finance.requisition.update.requisition.detail.success'
    static final String ERROR_MESSAGE_ITEM_IS_REQUIRED = 'item.is.required'

    /** Requisition Accounting **/
    static final String REQ_ACC_NAMED_QUERY_GET_LAST_SEQ = 'RequestAccounting.getLastSequenceNumberByRequestCode'
    static final String REQ_ACC_NAMED_QUERY_GET_LAST_ITEM = 'RequestAccounting.getLastItemNumberByRequestCode'
    static final String REQ_ACC_NAMED_QUERY_BY_CODE = 'RequestAccounting.fetchByRequestCodeItemAndSeq'
    static final String REQ_ACC_NAMED_QUERY_BY_USER = 'RequestAccounting.fetchByUser'
    static final String REQ_ACC_NAMED_QUERY_BY_REQUEST_CODE = 'RequestAccounting.fetchAccountingByRequestCode'
    static
    final String REQ_ACC_NAMED_QUERY_BY_REQUEST_CODE_AND_ITEM = 'RequestAccounting.fetchAccountingByRequestCodeAndItem'
    static final String QUERY_PARAM_REQUISITION_ACCOUNTING_ITEM = 'item'
    static final String QUERY_PARAM_REQUISITION_ACCOUNTING_SEQ_NUM = 'sequenceNumber'
    static final String REQ_ACC_ENTITY_FV_REQ_ACCOUNTING = 'FV_FPRREQA'
    static final String REQ_ACC_FIELD_FPRREQA_SURROGATE_ID = 'FPRREQA_SURROGATE_ID'
    static final String REQ_ACC_GEN_FPRREQA_SEQ_GEN = 'FPRREQA_SEQ_GEN'
    static final String REQ_ACC_SEQ_FPRREQA_SURROGATE_ID_SEQUENCE = 'FPRREQA_SURROGATE_ID_SEQUENCE'
    static final String REQ_ACC_FIELD_FPRREQA_REQH_CODE = 'FPRREQA_REQH_CODE'
    static final String REQ_ACC_FIELD_FPRREQA_ITEM = 'FPRREQA_ITEM'
    static final String REQ_ACC_FIELD_FPRREQA_SEQ_NUM = 'FPRREQA_SEQ_NUM'
    static final String REQ_ACC_FIELD_FPRREQA_ACTIVITY_DATE = 'FPRREQA_ACTIVITY_DATE'
    static final String REQ_ACC_FIELD_FPRREQA_USER_ID = 'FPRREQA_USER_ID'
    static final String REQ_ACC_FIELD_FPRREQA_PCT = 'FPRREQA_PCT'
    static final String REQ_ACC_FIELD_FPRREQA_AMT = 'FPRREQA_AMT'
    static final String REQ_ACC_FIELD_FPRREQA_FSYR_CODE = 'FPRREQA_FSYR_CODE'
    static final String REQ_ACC_FIELD_FPRREQA_PERIOD = 'FPRREQA_PERIOD'
    static final String REQ_ACC_FIELD_FPRREQA_RUCL_CODE = 'FPRREQA_RUCL_CODE'
    static final String REQ_ACC_FIELD_FPRREQA_COAS_CODE = 'FPRREQA_COAS_CODE'
    static final String REQ_ACC_FIELD_FPRREQA_ACCI_CODE = 'FPRREQA_ACCI_CODE'
    static final String REQ_ACC_FIELD_FPRREQA_FUND_CODE = 'FPRREQA_FUND_CODE'
    static final String REQ_ACC_FIELD_FPRREQA_ORGN_CODE = 'FPRREQA_ORGN_CODE'
    static final String REQ_ACC_FIELD_FPRREQA_ACCT_CODE = 'FPRREQA_ACCT_CODE'
    static final String REQ_ACC_FIELD_FPRREQA_PROG_CODE = 'FPRREQA_PROG_CODE'
    static final String REQ_ACC_FIELD_FPRREQA_ACTV_CODE = 'FPRREQA_ACTV_CODE'
    static final String REQ_ACC_FIELD_FPRREQA_LOCN_CODE = 'FPRREQA_LOCN_CODE'
    static final String REQ_ACC_FIELD_FPRREQA_SUSP_IND = 'FPRREQA_SUSP_IND'
    static final String REQ_ACC_FIELD_FPRREQA_NSF_SUSP_IND = 'FPRREQA_NSF_SUSP_IND'
    static final String REQ_ACC_FIELD_FPRREQA_CANCEL_IND = 'FPRREQA_CANCEL_IND'
    static final String REQ_ACC_FIELD_FPRREQA_CANCEL_DATE = 'FPRREQA_CANCEL_DATE'
    static final String REQ_ACC_FIELD_FPRREQA_PROJ_CODE = 'FPRREQA_PROJ_CODE'
    static final String REQ_ACC_FIELD_FPRREQA_APPR_IND = 'FPRREQA_APPR_IND'
    static final String REQ_ACC_FIELD_FPRREQA_NSF_OVERRIDE_IND = 'FPRREQA_NSF_OVERRIDE_IND'
    static final String REQ_ACC_FIELD_FPRREQA_ABAL_IND = 'FPRREQA_ABAL_IND'
    static final String REQ_ACC_FIELD_FPRREQA_CONVERTED_AMT = 'FPRREQA_CONVERTED_AMT'
    static final String REQ_ACC_FIELD_FPRREQA_CLOSED_IND = 'FPRREQA_CLOSED_IND'
    static final String REQ_ACC_FIELD_FPRREQA_DISC_AMT = 'FPRREQA_DISC_AMT'
    static final String REQ_ACC_FIELD_FPRREQA_TAX_AMT = 'FPRREQA_TAX_AMT'
    static final String REQ_ACC_FIELD_FPRREQA_ADDL_CHRG_AMT = 'FPRREQA_ADDL_CHRG_AMT'
    static final String REQ_ACC_FIELD_FPRREQA_CONVERT_DISC_AMT = 'FPRREQA_CONVERT_DISC_AMT'
    static final String REQ_ACC_FIELD_FPRREQA_CONVERT_TAX_AMT = 'FPRREQA_CONVERT_TAX_AMT'
    static final String REQ_ACC_FIELD_FPRREQA_CONVERT_ADDL_CHRG_AMT = 'FPRREQA_CONVERT_ADDL_CHRG_AMT'
    static final String REQ_ACC_FIELD_FPRREQA_DISC_AMT_PCT = 'FPRREQA_DISC_AMT_PCT'
    static final String REQ_ACC_FIELD_FPRREQA_ADDL_AMT_PCT = 'FPRREQA_ADDL_AMT_PCT'
    static final String REQ_ACC_FIELD_FPRREQA_TAX_AMT_PCT = 'FPRREQA_TAX_AMT_PCT'
    static final String REQ_ACC_FIELD_FPRREQA_DISC_RUCL_CODE = 'FPRREQA_DISC_RUCL_CODE'
    static final String REQ_ACC_FIELD_FPRREQA_TAX_RUCL_CODE = 'FPRREQA_TAX_RUCL_CODE'
    static final String REQ_ACC_FIELD_FPRREQA_ADDL_RUCL_CODE = 'FPRREQA_ADDL_RUCL_CODE'
    static final String REQ_ACC_FIELD_FPRREQA_RUCL_CODE_LIQ = 'FPRREQA_RUCL_CODE_LIQ'
    static final String REQ_ACC_FIELD_FPRREQA_VERSION = 'FPRREQA_VERSION'
    static final String REQ_ACC_FIELD_FPRREQA_DATA_ORIGIN = 'FPRREQA_DATA_ORIGIN'
    static final String ERROR_MESSAGE_MISSING_REQUISITION_ACCOUNTING = 'missing.requisition.accounting'

    static final boolean TRUE = true
    static final boolean FALSE = false

    static final String ERROR_MESSAGE_ITEM_SEQUENCE_REQUIRED = 'item.sequence.required'

    static final
    String SUCCESS_MESSAGE_CREATE_REQUISITION_ACCOUNTING = 'net.hedtech.banner.finance.requisition.create.requisition.accounting.success'
    static final
    String SUCCESS_MESSAGE_DELETE_REQUISITION_ACCOUNTING = 'net.hedtech.banner.finance.requisition.delete.requisition.accounting.success'
    static final
    String SUCCESS_MESSAGE_UPDATE_REQUISITION_ACCOUNTING = 'net.hedtech.banner.finance.requisition.update.requisition.accounting.success'

    /** Constants for Requisition Information */
    static final String VIEW_FVQ_REQ_DASHBOARD_INFO = 'FVQ_REQ_DASHBOARD_INFO'
    static final String ERROR_MESSAGE_INVALID_BUCKET_TYPE = 'invalid.bucket.type'

    static final String REQUISITION_INFO_FIELD_SURROGATE_ID = 'SURROGATE_ID'
    static final String REQUISITION_INFO_FIELD_AMOUNT = 'AMOUNT'
    static final String REQUISITION_INFO_FIELD_REQUESTOR_NAME = 'REQUESTOR_NAME'
    static final String REQUISITION_INFO_FIELD_VENDOR_NAME = 'VENDOR_NAME'
    static final String REQUISITION_INFO_FIELD_FPBREQH_TRANS_DATE = 'TRANS_DATE'
    static final String REQUISITION_INFO_FIELD_REQUEST_DATE = 'REQUEST_DATE'
    static final String REQUISITION_INFO_FIELD_DELIVERY_DATE = 'DELIVERY_DATE'
    static final String REQUISITION_INFO_FIELD_FPBREQH_USER_ID = 'USER_ID'
    static final String REQUISITION_INFO_FIELD_FPBREQH_ORIGIN_CODE = 'ORIGIN_CODE'
    static final String REQUISITION_INFO_FIELD_FPBREQH_CURR_CODE = 'CURR_CODE'
    static final String REQUISITION_INFO_FIELD_VERSION = 'VERSION'
    static final String REQUISITION_INFO_FIELD_FPBREQH_ORGN_CODE = 'ORGN_CODE'
    static final String REQUISITION_INFO_FIELD_ORG_TITLE = 'ORG_TITLE'
    static final String REQUISITION_INFO_FIELD_FPBREQH_COAS_CODE = 'COAS_CODE'
    static final String REQUISITION_INFO_FIELD_FPBREQH_VEND_PIDM = 'VEND_PIDM'
    static final String REQUISITION_INFO_FIELD_FPBREQH_ACTIVITY_DATE = 'ACTIVITY_DATE'
    static final String REQUISITION_INFO_FIELD_FPBREQH_FPBREQH_CODE = 'CODE'
    static final String REQUISITION_INFO_FIELD_STATUS = 'STATUS'
    static final String REQUISITION_INFO_FINDER_BY_STATUS = 'RequisitionInformation.listRequisitionByStatus'
    static final String REQUISITION_INFO_COUNT_FINDER_BY_STATUS = 'RequisitionInformation.countRequisitionsByStatus'
    static final String REQUISITION_INFO_FINDER_BY_CODE_USER = 'RequisitionInformation.countRequisitionsByCodeAndUser'
    static final String REQUISITION_INFO_FINDER_PARAM_STATUS = 'status'
    static final String REQUISITION_INFO_FINDER_PARAM_REQ_CODE = 'requisitionCode'
    static final String REQUISITION_INFO_FINDER_PARAM_STATUS_PARAM_USER_ID = 'userId'
    static final String REQUEST_PARAM_BUCKET_TYPES = 'bucketTypes'
    static
    final String REQUISITION_INFO_FINDER_BY_SEARCH_PARAM = 'RequisitionInformation.searchRequisitionsBySearchParam'
    static
    final String REQUISITION_INFO_COUNT_FINDER_BY_SEARCH_PARAM = 'RequisitionInformation.countRequisitionsBySearchParam'
    static final String REQUISITION_INFO_SEARCH_PARAM = 'searchParam'
    static
    final String REQUISITION_INFO_SEARCH_BY_TRANSACTION_DATE = 'RequisitionInformation.listRequisitionsByTransactionDate'
    static
    final String REQUISITION_INFO_SEARCH_COUNT_FINDER_BY_TRANSACTION_DATE = 'RequisitionInformation.countRequisitionsByTransactionDate'
    static final String REQUISITION_INFO_USER_NAME = 'GRAILS'
    /** Properties keys for Requisition Information status  **/
    static final String REQUISITION_INFO_STATUS_DRAFT = 'draft'
    static final String REQUISITION_INFO_STATUS_DISAPPROVED = 'disapproved'
    static final String REQUISITION_INFO_STATUS_PENDING = 'pending'
    static final String REQUISITION_INFO_STATUS_COMPLETED = 'completed'
    static final String REQUISITION_INFO_STATUS_ASSIGNED_TO_BUYER = 'assignedToBuyer'
    static final String REQUISITION_INFO_STATUS_CONVERTED_TO_PO = 'convertedToPo'
    static
    final String REQUISITION_INFO_FINDER_BY_SEARCH_PARAM_AND_STATUS = 'RequisitionInformation.searchRequisitionsByStatusAndSearchParam'
    static
    final String REQUISITION_INFO_COUNT_FINDER_BY_SEARCH_PARAM_AND_STATUS = 'RequisitionInformation.countRequisitionsByStatusAndSearchParam'
    static
    final String REQUISITION_INFO_SEARCH_BY_TRANSACTION_DATE_AND_STATUS = 'RequisitionInformation.listRequisitionsByTransactionDateAndStatus'
    static
    final String REQUISITION_INFO_SEARCH_COUNT_FINDER_BY_TRANSACTION_DATE_AND_STATUS = 'RequisitionInformation.countRequisitionsByTransactionDateAndStatus'
    static
    final String EMPTY_SEARCH_REQUISITION_INFO_MESSAGE = 'net.hedtech.banner.finance.requisition.search.requisition.info.failure'

    /** Constants for listing and search Requisitions */
    static final String REQUISITION_LIST_BUCKET_DRAFT = 'draft'
    static final String REQUISITION_LIST_BUCKET_PENDING = 'pending'
    static final String REQUISITION_LIST_BUCKET_COMPLETE = 'completed'
    static final String REQUISITION_LIST_BUCKET_ALL = 'all'
    static final String REQUEST_PARAM_ACCOUNTING_ITEM = 'item'
    static final String REQUEST_PARAM_ACCOUNTING_SEQUENCE = 'sequence'

    /** Constants for Requisition Summary */
    static final String REQUISITION_SUMMARY_VIEW = 'FVQ_REQ_SUMMARY'
    static final String REQUISITION_SUMMARY_FIELD_FPBREQH_VERSION = 'FPBREQH_VERSION'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_ITEM = 'FPRREQA_ITEM'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_SEQ_NUM = 'FPRREQA_SEQ_NUM'
    static final String REQUISITION_SUMMARY_FIELD_FPBREQH_SINGLE_ACCTG_IND = 'FPBREQH_SINGLE_ACCTG_IND'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_PCT = 'FPRREQA_PCT'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_AMT = 'FPRREQA_AMT'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_COAS_CODE = 'FPRREQA_COAS_CODE'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_ACCI_CODE = 'FPRREQA_ACCI_CODE'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_FUND_CODE = 'FPRREQA_FUND_CODE'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_ORGN_CODE = 'FPRREQA_ORGN_CODE'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_ACCT_CODE = 'FPRREQA_ACCT_CODE'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_ACTV_CODE = 'FPRREQA_ACTV_CODE'
    static final String REQUISITION_SUMMARY_FIELD_SHIP_TO_CODE = 'FPBREQH_SHIP_CODE'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_PROJ_CODE = 'FPRREQA_PROJ_CODE'
    static final String REQUISITION_SUMMARY_FIELD_FPBREQH_ATTENTION_TO = 'FPBREQH_ATTENTION_TO'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_PROG_CODE = 'FPRREQA_PROG_CODE'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_DISC_AMT = 'FPRREQA_DISC_AMT'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_ADDL_CHRG_AMT = 'FPRREQA_ADDL_CHRG_AMT'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_TAX_AMT = 'FPRREQA_TAX_AMT'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQD_COMM_CODE = 'FPRREQD_COMM_CODE'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQD_COMM_DESC = 'FPRREQD_COMM_DESC'
    static final String REQUISITION_SUMMARY_FIELD_FTVCOMM_DESC = 'FTVCOMM_DESC'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQD_QTY = 'FPRREQD_QTY'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQD_ITEM = 'FPRREQD_ITEM'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQD_DISC_AMT = 'FPRREQD_DISC_AMT'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQD_ADDL_CHRG_AMT = 'FPRREQD_ADDL_CHRG_AMT'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQD_TAX_AMT = 'FPRREQD_TAX_AMT'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQD_UNIT_PRICE = 'FPRREQD_UNIT_PRICE'
    static final String REQUISITION_SUMMARY_FIELD_FPBREQH_CODE = 'FPBREQH_CODE'
    static final String REQUISITION_SUMMARY_FIELD_FPBREQH_VEND_PIDM = 'FPBREQH_VEND_PIDM'
    static final String REQUISITION_SUMMARY_FIELD_FPBREQH_ATYP_SEQ_NUM = 'FPBREQH_ATYP_SEQ_NUM'
    static final String REQUISITION_SUMMARY_FIELD_FPBREQH_ATYP_CODE = 'FPBREQH_ATYP_CODE'
    static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRIDEN_ID = 'FPVVEND_SPRIDEN_ID'
    static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRIDEN_LAST_NAME = 'FPVVEND_SPRIDEN_LAST_NAME'
    static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_STREET_LINE1 = 'FPVVEND_SPRADDR_STREET_LINE1'
    static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_ZIP = 'FPVVEND_SPRADDR_ZIP'
    static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_STAT_CODE = 'FPVVEND_SPRADDR_STAT_CODE'
    static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_CITY = 'FPVVEND_SPRADDR_CITY'
    static final String REQUISITION_SUMMARY_FIELD_FPBREQH_SURROGATE_ID = 'FPBREQH_SURROGATE_ID'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQD_SURROGATE_ID = 'FPRREQD_SURROGATE_ID'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_SURROGATE_ID = 'FPRREQA_SURROGATE_ID'
    static final String REQUISITION_SUMMARY_FIELD_FPVVEND_PHONE_NUMBER = 'FPVVEND_PHONE_NUMBER'
    static final String REQUISITION_SUMMARY_FIELD_FPVVEND_PHONE_EXT = 'FPVVEND_PHONE_EXT'

    static
    final String REQUISITION_SUMMARY_FINDER_BY_REQUEST_CODE = 'RequisitionSummary.FetchRequisitionSummaryForRequestCode'

    /** Copy requisition **/
    static
    final String ERROR_MESSAGE_COMPLETED_REQUISITION_IS_REQUIRED = 'missing.requisition.complemented.requisition.required'
    static final String COPY_REQUISITION_HEADER_COPIED_FROM = 'COPIED FROM '
    static
    final String SUCCESS_MESSAGE_COPY_REQUISITION_HEADER = 'net.hedtech.banner.finance.requisition.copy.requisition.header.success'

    /** Date format error **/
    static final String ERROR_MESSAGE_INVALID_DATE_FORMAT = 'default.invalid.date.format'

    /** Recall requisition **/
    static
    final String ERROR_MESSAGE_RECALL_REQUISITION_PENDING_REQ_IS_REQUIRED = 'missing.requisition.recall.requisition.pendingReq.required'
    static
    final String SUCCESS_MESSAGE_RECALL_REQUISITION = 'net.hedtech.banner.finance.requisition.recall.requisition.success'

    /** Constant variable for Domain class for Unapproved Documents Table FOBUAPP **/
    static final String FOBUAPP = 'FOBUAPP'
    static final String FINANCE_UNAPPROVED_DOCUMENT_SEQ_GEN = 'FOBUAPP_SEQ_GEN'
    static final String FINANCE_UNAPPROVED_DOCUMENT_SURROGATE_ID_SEQUENCE = 'FOBUAPP_SURROGATE_ID_SEQUENCE'
    static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_SURROGATE_ID = 'FOBUAPP_SURROGATE_ID'
    static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_DOC_CODE = 'FOBUAPP_DOC_CODE'
    static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_ACTIVITY_DATE = 'FOBUAPP_ACTIVITY_DATE'
    static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_CHG_SEQ_NUM = 'FOBUAPP_CHG_SEQ_NUM'
    static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_DATA_ORIGIN = 'FOBUAPP_DATA_ORIGIN'
    static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_DOC_AMT = 'FOBUAPP_DOC_AMT'
    static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_ORIG_USER = 'FOBUAPP_ORIG_USER'
    static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_STATUS_IND = 'FOBUAPP_STATUS_IND'
    static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_SUBMISSION_NUMBER = 'FOBUAPP_SUBMISSION_NUMBER'
    static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_USER_CODE = 'FOBUAPP_USER_CODE'
    static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_USER_ID = 'FOBUAPP_USER_ID'
    static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_VERSION = 'FOBUAPP_VERSION'
    static final String FINANCE_UNAPPROVED_DOCUMENT_FOBUAPP_VPDI_CODE = 'FOBUAPP_VPDI_CODE'
    static
    final String FINANCE_UNAPPROVED_DOCUMENT_QUERY_NAME_FIND_BY_DOCUMENT_CODE = 'FinanceUnapprovedDocument.findByDocumentCode'
    static final String FINANCE_QUERY_PARAM_DOCUMENT_CODE = 'documentCode'

    /** Constant variable for Domain class for Unapproved Documents Table FOBAPPH **/
    static final String FOBAPPH = 'FOBAPPH'
    static final String FINANCE_APPROVAL_HISTORY_SEQ_GEN = 'FOBAPPH_SEQ_GEN'
    static final String FINANCE_APPROVAL_HISTORY_SURROGATE_ID_SEQUENCE = 'FOBAPPH_SURROGATE_ID_SEQUENCE'
    static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_SURROGATE_ID = 'FOBAPPH_SURROGATE_ID'
    static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_ACTIVITY_DATE = 'FOBAPPH_ACTIVITY_DATE'
    static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_CHG_SEQ_NUM = 'FOBAPPH_CHG_SEQ_NUM'
    static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_DATA_ORIGIN = 'FOBAPPH_DATA_ORIGIN'
    static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_DOC_CODE = 'FOBAPPH_DOC_CODE'
    static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_QUEUE_ID = 'FOBAPPH_QUEUE_ID'
    static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_QUEUE_LEVEL = 'FOBAPPH_QUEUE_LEVEL'
    static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_SEQ_NUM = 'FOBAPPH_SEQ_NUM'
    static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_SUBMISSION_NUMBER = 'FOBAPPH_SUBMISSION_NUMBER'
    static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_USER_ID = 'FOBAPPH_USER_ID'
    static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_VERSION = 'FOBAPPH_VERSION'
    static final String FINANCE_APPROVAL_HISTORY_FOBAPPH_VPDI_CODE = 'FOBAPPH_VPDI_CODE'
    static
    final String FINANCE_APPROVAL_HISTORY_QUERY_NAME_FIND_BY_DOCUMENT_CODE = 'FinanceApprovalHistory.findByDocumentCode'
    static final String FINANCE_APPROVAL_HISTORY_QUERY_ID_DENY = 'DENY'
    static final int FINANCE_APPROVAL_HISTORY_QUERY_LEVEL_ZERO = 0

    /** Constant variable for Domain class for Unapproved Documents Table FOBAINP **/
    static final String FOBAINP = 'FOBAINP'
    static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_SURROGATE_ID = 'FOBAINP_SURROGATE_ID'
    static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_ACTIVITY_DATE = 'FOBAINP_ACTIVITY_DATE'
    static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_APPROVAL_SEQUENCE = 'FOBAINP_APPROVAL_SEQUENCE'
    static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_CHG_SEQ_NUM = 'FOBAINP_CHG_SEQ_NUM'
    static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_DATA_ORIGIN = 'FOBAINP_DATA_ORIGIN'
    static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_DOC_NUM = 'FOBAINP_DOC_NUM'
    static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_DOC_TYPE = 'FOBAINP_DOC_TYPE'
    static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_LEVEL = 'FOBAINP_LEVEL'
    static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_QUEUE_ID = 'FOBAINP_QUEUE_ID'
    static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_SUBMISSION_NUM = 'FOBAINP_SUBMISSION_NUM'
    static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_USER_ID = 'FOBAINP_USER_ID'
    static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_VERSION = 'FOBAINP_VERSION'
    static final String FINANCE_APPROVAL_IN_PROCESS_FOBAINP_VPDI_CODE = 'FOBAINP_VPDI_CODE'
    static
    final String FINANCE_APPROVAL_IN_PROCESS_QUERY_NAME_FIND_BY_DOCUMENT_NUMBER = 'FinanceApprovalsInProcess.findByDocumentNumber'
    static final String FINANCE_APPROVAL_IN_PROCESS_QUERY_PARAM_DOCUMENT_NUMBER = 'documentNumber'

    /** FOBTEXT - FinanceText Domain constants **/
    static final String FINANCE_TEXT_TABLE = 'FOBTEXT'
    static final String FINANCE_TEXT_SEQ_GEN = 'FOBTEXT_SEQ_GEN'
    static final String FINANCE_TEXT_SURROGATE_ID_SEQUENCE = 'FOBTEXT_SURROGATE_ID_SEQUENCE'

    static final String FINANCE_TEXT_FIELD_FOBTEXT_SURROGATE_ID = 'FOBTEXT_SURROGATE_ID'
    static final String FINANCE_TEXT_FIELD_FOBTEXT_ACTIVITY_DATE = 'FOBTEXT_ACTIVITY_DATE'
    static final String FINANCE_TEXT_FIELD_FOBTEXT_CHG_SEQ_NUM = 'FOBTEXT_CHG_SEQ_NUM'
    static final String FINANCE_TEXT_FIELD_FOBTEXT_CLA_NUM = 'FOBTEXT_CLA_NUM'
    static final String FINANCE_TEXT_FIELD_FOBTEXT_CODE = 'FOBTEXT_CODE'
    static final String FINANCE_TEXT_FIELD_FOBTEXT_DATA_ORIGIN = 'FOBTEXT_DATA_ORIGIN'
    static final String FINANCE_TEXT_FIELD_FOBTEXT_DTYP_SEQ_NUM = 'FOBTEXT_DTYP_SEQ_NUM'
    static final String FINANCE_TEXT_FIELD_FOBTEXT_ITEM = 'FOBTEXT_ITEM'
    static final String FINANCE_TEXT_FIELD_FOBTEXT_PIDM = 'FOBTEXT_PIDM'
    static final String FINANCE_TEXT_FIELD_FOBTEXT_PRT_IND = 'FOBTEXT_PRT_IND'
    static final String FINANCE_TEXT_FIELD_FOBTEXT_SEQ_NUM = 'FOBTEXT_SEQ_NUM'
    static final String FINANCE_TEXT_FIELD_FOBTEXT_TEXT = 'FOBTEXT_TEXT'
    static final String FINANCE_TEXT_FIELD_FOBTEXT_USER_ID = 'FOBTEXT_USER_ID'
    static final String FINANCE_TEXT_FIELD_FOBTEXT_VERSION = 'FOBTEXT_VERSION'
    static final String FINANCE_TEXT_FIELD_FOBTEXT_VPDI_CODE = 'FOBTEXT_VPDI_CODE'

    static
    final String FINANCE_TEXT_NAMED_QUERY_GET_FINANCE_TEXT_BY_CODE_AND_SEQUENCE_NO = 'FinanceText.getFinanceTextByCodeAndItemNumber'
    static final String FINANCE_TEXT_QUERY_PARAM_TEXT_CODE = 'textCode'
    static final String FINANCE_TEXT_QUERY_PARAM_TEXT_ITEM = 'textItem'
    static final String FINANCE_TEXT_QUERY_PARAM_PRINT_INDICATOR = 'printOptionIndicator'
    static final String FINANCE_TEXT_NAMED_QUERY_LIST_ALL_FINANCE_TEXT_BY_CODE = 'FinanceText.listAllFinanceTextByCode'
    static
    final String FINANCE_TEXT_NAMED_QUERY_GET_FINANCE_TEXT_BY_CODE_AND_SEQUENCE_NO_AND_PRINT_IND = 'FinanceText.getFinanceTextByCodeAndItemNumberAndPrintInd'
    static
    final String FINANCE_TEXT_NAMED_QUERY_LIST_HEADER_LEVEL_TEXT_BY_CODE_AND_PRINT_OPTION_IND = 'FinanceText.listHeaderLevelTextByCodeAndPrintOptionInd'
    static
    final String FINANCE_TEXT_NAMED_QUERY_LIST_HEADER_LEVEL_TEXT_BY_CODE = 'FinanceText.listHeaderLevelTextByCode'

    static final int FINANCE_TEXT_TEXT_LENGTH = 50
    static final int FINANCE_TEXT_SEQUENCE_NUMBER_INCREMENT = 10
    static final int FINANCE_TEXT_DOCUMENT_TYPE_SEQ_NUMBER_REQUISITION = 1

    /** Constants for Domain FinanceBuyerVerification **/
    static final String FTVBUYR_TABLE = 'FVQ_REQ_BUYER_VERIFICATION'
    static
    final String FINANCE_BUYER_VERIFICATION_NAMED_QUERY_FIND_BY_REQUEST_CODE = 'FinanceBuyerVerification.findByDocumentCode'
    static final String FINANCE_BUYER_VERIFICATION_FTVBUYR_REQUEST_CODE = 'REQUEST_CODE'
    static final String FINANCE_BUYER_VERIFICATION_FTVBUYR_CODE = 'BUYER_CODE'
    static final String FINANCE_BUYER_VERIFICATION_FTVBUYR_NAME = 'BUYER_NAME'
    static final String FINANCE_BUYER_VERIFICATION_FTVBUYR_VERSION = 'VERSION'
    static final String FINANCE_BUYER_VERIFICATION_BUYER = 'buyer'
    static final String FINANCE_BUYER_VERIFICATION_QUERY_PARAM_REQUEST_CODE = 'requestCode'

    /** Constants for Domain FinanceRequestPOVerification **/
    static final String FTVRQPO_TABLE = 'FTVRQPO'
    static
    final String FINANCE_REQUEST_PO_VERIFICATION_NAMED_QUERY_FIND_BY_REQ_CODE = 'FinanceRequestPOVerification.findByRequestCode'
    static final String FINANCE_REQUEST_PO_VERIFICATION_FTVRQPO_SURROGATE_ID = 'FTVRQPO_SURROGATE_ID'
    static final String FINANCE_REQUEST_PO_VERIFICATION_FTVRQPO_REQD_CODE = 'FTVRQPO_REQD_CODE'
    static final String FINANCE_REQUEST_PO_VERIFICATION_FTVRQPO_POHD_CODE = 'FTVRQPO_POHD_CODE'
    static final String FINANCE_REQUEST_PO_VERIFICATION_FTVRQPO_VERSION = 'FTVRQPO_VERSION'
    static final String FINANCE_REQUEST_PO_VERIFICATION_QUERY_PARAM_REQUEST_CODE = 'requestCode'
    static final String FINANCE_PROCUREMENT_PDF_CONTENT_TYPE = 'application/pdf'
    static final String FINANCE_PROCUREMENT_PDF_CONTENT = 'Content-disposition'
    static final String FINANCE_PROCUREMENT_PDF_INLINE = 'inline;filename='
    static final String FINANCE_PROCUREMENT_PDF_HEADER_EXPIRES = 'Expires'
    static final String FINANCE_PROCUREMENT_PDF_HEADER_PRAGMA = 'Pragma'
    static final String FINANCE_PROCUREMENT_PDF_HEADER_PRAGMA_PUBLIC = 'public'
    static
    final String FINANCE_PROCUREMENT_PDF_HEADER_CACHE_CONTROL = 'Cache-Control'
    static final String FINANCE_PROCUREMENT_PDF_HEADER_CACHE_CONTROL_MUST_RE_VALIDATE = 'must-revalidate, post-check=0, pre-check=0'

    /** Recall requisition **/
    static final String ERROR_MESSAGE_DELETE_REQUISITION_DRAFT_OR_DISAPPROVED_REQ_IS_REQUIRED = 'missing.requisition.delete.requisition.draftOrDisapproved.required'

}
