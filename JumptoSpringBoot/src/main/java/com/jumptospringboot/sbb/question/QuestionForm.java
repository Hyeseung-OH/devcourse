package com.jumptospringboot.sbb.question;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// 입력받은 값을 검증하는 데 필요한 폼 클래스
// 폼 클래스는 입력 값을 검증할 때만 사용하는 게 아니라, 입력 항목을 바인딩할 때도 사용함
// 바인딩 = 템플릿의 항목과 form 클래스의 속성이 매핑되는 과정
@Getter
@Setter
public class QuestionForm {
    // @NotEmpty: 해당 값이 Null 또는 빈 문자열("")을 허용하지 않음을 의미
    @NotEmpty(message="제목은 필수 항목입니다.")
    // @Size(max = 200): 최대 길이가 200 바이트를 넘으면 안 된다는 의미
    @Size(max = 200)
    private String subject;

    @NotEmpty(message = "내용은 필수 항목입니다.")
    private String content;
}
