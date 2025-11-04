package com.back.global.security

import com.back.domain.member.member.entity.Member
import com.back.domain.member.member.service.MemberService
import com.back.global.exception.ServiceException
import com.back.global.rq.Rq
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CustomAuthenticationFilter(
    private val memberService: MemberService,
    private val rq: Rq
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        logger.debug("CustomAuthenticationFilter called")

        try {
            authenticate(request, response, filterChain)
        } catch (e: ServiceException) {
            val rsData = e.rsData
            response.apply {
                contentType = "application/json"
                status = rsData.statusCode
                writer.write("""
                    {
                        "resultCode": "${rsData.resultCode}",
                        "msg": "${rsData.msg}"
                    }
                """.trimIndent())
            }
        }
    }

    private fun authenticate(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (!request.requestURI.startsWith("/api/")) {
            filterChain.doFilter(request, response)
            return
        }

        if (request.requestURI in EXCLUDED_URIS) {
            filterChain.doFilter(request, response)
            return
        }

        val (apiKey, accessToken) = extractCredentials()

        if (apiKey.isBlank() && accessToken.isBlank()) {
            filterChain.doFilter(request, response)
            return
        }

        val (member, isAccessTokenValid) = resolveMember(apiKey, accessToken)

        if (accessToken.isNotBlank() && !isAccessTokenValid) {
            val newAccessToken = memberService.genAccessToken(member)
            rq.setCookie("accessToken", newAccessToken)
            rq.setHeader("accessToken", newAccessToken)
        }

        val user = SecurityUser(
            id = member.id,
            username = member.username,
            password = "",
            nickname = member.nickname,
            authorities = member.authorities
        )

        val authentication = UsernamePasswordAuthenticationToken(
            user,
            user.password,
            user.authorities
        )

        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }

    private fun extractCredentials(): Pair<String, String> {
        val headerAuthorization = rq.getHeader("Authorization", "")

        return if (headerAuthorization.isNotBlank()) {
            require(headerAuthorization.startsWith("Bearer ")) {
                throw ServiceException("401-2", "Authorization 헤더가 Bearer 형식이 아닙니다.")
            }

            val bits = headerAuthorization.split(" ", limit = 3)
            val apiKey = bits[1]
            val accessToken = bits.getOrElse(2) { "" }

            apiKey to accessToken
        } else {
            val apiKey = rq.getCookieValue("apiKey", "")
            val accessToken = rq.getCookieValue("accessToken", "")

            apiKey to accessToken
        }
    }

    private fun resolveMember(apiKey: String, accessToken: String): Pair<Member, Boolean> {
        var member: Member? = null
        var isAccessTokenValid = false

        if (accessToken.isNotBlank()) {
            memberService.payloadOrNull(accessToken)?.let { payload ->
                val id = payload["id"] as Long
                val username = payload["username"] as String
                val nickname = payload["nickname"] as String

                member = Member(id, username, nickname)
                isAccessTokenValid = true
            }
        }

        val resolvedMember = member ?: memberService.findByApiKey(apiKey)
            .orElseThrow { ServiceException("401-3", "API 키가 유효하지 않습니다.") }

        return resolvedMember to isAccessTokenValid
    }

    companion object {
        private val EXCLUDED_URIS = setOf(
            "/api/v1/members/join",
            "/api/v1/members/login"
        )
    }
}