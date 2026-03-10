package org.fn.core.excel;

import com.alibaba.excel.write.handler.WriteHandler;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author chenshoufeng
 * @since 2026/2/11 上午10:52
 **/
@Getter
public class ExportOptions {
    private boolean i18n = true;
    private boolean autoWidth = false;
    private Locale locale;
    private final List<WriteHandler> customHandlers = new ArrayList<>();

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ExportOptions options = new ExportOptions();

        public Builder i18n(boolean i18n) {
            options.i18n = i18n;
            return this;
        }

        public Builder autoWidth(boolean autoWidth) {
            options.autoWidth = autoWidth;
            return this;
        }

        public Builder locale(Locale locale) {
            options.locale = locale;
            return this;
        }

        public Builder addHandler(WriteHandler handler) {
            options.customHandlers.add(handler);
            return this;
        }

        public ExportOptions build() {
            return options;
        }
    }
}
