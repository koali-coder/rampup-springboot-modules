package com.demo.server;

import com.demo.entity.User;
import com.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Description AppListener
 * @Author zhouyw
 * @Date 2022/11/22 17:22
 **/
@Slf4j
@Component
public class AppListener implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    UserService userService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        User master = userService.findMasterById("1");
        log.info("findMasterById  --> {}", master.getUserName());

        User slave = userService.findSlaveById("1");
        log.info("findSlaveById  --> {}", slave.getUserName());

        User master2 = userService.findMasterById("2");
        log.info("findMasterById  --> {}", master2.getUserName());

        User slave2 = userService.findSlaveById("2");
        log.info("findSlaveById  --> {}", slave2.getUserName());
    }

}
