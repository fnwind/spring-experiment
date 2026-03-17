package org.fn.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author chenshoufeng
 * @since 2026/3/17 下午4:14
 **/
@Data
@AllArgsConstructor
public class FieldErrorResp {
    private String field;
    private String message;
}
