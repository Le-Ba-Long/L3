package com.globits.da.validate;


import com.globits.da.dto.EmployeeDTO;

public class ValidateEmployee extends ValidateBase {
    // hàm kiểm tra xem cách thuộc tính của nhân viên có rỗng hay không
    public static boolean checkEmployeeIsEmpty(EmployeeDTO employeeDTO) {
        return !employeeDTO.getCode().isEmpty()
                && !employeeDTO.getName().isEmpty()
                && !employeeDTO.getEmail().isEmpty()
                && !employeeDTO.getPhone().isEmpty()
                && !String.valueOf(employeeDTO.getAge()).isEmpty();
    }

    // hàm kiểm tra xem cách thuộc tính của nhân viên có null hay không
    public static boolean checkEmployeeIsNull(EmployeeDTO employeeDTO) {
        return employeeDTO.getCode() != null
                && employeeDTO.getName() != null
                && employeeDTO.getEmail() != null
                && employeeDTO.getPhone() != null;
    }


}
