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

    /* Constant for Requisition Header Domain*/

    public static final String REQUISITION_HEADER_FINDER_BY_REQUEST_CODE = 'RequisitionHeader.findByRequestCodeAndItem'
    public static final String REQUISITION_HEADER_FINDER_BY_USER = 'RequisitionHeader.fetchByUser'

    public static final String REQUISITION_HEADER_FINDER_BY_REQUEST_CODE_PARAM_REQUEST_CODE = 'requestCode'
    public static final String REQUISITION_HEADER_FINDER_BY_REQUEST_CODE_PARAM_USER_ID = 'userId'
    public static final String REQUISITION_HEADER_VIEW = 'FPVREQH'
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

    /** Constant variable for Purchase Requisition Detail Domain Property column name **/
    static final String FPVREQD = 'FPVREQD'
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
    static final String QUERY_PARAM_REQUISITION_ACCOUNTING_ITEM = 'item'
    static final String QUERY_PARAM_REQUISITION_ACCOUNTING_SEQ_NUM = 'sequenceNumber'
    static final String REQ_ACC_ENTITY_FPVREQA = 'FPVREQA'
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
    static final String VIEW_FPVREQINFO = 'FPVREQINFO'
    static final String ERROR_MESSAGE_INVALID_BUCKET_TYPE = 'invalid.bucket.type'

    static final String REQUISITION_INFO_FIELD_FPVREQINFO_SURROGATE_ID = 'FPVREQINFO_SURROGATE_ID'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_AMOUNT = 'FPVREQINFO_AMOUNT'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_REQUESTOR_NAME = 'FPVREQINFO_REQUESTOR_NAME'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_VENDOR_NAME = 'FPVREQINFO_VENDOR_NAME'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_TRANS_DATE = 'FPVREQINFO_TRANS_DATE'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_REQUEST_DATE = 'FPVREQINFO_REQUEST_DATE'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_DELIVERY_DATE = 'FPVREQINFO_DELIVERY_DATE'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_USER_ID = 'FPVREQINFO_USER_ID'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_ORIGIN_CODE = 'FPVREQINFO_ORIGIN_CODE'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_CURR_CODE = 'FPVREQINFO_CURR_CODE'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_VERSION = 'FPVREQINFO_VERSION'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_ORGN_CODE = 'FPVREQINFO_ORGN_CODE'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_ORG_TITLE = 'FPVREQINFO_ORG_TITLE'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_COAS_CODE = 'FPVREQINFO_COAS_CODE'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_VEND_PIDM = 'FPVREQINFO_VEND_PIDM'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_ACTIVITY_DATE = 'FPVREQINFO_ACTIVITY_DATE'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_FPBREQH_FPBREQH_CODE = 'FPVREQINFO_CODE'
    static final String REQUISITION_INFO_FIELD_FPVREQINFO_STATUS = 'FPVREQINFO_STATUS'
    static final String REQUISITION_INFO_FINDER_BY_STATUS = 'RequisitionInformation.listRequisitionByStatus'
    static final String REQUISITION_INFO_COUNT_FINDER_BY_STATUS = 'RequisitionInformation.countRequisitionsByStatus'
    static final String REQUISITION_INFO_FINDER_PARAM_STATUS = 'status'
    static final String REQUISITION_INFO_FINDER_PARAM_STATUS_PARAM_USER_ID = 'userId'
    static final String REQUEST_PARAM_BUCKET_TYPES = 'bucketTypes'

    /** Properties keys for Requisition Information status  **/
    static final String REQUISITION_INFO_STATUS_DRAFT = 'draft'
    static final String REQUISITION_INFO_STATUS_DISAPPROVED = 'disapproved'
    static final String REQUISITION_INFO_STATUS_PENDING = 'pending'
    static final String REQUISITION_INFO_STATUS_COMPLETED = 'completed'
    static final String REQUISITION_INFO_STATUS_ASSIGNED_TO_BUYER = 'assignedToBuyer'
    static final String REQUISITION_INFO_STATUS_CONVERTED_TO_PO = 'convertedToPo'

    /** Constants for listing and search Requisitions */
    static final String REQUISITION_LIST_BUCKET_DRAFT = 'draft'
    static final String REQUISITION_LIST_BUCKET_PENDING = 'pending'
    static final String REQUISITION_LIST_BUCKET_COMPLETE = 'completed'
    static final String REQUISITION_LIST_BUCKET_ALL = 'all'
    static final String REQUEST_PARAM_ACCOUNTING_ITEM = 'item'
    static final String REQUEST_PARAM_ACCOUNTING_SEQUENCE = 'sequence'

    /** Constants for Requisition Summary */
    static final String REQUISITION_SUMMARY_VIEW = 'FPV_REQ_SUMMARY'
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
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_PROJ_CODE = 'FPRREQA_PROJ_CODE'
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
    static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRIDEN_LAST_NAME = 'FPVVEND_SPRIDEN_LAST_NAME'
    static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_STREET_LINE1 = 'FPVVEND_SPRADDR_STREET_LINE1'
    static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_ZIP = 'FPVVEND_SPRADDR_ZIP'
    static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_STAT_CODE = 'FPVVEND_SPRADDR_STAT_CODE'
    static final String REQUISITION_SUMMARY_FIELD_FPVVEND_SPRADDR_CITY = 'FPVVEND_SPRADDR_CITY'
    static final String REQUISITION_SUMMARY_FIELD_FPBREQH_SURROGATE_ID = 'FPBREQH_SURROGATE_ID'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQD_SURROGATE_ID = 'FPRREQD_SURROGATE_ID'
    static final String REQUISITION_SUMMARY_FIELD_FPRREQA_SURROGATE_ID = 'FPRREQA_SURROGATE_ID'
    static final String REQUISITION_SUMMARY_FINDER_BY_REQUEST_CODE = 'RequisitionSummary.FetchRequisitionSummaryForRequestCode'
}
