package com.example.asset.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;

public class HttpServletRequestWrapperWithPrincipal extends HttpServletRequestWrapper {
    private final Principal principal;
    public HttpServletRequestWrapperWithPrincipal(HttpServletRequest request, Principal p) {
        super(request);
        this.principal = p;
    }
    @Override
    public Principal getUserPrincipal() {
        return principal;
    }
}
