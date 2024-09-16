package com.example.web_ide.log;

/* 로깅 시스템을 위한 필터, http request,response의 로그를 남긴다.
필터는 request/response가 dispatcher servlet의 앞단/뒷단에서 trigger된다.
client에 대한 응답 직전에 request, response 객체를 받아 로그를 남길 수 있다.
따라서 사용자의 http 요청에 대한 로그를 남기는데 aop, interceptor 대신 filter를 사용했다.
filter 중 genericfilterbean, onceperreqeustfilter가 가장 자주 사용된다.
genericfilterbean은 spring 설정 정보를 가져올 수 있도록 spring bean으로 등록되어 스프링 주기와 함께 관리된다.
하지만 spring security 등에서 dispatch servlet이 다른 servlet으로 요청을 전달할 때 filter가 여러번 호출되는 문제가 있다.
따라서 onceperrequestfilter를 사용해 각 사용자 요청에 대해 한 번만 로그를 남기도록 한다.*/

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
public class LogFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        filterChain.doFilter(requestWrapper, responseWrapper);

        String method=requestWrapper.getMethod();
        String url=requestWrapper.getRequestURL().toString();
        String queryString=requestWrapper.getQueryString();
        String requestBody=new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

        int status=responseWrapper.getStatus();
        String responseBody=new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        log.info("request : {} {} {} {}", method,url,queryString,requestBody);
        log.info("response : {} {}", status,responseBody);
        responseWrapper.copyBodyToResponse();
        MDC.clear();
    }
}
