package com.baiyajin.entity.bean;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Data
@TableName("sys_user")
public class SystemUser implements Serializable {
  private String id;
  private String name;
  private String phone;
  private String password;
  private String statusID;  //0代表正常，1代表删除
  private String userTypeID;
  @TableField(exist = false)
  private String token;


  private Timestamp createTime;
  private Timestamp updateTime;

    @Override
  public int hashCode() {
    return Objects.hash(name, id);
  }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SystemUser)) {
            return false;
        }
        SystemUser user = (SystemUser) o;
        return name == user.getName() &&
                Objects.equals(id, user.getId());
    }



}
