package com.example.runity.DTO;

import com.example.runity.enums.RunningType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDTO {
    @NotNull
    // 6-15자 영어 숫자
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호는 영문+숫자+특수문자를 포함해 8~15자로 입력해주세요.")
    private String password;

    @NotNull
    @Size(min = 1,max = 10,message = "이름은 1~10자까지 입력 가능합니다.")
    private String name;

    @NotNull
    @Pattern(regexp = "^[가-힣a-zA-Z]{1,10}$",
            message = "닉네임은 한글 또는 영문으로 1~10자까지 입력 가능합니다.")
    private String nickName;

    @NotNull
    @DecimalMin(value = "50.0", message = "키는 최소 50cm 이상이어야 합니다.")
    private Double height;

    @NotNull
    @DecimalMin(value = "10.0", message = "체중은 최소 10kg 이상이어야 합니다.")
    private Double weight;

    @NotNull(message = "러닝 타입을 선택해주세요.")
    private RunningType runningType;
}
