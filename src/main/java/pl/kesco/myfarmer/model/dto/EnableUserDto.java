package pl.kesco.myfarmer.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnableUserDto {

    @NotNull
    private boolean enabled;

}
