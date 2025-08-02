package io.github.viniciusrodriguesb.dto.request.follower;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FollowerRequest {

    @NotNull(message = "O ID do usuario a ser seguido é obrigatório")
    private Long followerId;
}
