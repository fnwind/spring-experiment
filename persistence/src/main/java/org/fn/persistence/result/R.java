package org.fn.persistence.result;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import org.fn.core.common.Const;
import org.fn.core.exception.errorcode.ErrorCode;

/**
 * @author chenshoufeng
 * @since 2026/3/17 下午2:59
 **/

@Getter
@Builder
@JsonPropertyOrder({"code", "success", "message", "i18n", "traceId", "data"})
public class R<T> {
    private String code;
    private String i18n;
    @JsonIgnore
    private Object[] args;
    private String message;
    private T data;
    private boolean success;
    private String traceId;

    public static <T> R<T> success() {
        return success(null);
    }

    public static <T> R<T> success(T data) {
        return R.<T>builder()
                .code(Const.ErrorCode.SUCCESS)
                .i18n("common.r.success")
                .data(data)
                .success(true)
                .build();
    }

    public static <T> R<T> fail(ErrorCode errorCode) {
        return R.<T>builder()
                .code(errorCode.getCode())
                .i18n(errorCode.getI18n())
                .success(false)
                .build();
    }

    public static <T> R<T> fail(ErrorCode errorCode, String i18n, Object... args) {
        return R.<T>builder()
                .code(errorCode.getCode())
                .i18n(i18n)
                .args(args)
                .success(false)
                .build();
    }
}
