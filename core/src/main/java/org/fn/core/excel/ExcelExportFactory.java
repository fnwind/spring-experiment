package org.fn.core.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

/**
 * @author chenshoufeng
 * @since 2026/2/11 上午9:34
 **/
@Component
@RequiredArgsConstructor
public class ExcelExportFactory {
    private final MessageSource messageSource;

    public <T> void write(OutputStream os,
                          Class<T> headClass, List<T> data, String sheetName, ExportOptions options) {
        write(os, List.of(new SheetSpec<>(sheetName, headClass, data)), options);
    }

    public void write(OutputStream os, List<SheetSpec<?>> sheets, ExportOptions options) {
        if (sheets == null || sheets.isEmpty()) {
            throw new IllegalArgumentException("Export sheets must not be empty");
        }

        // 默认选项
        ExportOptions opts = options != null ? options : ExportOptions.builder().build();

        ExcelWriterBuilder writerBuilder = EasyExcel.write(os).autoCloseStream(false);
        if (opts.isI18n() && messageSource != null) {
            Locale finalLocale = opts.getLocale() != null
                    ? opts.getLocale()
                    : LocaleContextHolder.getLocale();
            writerBuilder.registerWriteHandler(new I18nCellWriteHandler(messageSource, finalLocale));
        }

        // 注册其他自定义处理器
        for (WriteHandler handler : opts.getCustomHandlers()) {
            writerBuilder.registerWriteHandler(handler);
        }

        // 列宽自适应
        if (opts.isAutoWidth()) {
            writerBuilder.registerWriteHandler(new AutoWidthCellWriteHandler());
        }

        ExcelWriter writer = writerBuilder.build();

        try {
            int sheetNo = 0;

            for (SheetSpec<?> sheet : sheets) {
                WriteSheet writeSheet = EasyExcel.writerSheet(sheetNo++, sheet.getSheetName())
                        .head(sheet.getHeadClass())
                        .build();
                writer.write(sheet.getData(), writeSheet);
            }
        } finally {
            writer.finish();
        }
    }
}
