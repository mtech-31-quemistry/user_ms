package com.quemistry.user_ms.controller;

import com.quemistry.user_ms.controller.base.BaseController;
import com.quemistry.user_ms.model.ClassDto;
import com.quemistry.user_ms.model.SaveClassRequest;
import com.quemistry.user_ms.model.base.ResponseDto;
import com.quemistry.user_ms.service.ClassService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.quemistry.user_ms.constant.UserConstant.HEADER_KEY_USER_ID;

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
            @RequestHeader(value = HEADER_KEY_USER_ID) String userId,
            @Valid @RequestBody SaveClassRequest request) {

        String functionName = "saveClass";

        try {
            request.setUserId(userId);

            ResponseDto responseDto = prepareResponse(
                    controllerName,
                    functionName,
                    "The request has been completed.",
                    this.classService.saveClass(request)
            );

            return ResponseEntity.ok(responseDto);
        } catch (Exception ex) {
            return prepareException(controllerName, functionName, ex);
        }
    }

    @PutMapping
    public ResponseEntity<ResponseDto> updateClass(
            @RequestHeader(value = HEADER_KEY_USER_ID) String userId,
            @Valid @RequestBody ClassDto input) {

        String functionName = "updateClass";
        input.setUserId(userId);
        ClassDto classDto = this.classService.updateClass(input);

        ResponseDto responseDto = prepareResponse(
                controllerName,
                functionName,
                "The request has been completed.",
                classDto
        );
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllClasses(
            @RequestHeader(value = HEADER_KEY_USER_ID) String userId) {

        String functionName = "getAllClasses";
        ResponseDto responseDto = prepareResponse(
                controllerName,
                functionName,
                "The request has been completed.",
                this.classService.getAllClasses(userId)
        );
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{classId}")
    public ResponseEntity<ResponseDto> getClassAndInvitations(
            @RequestHeader(value = HEADER_KEY_USER_ID) String tutorId, @PathVariable Long classId) {
        String functionName = "getClassAndInvitations";

        ResponseDto responseDto = prepareResponse(
                controllerName,
                functionName,
                "The request has been completed.",
                this.classService.getClassWithInvitations(classId, tutorId));

        return ResponseEntity.ok(responseDto);

    }
}
