package org.fn.core.excel;

import java.util.ArrayList;
import java.util.List;

public class SheetCollector {
    private final List<SheetSpec<?>> sheets = new ArrayList<>();

    public <T> void add(String sheetName, Class<T> headClass, List<T> data) {
        sheets.add(new SheetSpec<>(sheetName, headClass, data));
    }

    List<SheetSpec<?>> getSheets() {
        return sheets;
    }
}