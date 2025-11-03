package com.back.global.security

import com.back.domain.member.member.service.MemberService
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomOAuth2UserService(
    private val memberService: MemberService
) : DefaultOAuth2UserService() {

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)

        val oauthUserId = oAuth2User.name
        val providerTypeCode = userRequest.clientRegistration.registrationId.uppercase()

        val attributes = oAuth2User.attributes
        val properties = attributes["properties"] as? Map<String, Any> ?: emptyMap()

        val nickname = properties["nickname"] as? String ?: ""
        val profileImgUrl = properties["profile_image"] as? String ?: ""
        val username = "${providerTypeCode}__${oauthUserId}"

        val member = memberService.modifyOrJoin(username, "", nickname, profileImgUrl)

        return SecurityUser(
            id = member.id,
            username = member.username,
            password = member.password,
            nickname = member.nickname,
            authorities = member.authorities
        )
    }
}