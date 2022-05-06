package com.demo.rampup.admin.controller;

import com.demo.rampup.admin.service.PersonService;
import com.demo.rampup.common.model.RestData;
import com.demo.rampup.data.entity.Person;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhouyw
 */
@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    PersonService personService;

    @GetMapping("/hello")
    public RestData<String> hello(@RequestParam("param") String param) {
        log.info("demo controller hello  {}", param);
        List<Person> list = personService.list();
        return RestData.success("hello world " + new Gson().toJson(list));
    }

}
