package com.spring.auth.auth_app.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@Data
public class CookieService {
    @Value("${security.jwt.refresh_token_cookie_name}")
    private String cookieName;
    @Value("${security.jwt.cookie_secure}")
    private boolean cookieSecure;
    @Value("${security.jwt.cookie_http_only}")
    private boolean cookieHttpOnly;
    @Value("${security.jwt.cookie_same_site}")
    private String cookieSameSite;
    @Value("${security.jwt.cookie_domain_name}")
    private String cookieDomain;

    public void attachRefreshTokenCookie(HttpServletResponse response,String value, int age){
     // we can use new called var too
      ResponseCookie.ResponseCookieBuilder cookie =  ResponseCookie.from(cookieName,value)
              .path("/")
              .httpOnly(cookieHttpOnly)
              .secure(cookieSecure)
              .maxAge(age)
              .sameSite(cookieSameSite);
              if(cookieDomain!=null && !cookieDomain.isBlank()){
                  cookie.domain(cookieDomain);
              }
              ResponseCookie responseCookie = cookie.build();
              response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }
    public void clearRefreshCookie(HttpServletResponse response){
        var cookie = ResponseCookie.from(cookieName,"") .path("/")
                .httpOnly(cookieHttpOnly)
                .secure(cookieSecure).
                  maxAge(0)
                . sameSite(cookieSameSite);
        if(cookieDomain!=null && !cookieDomain.isBlank()){
            cookie.domain(cookieDomain);
        }
        ResponseCookie responseCookie = cookie.build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }
    public void addNoHeader(HttpServletResponse response){
        response.setHeader(HttpHeaders.CACHE_CONTROL,"no-store");
        response.setHeader("pragma","no-cache");
    }

}
