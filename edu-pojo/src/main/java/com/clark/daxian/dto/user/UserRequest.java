package com.clark.daxian.dto.user;

import com.clark.daxian.dto.base.PageRequest;
import lombok.Data;

/**
 * 用户列表查询
 * @author clark
 */
@Data
public class UserRequest extends PageRequest {

    private String email;

    private String telephone;


}
