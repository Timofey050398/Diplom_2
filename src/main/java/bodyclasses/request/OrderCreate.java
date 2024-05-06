package bodyclasses.request;

import bodyclasses.Constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class OrderCreate {
    List<String> ingredients = new ArrayList<>();
    public OrderCreate(){};
    public OrderCreate(List<String> ingredients){
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
    @Step("Записать список в массив ingredients")
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public void removeAllIngredients(){
        ingredients.clear();
    }
    public void removeIngredients(String ingredient){
        ingredients.remove(ingredient);
    }
    public void addIngredients(List<String> newIngredients){
        ingredients.addAll(newIngredients);
    }
    public void addIngredient(String ingredient){
        ingredients.add(ingredient);
    }
    @Step("Send correct POST /api/orders request")
    public static Response sendPostOrderCreateAuthorized(User user, OrderCreate order) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", UserLogin.getAccessToken(user))
                .and()
                .body(order)
                .when()
                .post(Constants.ORDERS);
        return response;
    }
    @Step("Send correct POST /api/orders request")
    public static Response sendPostOrderCreateAuthorized(String AccessToken, OrderCreate order) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", AccessToken)
                .and()
                .body(order)
                .when()
                .post(Constants.ORDERS);
        return response;
    }
    @Step("Send unauthorized POST /api/orders request")
    public static Response sendPostOrderCreateUnauthorized(OrderCreate order) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(Constants.ORDERS);
        return response;
    }
    @Step("Compare the answer with invalid hash (Order Create)")
    public static  void compareResponseWithInvalidHash(Response response){
        response.then()
                .assertThat()
                .statusCode(500);
    }
    @Step("Compare the answer with answer without ingredients (Order Create)")
    public static  void compareResponseWithoutIngredients(Response response){
        response.then()
                .assertThat()
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(400);
    }
    @Step("Verify success response body")
    public static void verifyResponseStructure(Response response) {
        response.then().assertThat()
                .body("name", instanceOf(String.class)).and()
                .body("order.number", instanceOf(Integer.class)).and()
                .body("success", equalTo(true)).and()
                .statusCode(200);
    }
}
