package com.newyork.sharespace.config

import com.newyork.sharespace.services.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {
    private val BYPASS_URLS = arrayOf(
        "/api/auth/**",

        "/v3/api-docs",
        "/v3/api-docs.yaml",
        "/v3/api-docs/*",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/swagger/**",
        "/swagger-resources/**",
        "/webjars/**"
    )
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return BYPASS_URLS.any { path ->
            request.requestURI.startsWith(path.removeSuffix("**"))
        }
    }
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            if (jwtService.validateAccessToken(authHeader)) {
                val userId = jwtService.getUserIdFromToken(authHeader)
                val auth = UsernamePasswordAuthenticationToken(userId, null, emptyList())
                SecurityContextHolder.getContext().authentication = auth
            }
        }

        filterChain.doFilter(request, response)
    }
}