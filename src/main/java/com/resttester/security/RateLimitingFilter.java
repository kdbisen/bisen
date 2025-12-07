/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple rate limiting filter to prevent abuse
 */
@Component
@Order(1)
public class RateLimitingFilter implements Filter {

    // Rate limit: 100 requests per minute per IP
    private static final int MAX_REQUESTS_PER_MINUTE = 100;
    private static final long TIME_WINDOW_MS = 60 * 1000; // 1 minute

    private final ConcurrentHashMap<String, RequestCounter> requestCounters = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Only rate limit API endpoints
        String path = httpRequest.getRequestURI();
        if (path.startsWith("/api/")) {
            String clientIp = getClientIp(httpRequest);
            RequestCounter counter = requestCounters.computeIfAbsent(clientIp, k -> new RequestCounter());

            synchronized (counter) {
                long now = System.currentTimeMillis();
                
                // Reset counter if time window has passed
                if (now - counter.getLastReset() > TIME_WINDOW_MS) {
                    counter.reset(now);
                }

                // Check rate limit
                if (counter.getCount() >= MAX_REQUESTS_PER_MINUTE) {
                    httpResponse.setStatus(429); // 429 Too Many Requests
                    httpResponse.setContentType("application/json");
                    httpResponse.getWriter().write("{\"error\":\"Rate limit exceeded. Maximum " + 
                        MAX_REQUESTS_PER_MINUTE + " requests per minute allowed.\"}");
                    return;
                }

                counter.increment();
            }
        }

        chain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // Handle multiple IPs in X-Forwarded-For
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip != null ? ip : "unknown";
    }

    private static class RequestCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private long lastReset = System.currentTimeMillis();

        public void increment() {
            count.incrementAndGet();
        }

        public int getCount() {
            return count.get();
        }

        public void reset(long now) {
            count.set(0);
            lastReset = now;
        }

        public long getLastReset() {
            return lastReset;
        }
    }
}

