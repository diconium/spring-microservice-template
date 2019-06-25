package com.diconium.microservice.controller;


import com.diconium.microservice.controller.support.BaseController;
import com.diconium.microservice.logging.LogBuilder;
import com.diconium.microservice.model.dto.CustomDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/ext")
@Api(tags = {"/ext"})
@SuppressWarnings("unchecked")
public class ExternalController extends BaseController {

    @GetMapping("/dto")
    @ResponseBody
    @ApiOperation(value = "Get Custom DTO")
    public List<CustomDTO> getDtos(@ApiIgnore HttpSession session) {

        LogBuilder.logStart(ExternalController.class.getSimpleName(), session.getId(), "getDtos");

        return (List<CustomDTO>) runAuthenticated(() -> {

            List<CustomDTO> inner = new ArrayList<>();
            inner.add(new CustomDTO("foo1", "bar1"));
            inner.add(new CustomDTO("foo2", "bar2"));
            inner.add(new CustomDTO("foo3", "bar3"));

            LogBuilder.logEnd(ExternalController.class.getSimpleName(), session.getId(), "getDtos");
            return inner;
        });
    }
}
