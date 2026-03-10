package org.fn.core.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import jakarta.annotation.Nullable;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.context.MessageSource;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.List;
import java.util.Locale;

/**
 * @author chenshoufeng
 * @since 2026/2/10 下午4:13
 **/
public class I18nCellWriteHandler implements CellWriteHandler {

    private final PropertyPlaceholderHelper helper;
    private final MessageSource messageSource;
    private final Locale locale;

    public I18nCellWriteHandler(MessageSource messageSource, @Nullable Locale locale) {
        this.helper = new PropertyPlaceholderHelper(
                "${",
                "}",
                ":",
                '\\',
                true
        );
        this.messageSource = messageSource;
        this.locale = locale == null ? Locale.getDefault() : locale;
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                 List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        // 只处理表头
        if (!Boolean.TRUE.equals(isHead)) {
            return;
        }

        String original = cell.getStringCellValue();
        if (original == null || !original.contains("${")) {
            return;
        }

        // 使用 MessageSource 解析占位符
        PropertyPlaceholderHelper.PlaceholderResolver resolver = key -> {
            String defaultValue = null;
            // 处理默认值语法 ${key:defaultValue}
            int separatorIndex = key.indexOf(':');
            if (separatorIndex != -1) {
                defaultValue = key.substring(separatorIndex + 1);
                key = key.substring(0, separatorIndex);
            }
            try {
                return messageSource.getMessage(key, null, defaultValue, locale);
            } catch (Exception e) {
                return defaultValue;
            }
        };

        String resolved = helper.replacePlaceholders(original, resolver);
        cell.setCellValue(resolved);
    }
}
