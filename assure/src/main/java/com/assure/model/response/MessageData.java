package com.assure.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class MessageData {

	private HttpStatus code;
	private String message;
}
