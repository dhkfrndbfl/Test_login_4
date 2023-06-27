package Test.Testlogin.Member.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    Admin("ROLE_ADMIN"),
    User("ROLE_User");

    private String value;
}
