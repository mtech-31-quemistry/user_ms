package com.quemistry.user_ms.controller;

import ch.qos.logback.core.util.StringUtil;
import com.quemistry.user_ms.controller.base.BaseController;
import com.quemistry.user_ms.model.TutorDto;
import com.quemistry.user_ms.model.base.ResponseDto;
import com.quemistry.user_ms.service.TutorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.quemistry.user_ms.constant.UserConstant.HEADER_KEY_USER_EMAIL;
import static com.quemistry.user_ms.constant.UserConstant.HEADER_KEY_USER_ID;

@Slf4j
@RestController
@RequestMapping("/v1/users/tutor")
public class TutorController extends BaseController {

    private final TutorService tutorService;
    private final String controllerName = "TutorController";

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ResponseDto> getProfile(
            @NotBlank @RequestHeader(value = HEADER_KEY_USER_ID) String accountId,
            @NotBlank @RequestHeader(value = HEADER_KEY_USER_EMAIL) String email) {
            String functionName = "getTutorProfile";
            ResponseDto responseDto = prepareResponse(
                    controllerName,
                    functionName,
                    "Your profile has been retrieved.",
                    tutorService.getProfile(email));

            return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/profile")
    public ResponseEntity<ResponseDto> saveProfile(
            @NotBlank @RequestHeader(value = HEADER_KEY_USER_ID) String accountId,
            @NotBlank @RequestHeader(value = HEADER_KEY_USER_EMAIL) String email,
            @Valid @RequestBody TutorDto tutorDto) {

        String functionName = "saveTutorProfile";
        if (StringUtil.isNullOrEmpty(accountId))
            throw new IllegalArgumentException("account id is empty");
        tutorDto.setAccountId(accountId);
        tutorDto.setEmail(email);
        ResponseDto responseDto = prepareResponse(
                controllerName,
                functionName,
                "Your profile has been updated.",
                this.tutorService.saveProfile(tutorDto));
        return ResponseEntity.ok(responseDto);
    }
}
