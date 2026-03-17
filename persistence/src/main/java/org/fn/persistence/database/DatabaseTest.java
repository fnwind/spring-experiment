package org.fn.persistence.database;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.fn.persistence.database.mapper.AccountMapper;
import org.fn.persistence.query.PageQuery;
import org.fn.persistence.query.Query;
import org.fn.persistence.query.QueryGenerator;
import org.fn.persistence.entity.Account;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenshoufeng
 * @since 2026/3/16 下午1:34
 **/
@Component
@RequiredArgsConstructor
public class DatabaseTest {
    private final AccountMapper accountMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        // Account account = new Account();
        // account.setUsername("admin");
        // account.setNickname("管理员");
        // account.setPassword("123456");
        // account.setDataType(DataType.SYSTEM.getCode());
        // accountMapper.insert(account);

        // IntStream.range(0, 12).forEach(i -> {
        //     Account a = new Account();
        //     a.setUsername("user" + i);
        //     a.setNickname("用户" + i);
        //     a.setPassword("password" + i);
        //     accountMapper.insert(a);
        // });

        PageQuery<?> pageQuery = new PageQuery<>(1, 10);
        pageQuery.like("username", 0);

        QueryWrapper<Account> queryWrapper = QueryGenerator.initQueryWrapper(Account.class, pageQuery);
        Page<Account> accountPage = accountMapper.selectPage(pageQuery.toPage(), queryWrapper);
        System.out.println(">>> accountPage(" + accountPage.getTotal() + "): " + accountPage);


        // 前端请求
        Query<DemoDTO> query = new Query<>();
        query.likeRight("username", "a");
        query.setData(new DemoDTO("管理员"));

        // 后端处理
        DemoDTO data = query.getData();
        // ....
        QueryWrapper<Account> wrapper = QueryGenerator.initQueryWrapper(Account.class, query);
        wrapper.lambda().eq(StrUtil.isNotBlank(data.getName()), Account::getNickname, data.getName());

        // 最终查询
        List<Account> accounts = accountMapper.selectList(wrapper);
        System.out.println(">>> accounts(" + accounts.size() + "): " + accounts);
    }

    @Data
    @AllArgsConstructor
    static class DemoDTO {
        private String name;
    }
}