package pl.kesco.myfarmer.model.dto;

import lombok.*;
import pl.kesco.myfarmer.model.entity.Role;

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
    private boolean enabled;

}
