package espe.edu.tienda_ropa.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("t0", System.currentTimeMillis());
        System.out.println("preHandle -> " + request.getMethod() + " " + request.getRequestURI());
        LOG.info("Solicitud entrante: {} {}", request.getMethod(), request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long t0 = (Long) request.getAttribute("t0");
        long elapsed = (t0 == null ? 0 : System.currentTimeMillis() - t0);
        System.out.println("afterCompletion -> status = " + response.getStatus() + " tiempo = " + elapsed + " ms");
        LOG.info("Solicitud completada: {} {} -> {} (tiempo: {} ms)", request.getMethod(), request.getRequestURI(), response.getStatus(), elapsed);
    }
}
