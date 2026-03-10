package org.fn.persistence.entity.basic;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OwnerEntity extends BasicEntity implements IOwner {
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    @TableField(fill = FieldFill.UPDATE)
    private Long modifierId;
    @TableField(fill = FieldFill.UPDATE)
    private String modifierName;

    private LocalDateTime deleted;
}
