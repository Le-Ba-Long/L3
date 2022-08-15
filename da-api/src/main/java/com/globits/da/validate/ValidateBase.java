package com.globits.da.validate;

import com.globits.core.dto.BaseObjectDto;
import com.globits.da.Constants;
import com.globits.da.common.ErrorMessage;

import java.util.regex.Matcher;

public abstract class ValidateBase extends BaseObjectDto {


    public static Boolean checkIdIsNull(Integer id) {
        return id == null;
    }

    public static ErrorMessage checkCodeIsNull(String code) {
        if (code == null || code.isEmpty())
            return ErrorMessage.CODE_IS_NULL;
        return ErrorMessage.SUCCESS;
    }

    public static ErrorMessage checkValidLengthOfCode(String code) {
        if ((code.length() < Constants.MIN_LENGTH_CODE || code.length() > Constants.MAX_LENGTH_CODE))
            return ErrorMessage.LENGTH_CODE_INVALID;
        return ErrorMessage.SUCCESS;
    }

    public static ErrorMessage checkValidCodeContainSpace(String code) {
        if (code.contains(Constants.SPACE))
            return ErrorMessage.CHARACTER_CODE_INVALID;
        return ErrorMessage.SUCCESS;
    }

    public static ErrorMessage checkNameIsNull(String name) {
        if (name == null || name.isEmpty())
            return ErrorMessage.NAME_IS_NULL;
        return ErrorMessage.SUCCESS;

    }

    public static ErrorMessage checkEmailIsNull(String email) {
        if (email == null || email.isEmpty())
            return ErrorMessage.EMAIL_IS_NULL;
        return ErrorMessage.SUCCESS;

    }

    public static ErrorMessage checkValidEmail(String email) {
        Matcher matcher = Constants.VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if (matcher.find())
            return ErrorMessage.EMAIL_INVALID;
        return ErrorMessage.SUCCESS;

    }

    public static ErrorMessage checkPhoneIsNull(String phone) {
        if (phone == null || phone.isEmpty())
            return ErrorMessage.PHONE_IS_NULL;
        return ErrorMessage.SUCCESS;
    }

    public static ErrorMessage checkValidLengthOfPhone(String phone) {
        if ((phone.length() < Constants.MAX_LENGTH_PHONE))
            return ErrorMessage.LENGTH_PHONE_INVALID;
        return ErrorMessage.SUCCESS;
    }

    public static ErrorMessage checkValidPhoneIsNumber(String phone) {
        if (phone.matches(Constants.REGEX_VALID_PHONE))
            return ErrorMessage.CHARACTER_PHONE_INVALID;
        return ErrorMessage.SUCCESS;
    }

    public static ErrorMessage checkValidAge(int age) {
        if (age > 0)
            return ErrorMessage.AGE_INVALID;
        return ErrorMessage.SUCCESS;
    }

    public static ErrorMessage checkDistrictIsNull(String district) {
        if (district == null || district.isEmpty())
            return ErrorMessage.DISTRICT_IS_NULL;
        return ErrorMessage.SUCCESS;
    }

    public static ErrorMessage checkCommuneIsNull(String commune) {
        if (commune == null || commune.isEmpty())
            return ErrorMessage.COMMUNE_IS_NULL;
        return ErrorMessage.SUCCESS;
    }

    public static int resultStatusCode(ErrorMessage resultErrorMessage) {
        switch (resultErrorMessage) {
            case ID_IS_EXIST:
                return ErrorMessage.ID_IS_EXIST.getCode();
            case CODE_IS_EXIST:
                return ErrorMessage.CODE_IS_EXIST.getCode();
            case CODE_IS_NULL:
                return ErrorMessage.CODE_IS_NULL.getCode();
            case CHARACTER_CODE_INVALID:
                return ErrorMessage.CHARACTER_CODE_INVALID.getCode();
            case LENGTH_CODE_INVALID:
                return ErrorMessage.LENGTH_CODE_INVALID.getCode();
            case NAME_IS_EXIST:
                return ErrorMessage.NAME_IS_EXIST.getCode();
            case NAME_IS_NULL:
                return ErrorMessage.NAME_IS_NULL.getCode();
            case PROVINCE_ID_NOT_EXIST:
                return ErrorMessage.PROVINCE_ID_NOT_EXIST.getCode();
            case EMAIL_IS_NULL:
                return ErrorMessage.EMAIL_IS_NULL.getCode();
            case EMAIL_INVALID:
                return ErrorMessage.EMAIL_INVALID.getCode();
            case AGE_INVALID:
                return ErrorMessage.AGE_INVALID.getCode();
            case PHONE_IS_NULL:
                return ErrorMessage.PHONE_IS_NULL.getCode();
            case LENGTH_PHONE_INVALID:
                return ErrorMessage.LENGTH_PHONE_INVALID.getCode();
            case CHARACTER_PHONE_INVALID:
                return ErrorMessage.CHARACTER_PHONE_INVALID.getCode();
            case COMMUNE_IS_NULL:
                return ErrorMessage.COMMUNE_IS_NULL.getCode();
            case COMMUNE_NOT_FOUND:
                return ErrorMessage.COMMUNE_NOT_FOUND.getCode();
            case COMMUNE_NOT_IN_DISTRICT:
                return ErrorMessage.COMMUNE_NOT_IN_DISTRICT.getCode();
            case DISTRICT_IS_NULL:
                return ErrorMessage.DISTRICT_IS_NULL.getCode();
            case DISTRICT_NOT_FOUND:
                return ErrorMessage.DISTRICT_NOT_FOUND.getCode();
            case DISTRICT_NOT_IN_PROVINCE:
                return ErrorMessage.DISTRICT_NOT_IN_PROVINCE.getCode();
            case PROVINCE_IS_NULL:
                return ErrorMessage.PROVINCE_IS_NULL.getCode();
            case PROVINCE_NOT_FOUND:
                return ErrorMessage.PROVINCE_NOT_FOUND.getCode();
            case EMPLOYEE_IS_NULL:
                return ErrorMessage.EMPLOYEE_IS_NULL.getCode();
            case EMPLOYEE_NOT_FOUND:
                return ErrorMessage.EMPLOYEE_NOT_FOUND.getCode();
            case DIPLOMA_NOT_FOUND:
                return ErrorMessage.DIPLOMA_NOT_FOUND.getCode();
            case DIPLOMA_HAS_EFFECT:
                return ErrorMessage.DIPLOMA_HAS_EFFECT.getCode();
            case NUMBER_DIPLOMA_EXCEED:
                return ErrorMessage.NUMBER_DIPLOMA_EXCEED.getCode();
            case FILE_EXCEL_IS_Blank:
                return ErrorMessage.FILE_EXCEL_IS_Blank.getCode();
            case EXPORT_FILE_FAIL:
                return ErrorMessage.EXPORT_FILE_FAIL.getCode();
            case LIST_IS_EMPTY:
                return ErrorMessage.LIST_IS_EMPTY.getCode();
            case EMAIL_IS_EXIST:
                return ErrorMessage.EMAIL_IS_EXIST.getCode();
            case NOT_SUCCESS:
                return ErrorMessage.NOT_SUCCESS.getCode();
            case SUCCESS:
                return ErrorMessage.SUCCESS.getCode();

        }
        return 0;
    }

}
