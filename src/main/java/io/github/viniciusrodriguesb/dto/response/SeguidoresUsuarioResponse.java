package io.github.viniciusrodriguesb.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class SeguidoresUsuarioResponse {
    private Integer qntSeguidores;
    private List<SeguidorResponse> seguidores;
}
