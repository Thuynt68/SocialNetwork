package com.example.SocialNetwork.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@NoArgsConstructor
@Getter

public enum ErrorCode {
	USER_EXISTED(409, "Tên đăng nhập đã được sử dụng", HttpStatus.CONFLICT),
	EMAIL_USED(409, "Email đã được sử dụng", HttpStatus.CONFLICT),
	WRONG_CURRENT_PASSWORD(404, "Mật khẩu hiện tại không chính xác", HttpStatus.CONFLICT),
	USER_NOT_EXIST(400, "User không tồn tại", HttpStatus.BAD_REQUEST),
	UNKNOWN_EXCEPTION(999, "uncategorized exception", HttpStatus.BAD_REQUEST),
	INVALID_PASSWORD(1003, "Mật khẩu có ít nhất 8 ký tự", HttpStatus.BAD_REQUEST),
	UNAUTHENTICATED(913, "Sai thông tin tài khoản", HttpStatus.NOT_ACCEPTABLE);

	private int code;
	private String message;
	private HttpStatusCode statusCode;
	
	
}
