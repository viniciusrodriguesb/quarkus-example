package io.github.viniciusrodriguesb.dto.request.user;

import jakarta.ws.rs.QueryParam;
import lombok.Data;

@Data
public class GetUserRequest {
    @QueryParam("name")
    private String name;

    @QueryParam("age")
    private Integer age;
}
