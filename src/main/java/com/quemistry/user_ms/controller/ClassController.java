package com.quemistry.user_ms.controller;

import com.quemistry.user_ms.controller.base.BaseController;
import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.model.base.ResponseDto;
import com.quemistry.user_ms.model.response.ClassResponseDto;
import com.quemistry.user_ms.service.ClassService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.quemistry.user_ms.constant.UserConstant.USER_ID_HEADER_KEY;

@Slf4j
@RestController
@RequestMapping("/v1/class")
public class ClassController extends BaseController {

    private final ClassService classService;
    private final String controllerName = "ClassController";

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> saveClass(
            @RequestHeader(value = USER_ID_HEADER_KEY) String userId,
            @Valid @RequestBody ClassDto input) {

        String functionName = "saveClass";

        try {
            input.setUserId(userId);

            ResponseDto responseDto = prepareResponse(
                    controllerName,
                    functionName,
                    "The request has been completed.",
                    this.classService.saveClass(input)
            );

            return ResponseEntity.ok(responseDto);
        } catch (Exception ex) {
            return prepareException(controllerName, functionName, ex);
        }
    }

    @PutMapping
    public ResponseEntity<ResponseDto> updateClass(
            @RequestHeader(value = USER_ID_HEADER_KEY) String userId,
            @Valid @RequestBody ClassDto input) {

        String functionName = "updateClass";

        try {
            input.setUserId(userId);

            ClassResponseDto classResponseDto = this.classService.updateClass(input);

            if (classResponseDto == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto());

            ResponseDto responseDto = prepareResponse(
                    controllerName,
                    functionName,
                    "The request has been completed.",
                    classResponseDto
            );

            return ResponseEntity.ok(responseDto);
        } catch (Exception ex) {
            return prepareException(controllerName, functionName, ex);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllClasses(
            @RequestHeader(value = USER_ID_HEADER_KEY) String userId) {

        String functionName = "getAllClasses";

        try {

            ResponseDto responseDto = prepareResponse(
                    controllerName,
                    functionName,
                    "The request has been completed.",
                    this.classService.getAllClasses()
            );

            return ResponseEntity.ok(responseDto);
        } catch (Exception ex) {
            return prepareException(controllerName, functionName, ex);
        }
    }
}
