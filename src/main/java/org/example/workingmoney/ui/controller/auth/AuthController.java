package org.example.workingmoney.ui.controller.auth;

import org.example.workingmoney.service.auth.AuthService;
import org.example.workingmoney.ui.controller.common.Response;
import org.example.workingmoney.ui.dto.request.JoinRequestDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/join")
    public Response<Void> join(@RequestBody @Valid JoinRequestDto joinRequestDto) {
        authService.join(joinRequestDto.email(), joinRequestDto.nickname(), joinRequestDto.password());
        return Response.ok(null);
    }
}
