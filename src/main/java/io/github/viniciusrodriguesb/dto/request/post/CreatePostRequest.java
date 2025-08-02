package io.github.viniciusrodriguesb.dto.request.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePostRequest {

    @NotBlank(message = "O conteúdo do post é obrigatório")
    private String message;
}
