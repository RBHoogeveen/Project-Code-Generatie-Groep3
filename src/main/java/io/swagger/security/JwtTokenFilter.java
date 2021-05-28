package io.swagger.security;

import io.swagger.api.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

  @Autowired
  private JwtTokenProvider jwtTokenProvider;


  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

    String token = jwtTokenProvider.resolveToken(httpServletRequest);

    try {
      if (token != null && jwtTokenProvider.validateToken(token)){
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (ResponseStatusException e) {
      SecurityContextHolder.clearContext();
      httpServletResponse.sendError(e.getRawStatusCode(), e.getMessage());
      return;
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
