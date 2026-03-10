package org.fn.core.common;

import lombok.Getter;

/**
 * @author chenshoufeng
 * @since 2026/3/6 上午9:29
 **/
@Getter
public enum DataType {

    SYSTEM(0),
    USER(1);

    private final int code;

    DataType(int code) {
        this.code = code;
    }

    public static DataType of(Integer code) {
        for (DataType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown DataType: " + code);
    }
}