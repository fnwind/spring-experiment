package org.fn.core.excel;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.Map;

public class AutoWidthCellWriteHandler implements CellWriteHandler {
    private static final int PADDING = 4;

    /**
     * 每个 Sheet 的列宽缓存
     * key = sheetIndex, value = Map<列号, 最大字符长度>
     */
    private final Map<Integer, Map<Integer, Integer>> maxColumnWidthMap = new HashMap<>();

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        Cell cell = context.getCell();
        int sheetIndex = context.getWriteSheetHolder().getSheetNo();
        int columnIndex = cell.getColumnIndex();
        String cellValue = cell.toString();

        // 计算字符长度（中文按 2 个长度算）
        int len = getCellLength(cellValue);

        maxColumnWidthMap
                .computeIfAbsent(sheetIndex, k -> new HashMap<>())
                .merge(columnIndex, len, Math::max);

        // 实时设置列宽
        Sheet sheet = context.getWriteSheetHolder().getSheet();
        int width = maxColumnWidthMap.get(sheetIndex).get(columnIndex);
        sheet.setColumnWidth(columnIndex, Math.min((width + PADDING) * 256, 255 * 256));
    }

    /**
     * 简单计算列宽
     */
    private int getCellLength(String cellValue) {
        if (cellValue == null) return 0;
        int len = 0;
        for (char c : cellValue.toCharArray()) {
            len += (c > 255) ? 2 : 1; // 中文字符算2
        }
        return len;
    }
}
