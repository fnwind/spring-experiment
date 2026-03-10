package org.fn.web.enhance.systemlog;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.fn.core.common.Global;
import org.fn.core.model.SystemLogModel;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class SystemLogAspect {
    private final static int MAX_STACK_TRACE_LINES = 8;

    @Around("@annotation(SystemLog)")
    public Object logAfterMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        Throwable ex = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            ex = e;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SystemLog systemLog = method.getAnnotation(SystemLog.class);
        if (systemLog.onError() && ex == null) return result;

        SystemLogModel systemLogModel = new SystemLogModel();
        systemLogModel.setDescription(systemLog.value());
        systemLogModel.setLogType(systemLog.logType());
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        systemLogModel.setStartTime(startTime);

        String source = method.getDeclaringClass().getSimpleName() + "#" + method.getName();
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            systemLogModel.setUrl(request.getRequestURI());
            systemLogModel.setMethod(request.getMethod());
        }
        Object[] args = joinPoint.getArgs();
        Object[] filteredArgs = Arrays.stream(args)
                .filter(arg -> !(arg instanceof HttpServletRequest))
                .filter(arg -> !(arg instanceof HttpServletResponse))
                .filter(arg -> !(arg instanceof MultipartFile))
                .filter(arg -> !(arg instanceof InputStream))
                .filter(arg -> !(arg instanceof byte[]))
                .toArray();
        Map<String, Object> paramMap = new LinkedHashMap<>();
        String[] parameterNames = signature.getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            paramMap.put(parameterNames[i], filteredArgs[i]);
        }
        if (!paramMap.isEmpty()) {
            systemLogModel.setArgs(paramMap);
        }
        systemLogModel.setSource(source);
        systemLogModel.setDuration(duration);
        systemLogModel.setTraceId(Global.TraceId.get());
        systemLogModel.setTenantId(Global.Tenant.get());
        if (ex != null) {
            systemLogModel.setException(ex.getClass().getName());
            systemLogModel.setStackTrace(getTruncatedStackTrace(ex));
            systemLogModel.setSuccess(false);
            log.error("{}", JSONUtil.toJsonStr(systemLogModel));
        } else {
            systemLogModel.setSuccess(true);
            log.info("{}", JSONUtil.toJsonStr(systemLogModel));
        }

        if (ex != null) throw ex;

        return result;
    }

    private String getTruncatedStackTrace(Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String[] lines = sw.toString().split(System.lineSeparator());
        if (lines.length <= MAX_STACK_TRACE_LINES) {
            return sw.toString();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < MAX_STACK_TRACE_LINES; i++) {
            sb.append(lines[i]).append(System.lineSeparator());
        }
        sb.append("... (stack trace truncated, total lines=").append(lines.length).append(")");
        return sb.toString();
    }
}