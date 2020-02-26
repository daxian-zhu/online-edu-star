package com.clark.daxian.entity;

import com.clark.daxian.mybatis.annotation.Column;
import com.clark.daxian.mybatis.annotation.Table;
import com.clark.daxian.mybatis.annotation.UserParent;
import lombok.Data;

@Data
@UserParent
@Table(name="user")
public class User extends Base{

    @Column(name="name")
    private String nameBig;

    private String telephone;
}
