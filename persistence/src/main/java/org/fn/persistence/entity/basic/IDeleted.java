package org.fn.persistence.entity.basic;

import org.fn.core.common.Const;

import java.time.LocalDateTime;

/**
 * 逻辑删除
 *
 * @see <a href="https://baomidou.com/guides/logic-delete/">逻辑删除支持 | MyBatis-Plus</a>
 */
public interface IDeleted {
    String COLUMN = Const.Database.DELETED;
    String FIELD = Const.Entity.DELETED;

    LocalDateTime getDeleted();
}