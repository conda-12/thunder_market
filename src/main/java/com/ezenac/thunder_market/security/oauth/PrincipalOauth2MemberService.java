package com.ezenac.thunder_market.security.oauth;

import com.ezenac.thunder_market.security.Role;
import com.ezenac.thunder_market.security.RoleRepository;
import com.ezenac.thunder_market.security.auth.PrincipalDetails;
import com.ezenac.thunder_market.security.oauth.provider.*;
import com.ezenac.thunder_market.member.entity.Member;
import com.ezenac.thunder_market.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PrincipalOauth2MemberService extends DefaultOAuth2UserService {

    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

        @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Attribute를 파싱해서 공통 객체로 묶는다. 관리가 편함.
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FaceBookUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttribute("response"));
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            System.out.println("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            System.out.println("지원 하지 않는 소셜입니다.");
        }


        assert oAuth2UserInfo != null;
        String provider = oAuth2UserInfo.getProvider(); // ex. Google
        String providerId = oAuth2UserInfo.getProviderId(); // ex. Google PrimaryKey
        String memberId = provider + "_" + providerId;
        String email = oAuth2UserInfo.getEmail();
        Role role = roleRepository.findByRoleName("ROLE_MEMBER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        Optional<Member> member = memberRepository.findById(memberId);
        Member setMember = null;
        System.out.println("providerID= " + providerId);
        System.out.println(memberId);

        if (member.isEmpty()) {
            System.out.println("처음 만드는 회원가입한 멤버 입니다.");
            setMember = Member.builder()
                    .memberId(memberId)
                    .password(bCryptPasswordEncoder.encode("@SOCIAL_PASSWORD"))
                    .email(email)
                    .memberRoles(roles)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            memberRepository.save(setMember);
            return new PrincipalDetails(setMember, oAuth2User.getAttributes());
        } else {
            System.out.println("이미 회원가입한 멤버입니다.");
            return new PrincipalDetails(member.get(), oAuth2User.getAttributes());
        }
    }
}
