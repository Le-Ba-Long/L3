package com.globits.da.common;

public enum ErrorMessage {
    EMAIL_IS_NULL(1, "EMAIL Can Not Blank Or Null "),
    EMAIL_INVALID(2, "EMAIL Invalid"),
    CODE_IS_NULL(3, "Code Can Not Null"),
    LENGTH_CODE_INVALID(4, "The Code Must Be Between 6 And 10 Characters Long"),
    CODE_IS_EXIST(5, "Code Already Exists"),
    CHARACTER_CODE_INVALID(6, "Code Must Not Contain Spaces"),
    AGE_INVALID(7, "Age Cannot Be Negative"),
    NAME_IS_NULL(8, "NAME Cannot Be Blank Or Null"),
    NAME_IS_EXIST(9, "NAME Already Exists"),
    PHONE_IS_NULL(10, "Phone Number Cannot Be Blank Or Null"),
    LENGTH_PHONE_INVALID(11, "Phone Number No More Than 11 Characters Long"),
    CHARACTER_PHONE_INVALID(12, "The Characters In The Phone Number Must Be Numbers"),
    COMMUNE_IS_NULL(13, "Communal Information Cannot Be Blank Or Null"),
    COMMUNE_NOT_FOUND(14, "Commune Does Not Exist"),
    COMMUNE_NOT_IN_DISTRICT(15, "Commune Not In District"),
    DISTRICT_IS_NULL(16, "District Information Cannot Be Blank Or Null"),
    DISTRICT_NOT_FOUND(17, "District Does Not Exist"),
    DISTRICT_NOT_IN_PROVINCE(18, " District Not In Province "),
    PROVINCE_IS_NULL(19, "Province Information Cannot Be Blank Or Null"),

    PROVINCE_NOT_FOUND(20, "Province Does Not Exist"),

    EMPLOYEE_IS_NULL(21, "Employee Information Cannot Be Blank Or Null"),

    EMPLOYEE_NOT_FOUND(22, "Employee Does Not Exist"),
    CERTIFICATE_IS_NULL(23, "Certificate Information Cannot Be Blank Or Null"),

    CERTIFICATE_NOT_FOUND(24, "Certificate Does Not Exist"),
    CERTIFICATE_HAS_EFFECT(25, "Certificate is still valid, cannot be inserted"),
    NUMBER_CERTIFICATE_EXCEED(26, "Exceeding The Limit On The Number Of Certificates, The Number Of Certificate Of The Same Type Must Not Exceed 3"),

    FILE_EXCEL_IS_Blank(27, "Blank excel file!"),
    ID_IS_NULL(28, "ID Cannot Be Blank Or Null"),

    ID_IS_EXIST(29, "ID Already Exists"),

    ID_NOT_EXIST(30, "ID Not Exists"),

    EXPORT_FILE_FAIL(31, "Export fail"),
    LIST_IS_EMPTY(32, "List Is Empty"),

    NOT_SUCCESS(33, "Not Success"),
    PROVINCE_ID_NOT_EXIST(33, "Province Id Not Exist"),
    EMAIL_IS_EXIST(34, "EMAIL Already Exists"),

    DATA_IS_NULL(35, "Data Is Null"),
    AGE_IS_NULL(36, "Age Is Null"),

    OBJECT_NOT_EXIST(37, "Object Not Exist"),
    ACTION_NOT_EXIST(38, "Action Not Exists"),
    OBJECT_CANNOT_EMPTY(39, "Object  CanNot Is Empty"),

    SUCCESS(200, "Success!");

    private final int code;
    private final String message;

    ErrorMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
