package org.fn.persistence.query;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.PropDesc;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@UtilityClass
public class QueryGenerator {
    public static final String GE = "_ge";             // 大于等于
    public static final String LE = "_le";             // 小于等于
    public static final String WILDCARD = "*";         // 模糊匹配通配符
    public static final String COMMA = ",";            // IN/NOT_IN 分隔符
    public static final String NOT_EQUAL = "!";        // 不等于
    public static final String OR = "|";               // OR 条件分隔符
    public static final String IN = "_in";             // 包含
    public static final String NOT_IN = "_!in";        // 不包含

    // 内部查询规则枚举
    private enum Rule {
        GE, LE, EQ, NE, LIKE, LEFT_LIKE, RIGHT_LIKE, IN, NOT_IN
    }

    // OR 分隔符正则
    private static final String OR_SPLIT_REGEX = "\\|";

    private static final Map<Class<?>, Map<String, PropDesc>> PROP_CACHE = new ConcurrentHashMap<>();

    /**
     * 初始化 QueryWrapper
     *
     * @param clazz 实体类
     * @param query 查询条件
     * @param <T>   实体类型
     * @return QueryWrapper<T>
     */
    public static <T> QueryWrapper<T> initQueryWrapper(Class<T> clazz, Query<?> query) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        Map<String, String> data = query.getFilters();
        if (data != null) {
            // 处理 OR 条件
            Set<String> orKeys = data.keySet()
                    .stream()
                    .filter(k -> k.contains(OR))
                    .collect(Collectors.toSet());

            for (String key : orKeys) {
                String[] fields = key.split(OR_SPLIT_REGEX);
                String value = data.get(key);

                if (StrUtil.isBlank(value)) {
                    continue;
                }

                Map<String, String> orData = new HashMap<>();
                for (String field : fields) {
                    orData.put(field, value);
                }

                queryWrapper.and(w -> addQuery(orData, clazz, w, true));
                data.remove(key);
            }

            // 普通条件
            addQuery(data, clazz, queryWrapper, false);
        }

        // 排序
        List<SortCriteria> sortList = query.getSort();
        if (sortList != null) {
            for (SortCriteria sort : sortList) {
                if (!isEntityField(clazz, sort.getField())) continue;
                String column = fieldToColumn(sort.getField());
                if (sort.getDirection() == SortCriteria.Direction.ASC) {
                    queryWrapper.orderByAsc(column);
                } else {
                    queryWrapper.orderByDesc(column);
                }
            }
        }

        return queryWrapper;
    }

    /**
     * 添加条件到 QueryWrapper
     */
    private static void addQuery(Map<String, String> data, Class<?> clazz, QueryWrapper<?> wrapper, boolean isOr) {
        Map<String, PropDesc> propMap =
                PROP_CACHE.computeIfAbsent(clazz, c ->
                        BeanUtil.getBeanDesc(c)
                                .getProps()
                                .stream()
                                .collect(Collectors.toMap(PropDesc::getFieldName, p -> p))
                );

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (StrUtil.isBlank(value)) {
                continue;
            }

            String field = key;
            Rule rule = Rule.EQ;

            if (key.endsWith(GE)) {
                field = key.substring(0, key.length() - GE.length());
                rule = Rule.GE;
            } else if (key.endsWith(LE)) {
                field = key.substring(0, key.length() - LE.length());
                rule = Rule.LE;
            } else if (key.endsWith(NOT_EQUAL)) {
                field = key.substring(0, key.length() - NOT_EQUAL.length());
                rule = Rule.NE;
            } else if (key.endsWith(IN)) {
                field = key.substring(0, key.length() - IN.length());
                rule = Rule.IN;
            } else if (key.endsWith(NOT_IN)) {
                field = key.substring(0, key.length() - NOT_IN.length());
                rule = Rule.NOT_IN;
            } else if (key.startsWith(WILDCARD) && key.endsWith(WILDCARD)) {
                field = key.substring(1, key.length() - 1);
                rule = Rule.LIKE;
                value = escapeLike(value);
            } else if (key.startsWith(WILDCARD)) {
                field = key.substring(1);
                rule = Rule.LEFT_LIKE;
                value = escapeLike(value);
            } else if (key.endsWith(WILDCARD)) {
                field = key.substring(0, key.length() - 1);
                rule = Rule.RIGHT_LIKE;
                value = escapeLike(value);
            }

            PropDesc prop = propMap.get(field);
            if (prop == null) {
                continue;
            }

            Class<?> type = prop.getFieldClass();
            addQueryByRule(wrapper, field, type, value, rule, isOr);
        }
    }

    /**
     * 处理值并添加到 QueryWrapper
     */
    private static void addQueryByRule(QueryWrapper<?> wrapper, String name, Class<?> type, String value, Rule rule, boolean isOr) {
        if (StrUtil.isEmpty(value)) return;
        Object[] temp = convert(rule, value, type);
        addEasyQuery(wrapper, name, temp, rule, isOr);
    }

    /**
     * 值转换
     */
    private static Object[] convert(Rule rule, String value, Class<?> type) {
        if (rule == Rule.IN || rule == Rule.NOT_IN) {
            return Convert.toList(type, value.split(COMMA)).toArray();
        }
        return new Object[]{Convert.convert(type, value)};
    }

    /**
     * 添加条件到 QueryWrapper
     */
    private static void addEasyQuery(QueryWrapper<?> wrapper, String name, Object[] value, Rule rule, boolean isOr) {
        String column = fieldToColumn(name);
        switch (rule) {
            case GE -> wrapper.or(isOr).ge(column, value[0]);
            case LE -> wrapper.or(isOr).le(column, value[0]);
            case EQ -> wrapper.or(isOr).eq(column, value[0]);
            case NE -> wrapper.or(isOr).ne(column, value[0]);
            case LIKE -> wrapper.or(isOr).like(column, value[0]);
            case LEFT_LIKE -> wrapper.or(isOr).likeLeft(column, value[0]);
            case RIGHT_LIKE -> wrapper.or(isOr).likeRight(column, value[0]);
            case IN -> wrapper.or(isOr).in(column, value);
            case NOT_IN -> wrapper.or(isOr).notIn(column, value);
        }
    }

    /**
     * 驼峰字段转下划线
     */
    private static String fieldToColumn(String fieldName) {
        return com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(fieldName);
    }

    /**
     * 判断字段是否为实体字段
     */
    private static boolean isEntityField(Class<?> clazz, String fieldName) {
        List<Field> allFields = TableInfoHelper.getAllFields(clazz);
        return allFields.stream().anyMatch(f -> f.getName().equals(fieldName));
    }

    /**
     * 转义
     */
    private static String escapeLike(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
