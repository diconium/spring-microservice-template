package com.diconium.microservice.controller;

import com.diconium.microservice.controller.support.BaseController;
import com.diconium.microservice.logging.LogBuilder;
import com.diconium.microservice.model.dto.CustomDTO;
import com.diconium.microservice.service.CustomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api(tags = {"/api"})
@SuppressWarnings("unchecked")
public class Controller extends BaseController {

    private final CustomService customService;

    public Controller(CustomService customService) {
        this.customService = customService;
    }

    @GetMapping("/dto")
    @ResponseBody
    @ApiOperation(value = "Get Custom DTO")
    public List<CustomDTO> getDtos(HttpSession session) {

        LogBuilder.logStart(Controller.class.getSimpleName(), session.getId(), "getDtos");

        List<CustomDTO> result = (List<CustomDTO>) runAuthenticated(() -> customService.getDtos(session.getId()));

        LogBuilder.logEnd(Controller.class.getSimpleName(), session.getId(), "getDtos");
        return result;
    }

}
