package com.mapletestone.revitPlugin.dao.entity;

import com.mapletestone.revitPlugin.dao.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author hmx
 * @since 2025-09-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Record extends BaseEntity {


    private String record;

    private String filePath;


}
