package org.example.workingmoney.repository.common;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import org.apache.ibatis.executor.Executor;

@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class AuditInterceptor implements Interceptor {

    private final Clock clock;

    public AuditInterceptor(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        if (parameter == null) return invocation.proceed();

        SqlCommandType type = ms.getSqlCommandType();
        LocalDateTime now = LocalDateTime.now(clock);

        if (type == SqlCommandType.INSERT) {
            apply(parameter, "createdAt", now, true);
            apply(parameter, "updatedAt", now, true);
        } else if (type == SqlCommandType.UPDATE) {
            apply(parameter, "updatedAt", now, false);
        }
        return invocation.proceed();
    }

    private void apply(Object target, String field, LocalDateTime value, boolean onlyIfNull) {
        if (target instanceof Map<?, ?> map) {
            for (Object v : map.values()) apply(v, field, value, onlyIfNull);
            return;
        }
        if (target instanceof Collection<?> col) {
            for (Object v : col) apply(v, field, value, onlyIfNull);
            return;
        }
        setIfPresent(target, field, value, onlyIfNull);
    }

    private void setIfPresent(Object target, String fieldName, LocalDateTime value, boolean onlyIfNull) {
        if (target == null) return;
        Field f = findField(target.getClass(), fieldName);
        if (f == null || f.getType() != LocalDateTime.class) return;
        try {
            f.setAccessible(true);
            Object cur = f.get(target);
            if (!onlyIfNull || cur == null) f.set(target, value);
        } catch (IllegalAccessException ignored) {}
    }

    private Field findField(Class<?> type, String name) {
        for (Class<?> c = type; c != null && c != Object.class; c = c.getSuperclass()) {
            try { return c.getDeclaredField(name); } catch (NoSuchFieldException ignored) {}
        }
        return null;
    }
}
