package pl.kesco.myfarmer.model.dto.image;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDto {

    private Data data;
    private boolean success;
    private Integer status;
}
