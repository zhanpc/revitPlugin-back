package com.mapletestone.revitPlugin.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 * @date
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -1342729126453968851L;

    @Id
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String createdUser;

    private Date createdTime;

    private String lastModifiedUser;

    private Date lastModifiedTime;
}
