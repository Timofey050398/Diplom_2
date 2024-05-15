package bodyclasses.request;

import bodyclasses.Constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.startsWith;

public class UserGetOrders {
    @Step("Send correct GET /api/orders request")
    public static Response sendGetOrderCreateAuthorized(User user) {
        Response response = given()
                .header("Authorization", UserLogin.getAccessToken(user))
                .get(Constants.ORDERS);
        return response;
    }
    @Step("Send correct GET /api/orders request")
    public static Response sendGetOrderCreateAuthorized(String AccessToken) {
        Response response = given()
                .header("Authorization", AccessToken)
                .get(Constants.ORDERS);
        return response;
    }
    @Step("Send unauthorized GET /api/orders request")
    public static Response sendGetOrderCreateUnauthorized() {
        Response response = given()
                .get(Constants.ORDERS);
        return response;
    }
    @Step("Verify success response body")
    public static  void verifySuccessResponseBodyIsCorrect(Response response){
        response.then()
                .body("orders", instanceOf(List.class)).and()
                .body("success", equalTo(true)).and()
                .body("total", instanceOf(Integer.class)).and()
                .body("totalToday", instanceOf(Integer.class)).and()
                .statusCode(200);
    }
}
