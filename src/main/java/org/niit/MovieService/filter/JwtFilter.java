package org.niit.MovieService.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public class JwtFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String header = request.getHeader("Authorization");
        System.out.println(header);
        if (header == null || !header.startsWith("Bearer")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            servletOutputStream.println("Token is Missing or Invalid ");
        } else {
            String token = header.substring(7);
            System.out.println(token);
            Claims claims = Jwts.parser().setSigningKey("MySecretKeyForUserAuthenticationServiceOfMovieApp").build().parseClaimsJws(token).getBody();
            System.out.println("Filtered Claims:" + claims);
            request.setAttribute("claims", claims);
            filterChain.doFilter(request, response);

        }

    }
}
