package model;

import java.io.Serializable;
import java.util.List;

public class Result<T> implements Serializable{

	private static final long serialVersionUID = 123123435435L;

	private boolean isConnected; //标识链接服务器是否成功
	
	private int  errorType=-1;
	
	private float version ; //版本信息
	
	private T  Data;
	
	public Result() {
		// TODO Auto-generated constructor stub
		isConnected=true;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public int getErrorType() {
		return errorType;
	}

	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}

	public float getVersion() {
		return version;
	}

	public void setVersion(float version) {
		this.version = version;
	}

	public T getData() {
		return Data;
	}

	public void setData( T data) {
		Data = data;
	}





	
}
