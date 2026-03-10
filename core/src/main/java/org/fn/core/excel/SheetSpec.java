package org.fn.core.excel;

import lombok.Getter;

import java.util.List;

/**
 * @author chenshoufeng
 * @since 2026/2/11 上午10:22
 **/
@Getter
public class SheetSpec<T> {
    private final String sheetName;
    private final Class<T> headClass;
    private final List<T> data;

    public SheetSpec(String sheetName, Class<T> headClass, List<T> data) {
        this.sheetName = sheetName;
        this.headClass = headClass;
        this.data = data;
    }
}
