package org.example.workingmoney.controller.health;

import org.example.workingmoney.controller.common.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public Response<String> health() {

        return Response.ok("health");
    }
}
