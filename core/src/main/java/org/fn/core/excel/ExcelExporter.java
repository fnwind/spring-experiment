package org.fn.core.excel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author chenshoufeng
 * @since 2026/2/11 上午9:33
 **/
@Component
@RequiredArgsConstructor
public class ExcelExporter {
    private final ExcelExportFactory exportFactory;

    public <T> void export(OutputStream os, Class<T> headClass,
                           List<T> data, String sheetName, ExportOptions options) {
        Objects.requireNonNull(headClass, "headClass must not be null");
        exportFactory.write(os, headClass, data, sheetName, options);
    }

    public <T> void export(OutputStream os, List<T> data,
                           String sheetName, ExportOptions options) {
        export(os, resolveHeadClass(data), data, sheetName, options);
    }

    public void export(OutputStream os, Consumer<SheetCollector> sheetConsumer,
                       ExportOptions options) {
        SheetCollector collector = new SheetCollector();
        sheetConsumer.accept(collector);

        if (collector.getSheets().isEmpty()) {
            throw new IllegalArgumentException("No sheets defined for export");
        }

        exportFactory.write(os, collector.getSheets(), options);
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> resolveHeadClass(List<T> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Cannot resolve head class from empty export data. Please specify headClass explicitly.");
        }
        return (Class<T>) data.getFirst().getClass();
    }
}
