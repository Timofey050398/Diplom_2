import bodyclasses.Constants;
import bodyclasses.request.*;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

@DisplayName("Тесты метода авторизации пользователя POST /api/auth/login")
public class UserLoginTest {
    Faker faker = new Faker();
    String email = faker.internet().emailAddress();
    String password = faker.internet().password();
    String name = faker.name().firstName();
    User user = new UserCreate(email,password,name);

    HashMap<String, Object> requestBody = new HashMap<>();
    @Before
    @DisplayName("Предусловие: создание пользователя")
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
        Response response = UserCreate.sendPostUserCreate(user);
        CommonResponses.compareWithCorrectResponse(response);
    }
    @After
    @DisplayName("Постусловие: Удаление пользователя")
    public void deleteUser(){
        Response response = UserDelete.sendDeleteUser(user);
        UserDelete.userSuccessfullyDeleted(response);
    }
    @Test
    @DisplayName("Успешный логин")
    public void correctLoginResponse(){
        Response response = UserLogin.sendPostUserLogin(user);
        UserLogin.verifySuccessResponseBodyIsCorrect(response,email,name);
    }
    @Test
    @DisplayName("Логин с незарегестрированным email")
    public void loginWithWrongEmail(){
        String wrongEmail = faker.internet().emailAddress();
        requestBody.put("email",wrongEmail);
        requestBody.put("password",password);
        Response response = UserLogin.sendPostUserLogin(requestBody);
        UserLogin.compareResponseWithIncorrectCredentials(response);
    }
    @Test
    @DisplayName("Логин с неверным password")
    public void loginWithWrongPassword(){
        String wrongPassword = faker.internet().password();
        requestBody.put("email",email);
        requestBody.put("password",wrongPassword);
        Response response = UserLogin.sendPostUserLogin(requestBody);
        UserLogin.compareResponseWithIncorrectCredentials(response);
    }
    @Test
    @DisplayName("Логин с пустым password")
    public void loginWithEmptyPassword(){
        requestBody.put("email",email);
        requestBody.put("password","");
        Response response = UserLogin.sendPostUserLogin(requestBody);
        UserLogin.compareResponseWithIncorrectCredentials(response);
    }
}
