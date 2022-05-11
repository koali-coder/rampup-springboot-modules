package com.demo.rampup.common.minio;

import io.minio.messages.Item;
import io.minio.messages.Owner;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * @author zhouyw
 * @date 2022-05-11
 * @describe com.demo.rampup.common.minio
 */
@Data
public class MinioItem {

	private String objectName;
	private ZonedDateTime lastModified;
	private String etag;
	private Long size;
	private String storageClass;
	private Owner owner;
	private String type;

	public MinioItem() {
	}

	public MinioItem(Item item) {
		this.objectName = item.objectName();
		this.lastModified = item.lastModified();
		this.etag = item.etag();
		this.size = (long) item.size();
		this.storageClass = item.storageClass();
		this.owner = item.owner();
		this.type = item.isDir() ? "directory" : "file";
	}

}
