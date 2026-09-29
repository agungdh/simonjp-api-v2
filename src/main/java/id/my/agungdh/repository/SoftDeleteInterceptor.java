package id.my.agungdh.repository;

import id.my.agungdh.config.SoftDelete;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.util.Set;

@SoftDelete
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class SoftDeleteInterceptor {

    @Inject
    Set<SoftDeletableRepository<?, ?>> repositories;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        repositories.forEach(SoftDeletableRepository::enableDeletedFilter);
        try {
            return ctx.proceed();
        } finally {
            repositories.forEach(SoftDeletableRepository::disableDeletedFilter);
        }
    }
}
