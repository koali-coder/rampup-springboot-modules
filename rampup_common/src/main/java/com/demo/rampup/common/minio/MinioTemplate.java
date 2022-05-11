package com.demo.rampup.common.minio;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhouyw
 * @date 2022-05-11
 * @describe com.demo.rampup.common.minio
 */
@Component
public class MinioTemplate {

	@Autowired
	private MinioClient minioClient;

	/**
	 * description: 判断bucket是否存在，不存在则创建
	 *
	 * @return: void
	 */
	public void existBucket(String name) {
		try {
			boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
			if (!exists) {
				minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建存储bucket
	 * @param bucketName 存储bucket名称
	 * @return Boolean
	 */
	public Boolean makeBucket(String bucketName) {
		try {
			minioClient.makeBucket(MakeBucketArgs.builder()
					.bucket(bucketName)
					.build());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 删除存储bucket
	 * @param bucketName 存储bucket名称
	 * @return Boolean
	 */
	public Boolean removeBucket(String bucketName) {
		try {
			minioClient.removeBucket(RemoveBucketArgs.builder()
					.bucket(bucketName)
					.build());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * description: 上传文件
	 *
	 * @param ins
	 * @return: java.lang.String
	 */
	public List<String> upload(String bucketName, String fileName, InputStream[] ins) {
		List<String> names = new ArrayList<>(ins.length);
		for (InputStream in : ins) {
			upload(bucketName, fileName, in);
			names.add(fileName);
		}
		return names;
	}

	/**
	 * description: 上传文件
	 * @param bucketName
	 * @param fileName
	 * @param in
	 * @return
	 */
	public String upload(String bucketName, String fileName, InputStream in) {
		try {
			minioClient.putObject(PutObjectArgs.builder()
					.bucket(bucketName)
					.object(fileName)
					.stream(in, in.available(), -1)
					.contentType("application/octet-stream")
					.build()
			);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileName;
	}

	/**
	 * description: 下载文件
	 *
	 * @param fileName
	 * @return: org.springframework.http.ResponseEntity<byte [ ]>
	 */
	public ResponseEntity<byte[]> download(String bucketName, String fileName) {
		ResponseEntity<byte[]> responseEntity = null;
		InputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
			out = new ByteArrayOutputStream();
			IOUtils.copy(in, out);
			//封装返回值
			byte[] bytes = out.toByteArray();
			HttpHeaders headers = new HttpHeaders();
			try {
				headers.add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			headers.setContentLength(bytes.length);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setAccessControlExposeHeaders(Arrays.asList("*"));
			responseEntity = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseEntity;
	}

	/**
	 * 查看文件对象
	 * @param bucketName 存储bucket名称
	 * @return 存储bucket内文件对象信息
	 */
	public List<MinioItem> listObjects(String bucketName) {
		Iterable<Result<Item>> results = minioClient.listObjects(
				ListObjectsArgs.builder().bucket(bucketName).build());
		List<MinioItem> objectItems = new ArrayList<>();
		try {
			for (Result<Item> result : results) {
				Item item = result.get();
				MinioItem objectItem = new MinioItem();
				objectItem.setObjectName(item.objectName());
				objectItem.setSize(item.size());
				objectItems.add(objectItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return objectItems;
	}

	/**
	 * 批量删除文件对象
	 * @param bucketName 存储bucket名称
	 */
	public void removeObjects(String bucketName, String objectName) {
		RemoveObjectArgs objectArgs = RemoveObjectArgs.builder().object(objectName)
				.bucket(bucketName).build();
		try {
			minioClient.removeObject(objectArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     * 获取文件流
	 * @param bucketName
     * @param fileName
     * @return
     */
	public InputStream getObject(String bucketName, String fileName) {
		InputStream in = null;
		try {
			in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}

}
