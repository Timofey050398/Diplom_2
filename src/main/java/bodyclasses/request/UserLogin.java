package bodyclasses.request;
import bodyclasses.Constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.startsWith;

public class UserLogin implements User {
    private String email;
    private String password;

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getName(){
        return null;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserLogin() {
        // Пустой конструктор
    }

    public UserLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserLogin(String email) {
        this.email = email;
    }

    @Step("Send correct POST /api/v1/user/login request")
    public static Response sendPostUserLogin(User user){
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(Constants.USER_LOGIN);
        return response;
    }
    @Step("Send incorrect POST /api/v1/user/login request")
    public static Response sendPostUserLogin(HashMap<String, Object> requestBody){
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(Constants.USER_LOGIN);
        return response;
    }

    @Step("Get Access Token")
    public static String getAccessToken(Response response){
        String accessToken = response.jsonPath().getString("accessToken");
        return accessToken;
    }
    @Step("Get Access Token")
    public static String getAccessToken(User user){
        Response response = sendPostUserLogin(user);
        String accessToken = response.jsonPath().getString("accessToken");
        return accessToken;
    }
    @Step("Get Refresh Token")
    public static String getRefreshToken(User user){
        Response response = sendPostUserLogin(user);
        String refreshToken = response.jsonPath().getString("refreshToken");
        return refreshToken;
    }
    @Step("Get Refresh Token")
    public static String getRefreshToken(Response response){
        String refreshToken = response.jsonPath().getString("refreshToken");
        return refreshToken;
    }
    @Step("Compare response with response when user try login with incorrect credentials")
    public static  void compareResponseWithIncorrectCredentials(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
    @Step("Verify success response body")
    public static  void verifySuccessResponseBodyIsCorrect(Response response, String expectedEmail, String expectedName){
        response.then()
                .body("accessToken", startsWith("Bearer ")).and()
                .body("refreshToken", instanceOf(String.class)).and()
                .body("refreshToken", notNullValue()).and()
                .body("success", equalTo(true)).and()
                .body("user.email", equalTo(expectedEmail)).and()
                .body("user.name", equalTo(expectedName)).and()
                .statusCode(200);
    }
}
