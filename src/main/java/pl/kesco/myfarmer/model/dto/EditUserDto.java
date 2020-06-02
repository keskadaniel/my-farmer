package pl.kesco.myfarmer.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUserDto {

    @NotBlank
    private String name;

    @NotNull
    private String role;

}
