package dotsub.demo.endpoint.api;

public class ResponseDTO<T> {

	private String errorMessage;
	private T data;
	private boolean success;
	
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public T getData() {
		return data;
	}
	public void setData(final T data) {
		this.data = data;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(final boolean success) {
		this.success = success;
	}
	
	
}
