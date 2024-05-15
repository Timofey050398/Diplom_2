package bodyclasses.request;
import bodyclasses.Constants;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserUpdate implements User{
    private String email;
    private String password;
    private String name;
    @Override
    public String getEmail() {
        return email;
    }
    @Step("Изменить почту")
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Step("Изменить пароль")
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getName() {
        return name;
    }
    @Step("Изменить имя")
    public void setName(String name) {
        this.name = name;
    }

    public UserUpdate() {
        Faker faker = new Faker();
        this.email = faker.internet().emailAddress();
        this.password = faker.internet().password();
        this.name = faker.name().name();
    }

    public UserUpdate(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserUpdate(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Step("Send PATCH /api/auth/user request with custom access token")
    public static Response sendPatchUserUpdate(String AccessToken, User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", AccessToken)
                .and()
                .body(user)
                .when()
                .patch(Constants.USER_UPDATE);
        return response;
    }
    @Step("Send unauthorized PATCH /api/auth/user request")
    public static Response sendPatchUserUpdateUnauthorized(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(Constants.USER_UPDATE);
        return response;
    }
    @Step("Compare the answer with answer when a user tries to change email to existed")
    public static  void compareResponseWithExistedEmail(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("User with such email already exists"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(403);
    }
    @Step("Verify success response body")
    public static  void verifySuccessResponseBodyIsCorrect(Response response, String expectedEmail, String expectedName){
        response.then()
                .body("success", equalTo(true)).and()
                .body("user.email", equalTo(expectedEmail)).and()
                .body("user.name", equalTo(expectedName)).and()
                .statusCode(200);
    }
}
