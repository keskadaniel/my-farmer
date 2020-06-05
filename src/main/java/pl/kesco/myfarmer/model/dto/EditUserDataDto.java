package pl.kesco.myfarmer.model.dto;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EditUserDataDto {


    @NotEmpty
    private String name;

    @Email(message = "Podaj prawidłowy adres email")
    private String email;

    @Column(name = "phone_number")
    @Size(min = 9, max = 14, message = "Podaj prawidłowy numer telefonu")
    private String phoneNumber;


}
