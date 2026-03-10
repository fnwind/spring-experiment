package org.fn.core.common;

import lombok.Getter;

/**
 * Redis Database Index Enumeration
 *
 * @author chenshoufeng
 * @since 2026/3/10 下午1:31
 **/
@Getter
public enum RdbIndex {
    DB0(0),
    DB1(1),
    DB2(2),
    DB3(3),
    DB4(4),
    DB5(5),
    DB6(6),
    DB7(7),
    DB8(8),
    DB9(9),
    DB10(10),
    DB11(11),
    DB12(12),
    DB13(13),
    DB14(14),
    DB15(15);

    private final int index;

    RdbIndex(int index) {
        this.index = index;
    }

    public static RdbIndex of(int index) {
        for (RdbIndex rdbIndex : RdbIndex.values()) {
            if (rdbIndex.getIndex() == index) {
                return rdbIndex;
            }
        }
        throw new IllegalArgumentException("Invalid Redis Database Index: " + index);
    }

}
