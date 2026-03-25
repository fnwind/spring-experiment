package org.fn.core.common;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;
import org.fn.core.identity.IdentityUser;

/**
 * @author chenshoufeng
 * @since 2026/3/4 上午10:09
 **/
@UtilityClass
public class Global {
    public static class TraceId {
        private static final TransmittableThreadLocal<String> TRACE_ID =
                TransmittableThreadLocal.withInitial(() -> null);

        public static String get() {
            return TRACE_ID.get();
        }

        public static void set(String traceId) {
            TRACE_ID.set(traceId);
        }

        public static void clear() {
            TRACE_ID.remove();
        }
    }

    public static class Tenant {
        public static final String DEFAULT_TENANT_ID = "0";

        private static final TransmittableThreadLocal<String> TENANT_ID =
                TransmittableThreadLocal.withInitial(() -> DEFAULT_TENANT_ID);

        public static String get() {
            return TENANT_ID.get();
        }

        public static void set(String tenantId) {
            TENANT_ID.set(tenantId);
        }

        public static void clear() {
            TENANT_ID.remove();
        }
    }

    public static class User {
        private static final TransmittableThreadLocal<IdentityUser> USER =
                TransmittableThreadLocal.withInitial(() -> null);

        public static IdentityUser get() {
            return USER.get();
        }

        public static void set(IdentityUser identityUser) {
            USER.set(identityUser);
        }

        public static void clear() {
            USER.remove();
        }
    }

    public static class Redis {
        private static final TransmittableThreadLocal<RdbIndex> INDEX =
                TransmittableThreadLocal.withInitial(() -> null);

        public static RdbIndex get() {
            return INDEX.get();
        }

        public static void set(RdbIndex rdbIndex) {
            INDEX.set(rdbIndex);
        }

        public static void clear() {
            INDEX.remove();
        }
    }
}
