package id.my.agungdh.repository;

import id.my.agungdh.config.SoftDelete;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@SoftDelete
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class SoftDeleteInterceptor {

    @Inject
    PegawaiRepository pegawaiRepository;

    @Inject
    UserRepository userRepository;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        pegawaiRepository.enableDeletedFilter();
        userRepository.enableDeletedFilter();
        try {
            return ctx.proceed();
        } finally {
            pegawaiRepository.disableDeletedFilter();
            userRepository.disableDeletedFilter();
        }
    }
}
