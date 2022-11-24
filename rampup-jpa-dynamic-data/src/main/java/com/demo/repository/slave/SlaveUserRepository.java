package com.demo.repository.slave;

import com.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 用于用户登陆信息的持久化操作
 *
 * @author 周晓菠
 * @data 2017年5月9日 下午1:50:54
 */
public interface SlaveUserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
	
}
