package io.github.viniciusrodriguesb.dto.request.user;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private Integer age;
}
