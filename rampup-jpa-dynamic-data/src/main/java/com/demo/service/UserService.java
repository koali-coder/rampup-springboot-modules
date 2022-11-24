package com.demo.service;

import com.demo.entity.User;
import com.demo.repository.master.MasterUserRepository;
import com.demo.repository.slave.SlaveUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description UserService
 * @Author zhouyw
 * @Date 2022/11/22 17:15
 **/
@Service
public class UserService {

    @Autowired
    private MasterUserRepository masterUserRepository;
    @Autowired
    private SlaveUserRepository slaveUserRepository;

    public User findMasterById(String id) {
        return masterUserRepository.findById(id).get();
    }

    public User findSlaveById(String id) {
        return slaveUserRepository.findById(id).get();
    }

}
