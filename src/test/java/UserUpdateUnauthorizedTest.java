import bodyclasses.Constants;
import bodyclasses.request.*;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
@DisplayName("Тесты попытки изменения данных пользователя без авторизации PATCH /api/auth/user")
public class UserUpdateUnauthorizedTest {

    @Parameters
    public static Collection<Object[]> data() {
        Faker faker = new Faker();
        return Arrays.asList(new Object[][] {
                { "email", faker.internet().emailAddress()},
                { "name", faker.name().firstName()},
                { "password", faker.internet().password()}
        });
    }

    private final String fieldToUpdate;
    private final String newValue;

    public UserUpdateUnauthorizedTest(String fieldToUpdate, String newValue) {
        this.fieldToUpdate = fieldToUpdate;
        this.newValue = newValue;
    }

    @Test
    @DisplayName("Проверка изменения данных пользователя без авторизации")
    public void UnauthorizedUpdate() {

        RestAssured.baseURI = Constants.BASE_URL;
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().firstName();
        UserUpdate user = new UserUpdate(email, password, name);
        UserCreate.sendPostUserCreate(user);

        switch (fieldToUpdate) {
            case "email":
                user.setEmail(newValue);
                break;
            case "name":
                user.setName(newValue);
                break;
            case "password":
                user.setPassword(newValue);
                break;
            default:
                throw new IllegalArgumentException("Invalid field to update: " + fieldToUpdate);
        }
        Response response = UserUpdate.sendPatchUserUpdateUnauthorized(user);
        CommonResponses.compareResponseWithoutCorrectAccessToken(response);
        user.setEmail(email);
        user.setPassword(password);
        UserDelete.sendDeleteUser(user);
    }
}
