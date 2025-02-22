package com.example.demo.member.dto;

import lombok.Getter;

@Getter
public class MemberUpdateRequestDto {
    private final String email; // ❌ final 필드 + 기본 생성자 없음

    public MemberUpdateRequestDto(String email) {
        this.email = email;
    }
}
