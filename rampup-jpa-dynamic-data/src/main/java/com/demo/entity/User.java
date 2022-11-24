package com.demo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 
 * @author: xiaochuan.xiong
 * @date: 2022年10月19日
 * @notes:系统用户
 */
@Data
@Entity
@Table(name = "system_user")
public class User {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 6894259950774275269L;

	@Id
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid2")
	@Column(name = "id", unique = true, nullable = false, length = 36)
	private String id;

	@Column(name = "user_name", length = 20, nullable = false)
	private String userName;
	
	@Column(name = "login_id", length = 20, nullable = false)
	private String loginId;
	
}
