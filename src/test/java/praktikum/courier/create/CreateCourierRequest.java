package praktikum.courier.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourierRequest {
    private String login;
    private String password;
    private String firstName;
}
