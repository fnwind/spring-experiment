package org.fn.core.model;

import lombok.Data;
import org.fn.core.common.LogType;

import java.util.Map;

/**
 * @author chenshoufeng
 * @since 2026/3/4 下午1:53
 **/
@Data
public class SystemLogModel {
    private String url;
    private Map<String, Object> args;
    private String source;
    private Long duration;
    private String method;
    private String exception;
    private String stackTrace;
    private String tenantId;
    private String traceId;
    private Long startTime;
    private boolean success;
    private String description;
    private LogType logType;
}
