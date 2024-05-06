import bodyclasses.Constants;
import bodyclasses.request.CommonResponses;
import bodyclasses.request.User;
import bodyclasses.request.UserCreate;
import bodyclasses.request.UserDelete;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;



@DisplayName("Тесты метода создания пользователя POST /api/auth/register")
public class UserCreateTest {
    Faker faker = new Faker();
    String email = faker.internet().emailAddress();
    String password = faker.internet().password();
    String name = faker.name().firstName();

    HashMap<String, Object> requestBody = new HashMap<>();
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    public void correctCreateResponseMessage() {
        User user = new UserCreate(email,password,name);
        Response response = UserCreate.sendPostUserCreate(user);
        CommonResponses.compareWithCorrectResponse(response);
        UserDelete.sendDeleteUser(user);
    }
    @Test
    @DisplayName("Eсли попробовать создать пользователя с занятым email, возвращается ошибка")
    public void cantCreateTwoUsersWithEqualEmails() {
        User user = new UserCreate(email,password,name);
        String secondName = faker.internet().emailAddress();
        String secondPassword = faker.internet().password();
        UserCreate secondUser = new UserCreate(user.getEmail(),secondPassword,secondName);
        UserCreate.sendPostUserCreate(user);
        Response response = UserCreate.sendPostUserCreate(secondUser);
        UserCreate.compareResponseWithEmailAlreadyUsed(response);
        UserDelete.sendDeleteUser(user);
    }
    @Test
    @DisplayName("Код ответа 403, если в запросе не передан параметр email")
    public void cantCreateUserWithoutEmail() {
        requestBody.put("password",password);
        requestBody.put("firstName",name);
        Response response = UserCreate.sendPostUserCreate(requestBody);
        UserCreate.compareResponseWithoutRequiredFields(response);
    }
    @Test
    @DisplayName("Код ответа 403, если в запросе не передан параметр password")
    public void cantCreateUserWithoutPassword() {
        requestBody.put("email",email);
        requestBody.put("firstName",name);
        Response response = UserCreate.sendPostUserCreate(requestBody);
        UserCreate.compareResponseWithoutRequiredFields(response);
    }
    @Test
    @DisplayName("Код ответа 403, если в запросе не передан параметр firstName")
    public void cantCreateUserWithoutFirstName() {
        requestBody.put("email",email);
        requestBody.put("password",password);
        Response response = UserCreate.sendPostUserCreate(requestBody);
        UserCreate.compareResponseWithoutRequiredFields(response);
    }
    @Test
    @DisplayName("Код ответа 403, если в запросе не передан ключ email")
    public void cantCreateUserWithoutEmailKey() {
        User user = new UserCreate("",password,name);
        Response response = UserCreate.sendPostUserCreate(user);
        UserCreate.compareResponseWithoutRequiredFields(response);
    }
    @Test
    @DisplayName("Код ответа 403, если в запросе не передан ключ password")
    public void cantCreateUserWithoutPasswordKey() {
        User user = new UserCreate(email,"",name);
        Response response = UserCreate.sendPostUserCreate(user);
        UserCreate.compareResponseWithoutRequiredFields(response);
    }
    @Test
    @DisplayName("Код ответа 403, если в запросе не передан ключ name")
    public void cantCreateUserWithoutFirstNameKey() {
        User user = new UserCreate(email,password,"");
        Response response = UserCreate.sendPostUserCreate(user);
        UserCreate.compareResponseWithoutRequiredFields(response);
    }
}

