package com.demo.rampup.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.rampup.admin.service.PersonService;
import com.demo.rampup.data.entity.Person;
import com.demo.rampup.data.mapper.PersonMapper;
import org.springframework.stereotype.Service;

/**
 * @author zhouyw
 * @date 2022-05-06
 * @describe com.demo.rampup.admin.service.impl
 */
@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements PersonService {
}
