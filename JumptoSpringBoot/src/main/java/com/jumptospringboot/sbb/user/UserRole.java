package com.jumptospringboot.sbb.user;

import lombok.Getter;

// 스프링 시큐리티는 인증뿐만 아니라 권한 부여도 함
// enum 자료형(열거 자료형)
// 값이 변경될 필요가 없으므로 @Getter만 사용
@Getter
public enum UserRole {
   ADMIN("ROLE_ADMIN"),
   USER("ROLE_USER");

   UserRole(String value) {
       this.value = value;
   }

   private String value;
}
