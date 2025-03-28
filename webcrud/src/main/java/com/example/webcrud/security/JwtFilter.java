    package com.example.webcrud.security;

    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.Cookie;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;

    import java.io.IOException;

    @Component
    public class JwtFilter extends OncePerRequestFilter {
        private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
        
        private final JwtTokenProvider jwtTokenProvider;
        private final UserDetailsService userDetailsService;

        public JwtFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
            this.jwtTokenProvider = jwtTokenProvider;
            this.userDetailsService = userDetailsService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                throws ServletException, IOException {
            String token = getTokenFromRequest(request);
            
            // Debug
            logger.info("Request URL: " + request.getRequestURI());
            logger.info("JWT Token found: " + (token != null));
        
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    String username = jwtTokenProvider.extractUsername(token);
                    // Debug
                    logger.debug("Username from token: {}", username);
                    
                    // Initialize variable
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
                    if (jwtTokenProvider.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        
                        logger.debug("Authentication set for user: {}", username);
                    }
                } catch (Exception e) {
                    logger.error("Error processing JWT token", e);
                }
            } else {
                logger.info("No token or authentication already set");
            }

            chain.doFilter(request, response);
        }    

        private String getTokenFromRequest(HttpServletRequest request) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("jwt_token".equals(cookie.getName())) {
                        System.out.println("Token found in cookie: " + cookie.getValue());
                        return cookie.getValue();
                    }
                }
            }
            
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
            
            return null;
        }
    }