package org.fn.persistence.query;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenshoufeng
 * @since 2026/2/9 上午10:56
 **/
@Data
public class Query<T> {
    protected Map<String, String> filters = new LinkedHashMap<>();
    protected List<SortCriteria> sort = new ArrayList<>();
    protected T data;

    public Query<T> eq(String field, Object value) {
        if (Objects.nonNull(value)) {
            filters.put(field, value.toString());
        }
        return this;
    }

    public Query<T> ne(String field, Object value) {
        if (Objects.nonNull(value)) {
            filters.put(field + QueryGenerator.NOT_EQUAL, value.toString());
        }

        return this;
    }

    public Query<T> like(String field, Object value) {
        if (value != null) {
            filters.put(QueryGenerator.WILDCARD + field + QueryGenerator.WILDCARD, value.toString());
        }
        return this;
    }

    public Query<T> likeLeft(String field, Object value) {
        if (value != null) {
            filters.put(QueryGenerator.WILDCARD + field, value.toString());
        }
        return this;
    }

    public Query<T> likeRight(String field, Object value) {
        if (value != null) {
            filters.put(field + QueryGenerator.WILDCARD, value.toString());
        }
        return this;
    }

    public Query<T> in(String field, Collection<?> values) {
        if (values != null && !values.isEmpty()) {
            filters.put(field + QueryGenerator.IN, join(values));
        }
        return this;
    }

    public Query<T> notIn(String field, Collection<?> values) {
        if (values != null && !values.isEmpty()) {
            filters.put(field + QueryGenerator.NOT_IN, join(values));
        }
        return this;
    }

    public Query<T> ge(String field, Object value) {
        if (value != null) {
            filters.put(field + QueryGenerator.GE, value.toString());
        }
        return this;
    }

    public Query<T> le(String field, Object value) {
        if (value != null) {
            filters.put(field + QueryGenerator.LE, value.toString());
        }
        return this;
    }

    public Query<T> orderByAsc(String field) {
        sort.add(SortCriteria.asc(field));
        return this;
    }

    public Query<T> orderByDesc(String field) {
        sort.add(SortCriteria.desc(field));
        return this;
    }

    private String join(Collection<?> values) {
        return values.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
