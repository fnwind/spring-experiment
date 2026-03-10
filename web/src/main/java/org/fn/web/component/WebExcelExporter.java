package org.fn.web.component;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.fn.core.excel.ExcelExporter;
import org.fn.core.excel.ExportOptions;
import org.fn.core.excel.SheetCollector;
import org.fn.web.common.DownloadUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class WebExcelExporter {
    private final ExcelExporter excelExporter;

    public ResponseEntity<byte[]> export(String filename,
                                         Consumer<SheetCollector> sheetConsumer,
                                         ExportOptions options) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        excelExporter.export(stream, sheetConsumer, options);
        return DownloadUtil.download(filename, stream.toByteArray());
    }

    public ResponseEntity<byte[]> export(String filename,
                                         List<?> data,
                                         String sheetName,
                                         ExportOptions options) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        excelExporter.export(stream, data, sheetName, options);
        return DownloadUtil.download(filename, stream.toByteArray());
    }

    public StreamingResponseBody export(HttpServletResponse response,
                                        String filename,
                                        Consumer<SheetCollector> sheetConsumer,
                                        ExportOptions options) {
        return DownloadUtil.download(response, filename, outputStream -> {
            excelExporter.export(outputStream, sheetConsumer, options);
        });
    }

    public StreamingResponseBody export(HttpServletResponse response,
                                        String filename,
                                        List<?> data,
                                        String sheetName,
                                        ExportOptions options) {
        return DownloadUtil.download(response, filename, outputStream -> {
            excelExporter.export(outputStream, data, sheetName, options);
        });
    }
}