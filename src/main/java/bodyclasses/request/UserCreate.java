package bodyclasses.request;

import bodyclasses.Constants;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserCreate implements User {
    private  String email;
    private  String password;
    private String name;
    @Override
    public  String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public  String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public UserCreate() {
        Faker faker = new Faker();
        this.email = faker.internet().emailAddress();
        this.password = faker.internet().password();
        this.name = faker.name().name();
    }

    public UserCreate(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserCreate(String email, String password) {
        this.email = email;
        this.password = password;
    }
    @Step("Send correct POST /api/auth/register request")
    public static Response sendPostUserCreate(User user){
        Response response =given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(Constants.USER_CREATE);
        return response;
    }
    @Step("Send incorrect POST /api/auth/register request")
    public static Response sendPostUserCreate(HashMap<String, Object> requestBody){
        Response response =given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(Constants.USER_CREATE);
        return response;
    }
    @Step("Compare response with correct response without required fields")
    public static  void compareResponseWithoutRequiredFields(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(403);
    }
    @Step("Checks if the response is correct for a request with a email already used")
    public static void compareResponseWithEmailAlreadyUsed(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("User already exists"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(403);
    }
}
