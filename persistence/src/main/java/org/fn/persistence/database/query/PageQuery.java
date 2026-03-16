package org.fn.persistence.database.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.poi.ss.formula.functions.T;

/**
 * @author chenshoufeng
 * @since 2026/2/9 上午10:53
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PageQuery<T> extends Query<T> {
    private Integer page = 1;
    private Integer size = 20;

    public <E> Page<E> toPage() {
        // TODO: 可以添加一些校验逻辑，比如 page 和 size 的合理范围
        return new Page<>(page, size);
    }
}
