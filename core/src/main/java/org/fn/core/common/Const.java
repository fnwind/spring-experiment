package org.fn.core.common;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

@UtilityClass
public class Const {
    public static class Datetime {
        public static final DateTimeFormatter DATETIME = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").withResolverStyle(ResolverStyle.STRICT);
        public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
        public static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss").withResolverStyle(ResolverStyle.STRICT);
    }

    public static class MDC {
        public static final String TRACE_ID = "traceId";
        public static final String USERNAME = "username";
        public static final String TENANT_ID = "tenantId";
        public static final String URL = "url";
    }

    public static class Header {
        public static final String TRACE_ID = "X-Trace-Id";
        public static final String TENANT_ID = "tenant-id";
    }

    public static class Entity {
        public static final String CREATE_TIME = "createTime";
        public static final String UPDATE_TIME = "updateTime";
        public static final String TENANT_ID = "tenantId";
        public static final String DATA_TYPE = "dataType";
        public static final String DELETED = "deleted";
        public static final String CREATOR_ID = "creatorId";
        public static final String CREATOR_NAME = "creatorName";
        public static final String MODIFIER_ID = "modifierId";
        public static final String MODIFIER_NAME = "modifierName";
    }

    public static class Database {
        public static final String CREATE_TIME = "create_time";
        public static final String UPDATE_TIME = "update_time";
        public static final String TENANT_ID = "tenant_id";
        public static final String DATA_TYPE = "data_type";
        public static final String DELETED = "deleted";
        public static final String CREATOR_ID = "creator_id";
        public static final String CREATOR_NAME = "creator_name";
        public static final String MODIFIER_ID = "modifier_id";
        public static final String MODIFIER_NAME = "modifier_name";
    }

    public static class Default {
        public static final String ROOT_ACCOUNT_CODE = "root";
        public static final String DEFAULT_ORGANIZATION_CODE = "default_organization";
    }

    public static class SQL {
        public static final String OR_DELETED_IS_NOT_NULL = "or deleted is not null";
    }

    public static class ErrorCode {
        public static final String SUCCESS = "00000";
    }
}