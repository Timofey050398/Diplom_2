package bodyclasses.request;

import bodyclasses.Constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserDelete {

    @Step("Send correct DELETE /api/auth/user request")
    public static Response sendDeleteUser(User user){
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", UserLogin.getAccessToken(user))
                .delete(Constants.USER_UPDATE);
                return response;
    }
    @Step("Проверка, что пользователь успешно удалён")
    public static void userSuccessfullyDeleted(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("User successfully removed"))
                .and()
                .body("success", equalTo(true))
                .and()
                .statusCode(202);
    }
}
