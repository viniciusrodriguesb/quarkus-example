package io.github.viniciusrodriguesb.dto.response;

import io.github.viniciusrodriguesb.model.Follower;
import lombok.Data;

@Data
public class SeguidorResponse {
    private Long id;
    private String nome;

    public SeguidorResponse(){}

    public SeguidorResponse(Follower follower){
        this(follower.getId(), follower.getUserId().getName());
    }

    public SeguidorResponse(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
