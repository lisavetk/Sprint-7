package praktikum.courier.loginCourier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCourierRequest {
    private String login;
    private String password;

}
