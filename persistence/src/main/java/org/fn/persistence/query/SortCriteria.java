package org.fn.persistence.query;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author chenshoufeng
 * @since 2026/2/9 上午11:06
 **/
@Data
@AllArgsConstructor
public class SortCriteria {
    private String field;
    private Direction direction;

    public static SortCriteria asc(String field) {
        return new SortCriteria(field, Direction.ASC);
    }

    public static SortCriteria desc(String field) {
        return new SortCriteria(field, Direction.DESC);
    }

    public enum Direction {
        ASC,
        DESC
    }
}
