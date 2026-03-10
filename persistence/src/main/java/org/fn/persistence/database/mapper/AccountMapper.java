package org.fn.persistence.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.fn.persistence.entity.Account;

/**
 * @author chenshoufeng
 * @since 2026/3/5 上午10:55
 **/
@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}
