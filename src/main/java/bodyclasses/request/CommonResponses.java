package bodyclasses.request;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.equalTo;

public class CommonResponses {
    @Step("Checks if the response is correct for a request with correct data")
    public static void compareWithCorrectResponse(Response response){
        response.then()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }
    @Step("Compare response with response when user unauthorised")
    public static  void compareResponseWithoutCorrectAccessToken(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("You should be authorised"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}
