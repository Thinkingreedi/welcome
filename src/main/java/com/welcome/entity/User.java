package com.welcome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.welcome.util.LongJsonDeserializer;
import com.welcome.util.LongJsonSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 姓名
     */
    private String nickName;


    /**
     * 性别
     */
    private String gender;


    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 状态 0:禁用，1:正常
     */
    private Integer status;

    // 没有is_deleted字段,因为我们不允许用户注销
}
