package com.ezenac.thunder_market.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    @Pattern(regexp = "^[a-z0-9][a-z0-9_\\-]{4,19}$", message = "not match MEMBER_ID regex")
    @NotBlank(message = "memberId NotBlank Exception")
    private String memberId;

    @Pattern(regexp = "[^\\s](?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{7,17}$", message = "not match PASSWORD regex")
    @NotBlank(message = "password NotBlank Exception")
    private String password;

    @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "not match MEMBER_NAME regex")
    @NotBlank
    private String name;

    @Nullable
    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$|", message = "not match MEMBER_EMAIL regex")
    private String email;

    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "not match MEMBER_PHONE_NUM regex")
    @NotBlank
    private String phoneNumber;
}
