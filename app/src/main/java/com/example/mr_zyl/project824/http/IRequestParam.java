package com.example.mr_zyl.project824.http;

/**
 * 请求参数封装
 * @param <T>
 */
public interface IRequestParam<T> {
	public void put(String key, Object value);

	public Object get(String key);
	
	public int size();

	public T getRequestParam();
	
	
}
