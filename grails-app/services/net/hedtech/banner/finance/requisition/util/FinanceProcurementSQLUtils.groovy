/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.finance.requisition.util

/**
 * This util file will have the final methods where the methods will return the required SQL Queries.
 */
final class FinanceProcurementSQLUtils {
    /**
     * This method is used update the next requisition number.
     * @return
     */
    public static final def getUpdateReqNextQuery() {
        StringBuffer query = new StringBuffer()
        query.append("UPDATE FOBSEQN SET FOBSEQN_MAXSEQNO_7 = FOBSEQN_MAXSEQNO_7 + 1 WHERE FOBSEQN_SEQNO_TYPE = 'R'")
        query.toString()
    }

    /**
     * This method is used to get generated request code.
     * @return
     */
    public static final def getSelectGeneratedReqCodeQuery() {
        StringBuffer query = new StringBuffer()
        query.append("SELECT FOBSEQN_SEQNO_PREFIX||LPAD(TO_CHAR(FOBSEQN_MAXSEQNO_7),7,'0') AS NEXT_REQ_NUMBER FROM FOBSEQN WHERE FOBSEQN_SEQNO_TYPE = 'R'")
        query.toString()
    }

    /**
     * This method is used to copy requisition.
     * @return
     */
    public static final def getCopyRequisitionQuery() {
        StringBuffer query = new StringBuffer();
        query.append("BEGIN");
        query.append("	INSERT INTO FPBREQH (");
        query.append("		FPBREQH_CODE,FPBREQH_ACTIVITY_DATE,FPBREQH_USER_ID,FPBREQH_REQH_DATE,");
        query.append("		FPBREQH_TRANS_DATE,FPBREQH_NAME,FPBREQH_PHONE_AREA,");
        query.append("		FPBREQH_PHONE_NUM,FPBREQH_PHONE_EXT,FPBREQH_VEND_PIDM,");
        query.append("		FPBREQH_ATYP_CODE, FPBREQH_ATYP_SEQ_NUM,FPBREQH_COAS_CODE,");
        query.append("		FPBREQH_ORGN_CODE,FPBREQH_REQD_DATE,FPBREQH_COMPLETE_IND,");
        query.append("		FPBREQH_PRINT_IND,FPBREQH_ENCUMB_IND,FPBREQH_SUSP_IND,");
        query.append("		FPBREQH_CANCEL_IND,FPBREQH_CANCEL_DATE,FPBREQH_POST_DATE,");
        query.append("		FPBREQH_APPR_IND,FPBREQH_TEXT_IND,FPBREQH_EDIT_DEFER_IND,");
        query.append("		FPBREQH_RECOMM_VEND_NAME,FPBREQH_CURR_CODE,FPBREQH_NSF_ON_OFF_IND,");
        query.append("		FPBREQH_SINGLE_ACCTG_IND,FPBREQH_CLOSED_IND,FPBREQH_SHIP_CODE,");
        query.append("		FPBREQH_RQST_TYPE_IND,FPBREQH_INVENTORY_REQ_IND,FPBREQH_DELIVERY_COMMENT,FPBREQH_EMAIL_ADDR,FPBREQH_FAX_AREA,");
        query.append("		FPBREQH_FAX_NUMBER,FPBREQH_FAX_EXT,FPBREQH_ATTENTION_TO,FPBREQH_VENDOR_CONTACT,FPBREQH_DISC_CODE,FPBREQH_VEND_EMAIL_ADDR,");
        query.append("		FPBREQH_COPIED_FROM, FPBREQH_TGRP_CODE,");
        query.append("		FPBREQH_CTRY_CODE_PHONE,FPBREQH_CTRY_CODE_FAX)");
        query.append("	SELECT  (:nextDocCode),SYSDATE,USER,SYSDATE,SYSDATE,FPBREQH_NAME,");
        query.append("		FPBREQH_PHONE_AREA,FPBREQH_PHONE_NUM,FPBREQH_PHONE_EXT,");
        query.append("		FPBREQH_VEND_PIDM,FPBREQH_ATYP_CODE,FPBREQH_ATYP_SEQ_NUM,");
        query.append("		FPBREQH_COAS_CODE,FPBREQH_ORGN_CODE,NULL,'N',NULL,NULL,'Y',NULL,NULL,NULL,");
        query.append("		'N',FPBREQH_TEXT_IND,'N',FPBREQH_RECOMM_VEND_NAME,");
        query.append("		FPBREQH_CURR_CODE,'Y',FPBREQH_SINGLE_ACCTG_IND,NULL,");
        query.append("		FPBREQH_SHIP_CODE,FPBREQH_RQST_TYPE_IND, FPBREQH_INVENTORY_REQ_IND,FPBREQH_DELIVERY_COMMENT,FPBREQH_EMAIL_ADDR,FPBREQH_FAX_AREA,");
        query.append("		FPBREQH_FAX_NUMBER,FPBREQH_FAX_EXT,FPBREQH_ATTENTION_TO,FPBREQH_VENDOR_CONTACT,FPBREQH_DISC_CODE,FPBREQH_VEND_EMAIL_ADDR,");
        query.append("		(:oldDocCode), FPBREQH_TGRP_CODE,");
        query.append("		FPBREQH_CTRY_CODE_PHONE,FPBREQH_CTRY_CODE_FAX");
        query.append("	FROM FPBREQH WHERE FPBREQH_CODE=(:oldDocCode);");
        query.append("	INSERT INTO FPRREQD (");
        query.append("		FPRREQD_REQH_CODE, FPRREQD_ITEM, FPRREQD_ACTIVITY_DATE,");
        query.append("		FPRREQD_USER_ID, FPRREQD_COMM_CODE, FPRREQD_COMM_DESC,");
        query.append("		FPRREQD_COAS_CODE, FPRREQD_ORGN_CODE, FPRREQD_BUYR_CODE,");
        query.append("		FPRREQD_QTY, FPRREQD_UOMS_CODE, FPRREQD_UNIT_PRICE, FPRREQD_AGRE_CODE,");
        query.append("		FPRREQD_REQD_DATE, FPRREQD_SHIP_CODE, FPRREQD_VEND_PIDM,");
        query.append("		FPRREQD_VEND_REF_NUM, FPRREQD_PROJ_CODE, FPRREQD_POHD_CODE,");
        query.append("		FPRREQD_POHD_ITEM, FPRREQD_BIDS_CODE, FPRREQD_COMPLETE_IND,");
        query.append("		FPRREQD_SUSP_IND, FPRREQD_CANCEL_IND, FPRREQD_CANCEL_DATE ,");
        query.append("		FPRREQD_CLOSED_IND, FPRREQD_POST_DATE, FPRREQD_TEXT_USAGE,");
        query.append("		FPRREQD_ATYP_CODE, FPRREQD_ATYP_SEQ_NUM, FPRREQD_RECOMM_VEND_NAME,");
        query.append("		FPRREQD_CURR_CODE, FPRREQD_CONVERTED_UNIT_PRICE,FPRREQD_DISC_AMT,FPRREQD_TAX_AMT,FPRREQD_ADDL_CHRG_AMT,FPRREQD_CONVERT_DISC_AMT,");
        query.append("		FPRREQD_CONVERT_TAX_AMT,FPRREQD_CONVERT_ADDL_CHRG_AMT,FPRREQD_TGRP_CODE,FPRREQD_AMT,FPRREQD_DESC_CHGE_IND)");
        query.append("	SELECT (:nextDocCode),FPRREQD_ITEM,SYSDATE,USER,FPRREQD_COMM_CODE,FPRREQD_COMM_DESC,FPRREQD_COAS_CODE,FPRREQD_ORGN_CODE,NULL,");
        query.append("		FPRREQD_QTY,FPRREQD_UOMS_CODE,FPRREQD_UNIT_PRICE,FPRREQD_AGRE_CODE,");
        query.append("		FPRREQD_REQD_DATE,FPRREQD_SHIP_CODE,FPRREQD_VEND_PIDM, FPRREQD_VEND_REF_NUM,");
        query.append("		FPRREQD_PROJ_CODE,NULL,NULL,NULL,'N','Y',NULL,NULL,NULL,NULL,");
        query.append("		FPRREQD_TEXT_USAGE,FPRREQD_ATYP_CODE,FPRREQD_ATYP_SEQ_NUM,");
        query.append("		FPRREQD_RECOMM_VEND_NAME,FPRREQD_CURR_CODE,");
        query.append("		FPRREQD_CONVERTED_UNIT_PRICE,FPRREQD_DISC_AMT,FPRREQD_TAX_AMT,FPRREQD_ADDL_CHRG_AMT,FPRREQD_CONVERT_DISC_AMT,");
        query.append("		FPRREQD_CONVERT_TAX_AMT,FPRREQD_CONVERT_ADDL_CHRG_AMT,FPRREQD_TGRP_CODE,FPRREQD_AMT,FPRREQD_DESC_CHGE_IND");
        query.append("	FROM FPRREQD WHERE FPRREQD_REQH_CODE=(:oldDocCode);");
        query.append("	INSERT INTO FPRREQA(");
        query.append("		FPRREQA_REQH_CODE,FPRREQA_ITEM,FPRREQA_SEQ_NUM,FPRREQA_ACTIVITY_DATE,");
        query.append("		FPRREQA_USER_ID,FPRREQA_PCT,FPRREQA_AMT,FPRREQA_FSYR_CODE,");
        query.append("		FPRREQA_PERIOD,FPRREQA_RUCL_CODE,FPRREQA_COAS_CODE,FPRREQA_ACCI_CODE,");
        query.append("		FPRREQA_FUND_CODE,FPRREQA_ORGN_CODE,FPRREQA_ACCT_CODE,");
        query.append("		FPRREQA_PROG_CODE,FPRREQA_ACTV_CODE,FPRREQA_LOCN_CODE,FPRREQA_SUSP_IND,FPRREQA_NSF_SUSP_IND,FPRREQA_CANCEL_IND,FPRREQA_CANCEL_DATE,");
        query.append("		FPRREQA_PROJ_CODE,FPRREQA_APPR_IND,FPRREQA_NSF_OVERRIDE_IND,");
        query.append("		FPRREQA_ABAL_IND,FPRREQA_CONVERTED_AMT,FPRREQA_CLOSED_IND,FPRREQA_DISC_AMT,FPRREQA_TAX_AMT,FPRREQA_ADDL_CHRG_AMT,");
        query.append("		FPRREQA_CONVERT_DISC_AMT,FPRREQA_CONVERT_TAX_AMT,FPRREQA_CONVERT_ADDL_CHRG_AMT,FPRREQA_DISC_AMT_PCT,FPRREQA_ADDL_AMT_PCT,FPRREQA_TAX_AMT_PCT,");
        query.append("		FPRREQA_DISC_RUCL_CODE,FPRREQA_TAX_RUCL_CODE,FPRREQA_ADDL_RUCL_CODE)");
        query.append("	SELECT (:nextDocCode),FPRREQA_ITEM,FPRREQA_SEQ_NUM,SYSDATE,USER,FPRREQA_PCT, FPRREQA_AMT,FPRREQA_FSYR_CODE,FPRREQA_PERIOD,FPRREQA_RUCL_CODE,FPRREQA_COAS_CODE,");
        query.append("		FPRREQA_ACCI_CODE,FPRREQA_FUND_CODE,FPRREQA_ORGN_CODE,FPRREQA_ACCT_CODE,FPRREQA_PROG_CODE,FPRREQA_ACTV_CODE,FPRREQA_LOCN_CODE,'Y','Y',NULL,");
        query.append("		NULL,FPRREQA_PROJ_CODE,'N','N',NULL,FPRREQA_CONVERTED_AMT,NULL,FPRREQA_DISC_AMT,FPRREQA_TAX_AMT,FPRREQA_ADDL_CHRG_AMT,");
        query.append("		FPRREQA_CONVERT_DISC_AMT,FPRREQA_CONVERT_TAX_AMT,FPRREQA_CONVERT_ADDL_CHRG_AMT,FPRREQA_DISC_AMT_PCT,FPRREQA_ADDL_AMT_PCT,FPRREQA_TAX_AMT_PCT,");
        query.append("		FPRREQA_DISC_RUCL_CODE,FPRREQA_TAX_RUCL_CODE,FPRREQA_ADDL_RUCL_CODE");
        query.append("	FROM FPRREQA WHERE FPRREQA_REQH_CODE=(:oldDocCode);");
        query.append("	INSERT INTO FOBTEXT(FOBTEXT_DTYP_SEQ_NUM, FOBTEXT_CODE, FOBTEXT_CHG_SEQ_NUM,");
        query.append("		FOBTEXT_ITEM, FOBTEXT_SEQ_NUM, FOBTEXT_PIDM, FOBTEXT_ACTIVITY_DATE,");
        query.append("		FOBTEXT_USER_ID,FOBTEXT_CLA_NUM, FOBTEXT_PRT_IND, FOBTEXT_TEXT)");
        query.append("	SELECT FOBTEXT_DTYP_SEQ_NUM,(:nextDocCode),FOBTEXT_CHG_SEQ_NUM,FOBTEXT_ITEM,");
        query.append("		FOBTEXT_SEQ_NUM,FOBTEXT_PIDM,FOBTEXT_ACTIVITY_DATE,USER,");
        query.append("		FOBTEXT_CLA_NUM,FOBTEXT_PRT_IND,FOBTEXT_TEXT");
        query.append("	FROM FOBTEXT WHERE FOBTEXT_CODE=(:oldDocCode) AND FOBTEXT_DTYP_SEQ_NUM = 1;");
        query.append("	INSERT INTO FPRRQTX(FPRRQTX_REQH_CODE,FPRRQTX_ITEM,FPRRQTX_PRIORITY_NUM,FPRRQTX_TRAT_CODE,FPRRQTX_TAX_AMT,");
        query.append("		FPRRQTX_TAXABLE_AMT,FPRRQTX_PAY_TAX_TO,FPRRQTX_ACTIVITY_DATE,FPRRQTX_USER_ID)");
        query.append("	SELECT (:nextDocCode),FPRRQTX_ITEM,FPRRQTX_PRIORITY_NUM,FPRRQTX_TRAT_CODE,FPRRQTX_TAX_AMT,");
        query.append("		FPRRQTX_TAXABLE_AMT,FPRRQTX_PAY_TAX_TO,FPRRQTX_ACTIVITY_DATE,FPRRQTX_USER_ID");
        query.append("	FROM FPRRQTX WHERE FPRRQTX_REQH_CODE=(:oldDocCode);");
        query.append("END;");
        query.toString()
    }
}
