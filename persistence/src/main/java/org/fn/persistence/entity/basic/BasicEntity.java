package org.fn.persistence.entity.basic;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BasicEntity implements IEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
}
