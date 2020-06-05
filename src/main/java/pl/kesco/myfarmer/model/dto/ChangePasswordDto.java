package pl.kesco.myfarmer.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {

    @NotEmpty(message = "Wprowadź nowe hasło")
    @Size(min = 4, message = "Hasło musi mieć minimum 4 znaki")
    private String newPassword;

    @NotBlank(message = "Powtórz hasło")
    private String repeatPassword;

}
