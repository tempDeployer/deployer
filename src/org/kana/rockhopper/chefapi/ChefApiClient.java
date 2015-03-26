package org.kana.rockhopper.chefapi;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

import org.kana.rockhopper.chefapi.method.Delete;
import org.kana.rockhopper.chefapi.method.Get;
import org.kana.rockhopper.chefapi.method.Post;
import org.kana.rockhopper.chefapi.method.Put;

public class ChefApiClient {
	private String endpoint;
	private String userId;
	private String pemPath;
	
	/**
	 * 
	 * @param userId user name correspond to the pem key
	 * @param pemPath path of the auth key
	 * @param endpoint chef api server address
	 */
	public ChefApiClient(String userId, String pemPath, String endpoint){
		this.userId = userId;
		this.pemPath = pemPath;
		this.endpoint = endpoint;
	}
	
	/**
	 * 
	 * @param path in the endpoint. e.g /clients
	 * @return
	 */
	public Get get(String path){
		Get get = new Get(new GetMethod(endpoint+path));
		get.setPemPath(pemPath);
		get.setUserId(userId);
		return get;
	}
	
	public Post post(String path){
		Post post = new Post(new PostMethod(endpoint+path));
		post.setPemPath(pemPath);
		post.setUserId(userId);
		return post;
	}
	
	public Delete delete(String path){
	    Delete del = new Delete(new DeleteMethod(endpoint+path));
	    del.setPemPath(pemPath);
	    del.setUserId(userId);
	    return del;
	}
	
	public Put put(String path) {
		Put put = new Put(new PutMethod(endpoint+path));
		put.setPemPath(pemPath);
		put.setUserId(userId);
	    return put;
	}
	
	public Header[] buildHeaders(){
	    
	    return null;
	}
	
}
