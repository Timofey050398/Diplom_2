import bodyclasses.Constants;
import bodyclasses.request.*;
import bodyclasses.response.Ingredients.GetIngredients;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Тесты запроса списка заказов пользователя GET /api/orders")
public class UserGetOrdersTest {
    private List<String> bunsHash;
    private List<String> saucesHash;
    private List<String> mainsHash;
    User user = new UserCreate();

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }
    @Test
    @DisplayName("Запросить заказы пользователя без авторизации")
    public void UserGetOrdersUnauthorized(){
        Response response = UserGetOrders.sendGetOrderCreateUnauthorized();
        CommonResponses.compareResponseWithoutCorrectAccessToken(response);
    }
    @Test
    @DisplayName("Запросить заказы пользователя с авторизацией")
    public void UserGetOrdersAuthorized(){
        UserCreate.sendPostUserCreate(user);
        GetIngredients allIngredients = new GetIngredients();
        Response getIngredients = allIngredients.sendGetIngredients();
        bunsHash = allIngredients.getBunsHash(getIngredients);
        saucesHash = allIngredients.getSaucesHash(getIngredients);
        mainsHash = allIngredients.getMainsHash(getIngredients);
        OrderCreate order = new OrderCreate();
        order.setIngredients(GetIngredients.buildRandomOrder(saucesHash,mainsHash,bunsHash));
        OrderCreate.sendPostOrderCreateAuthorized(user,order);
        Response response = UserGetOrders.sendGetOrderCreateAuthorized(user);
        UserGetOrders.verifySuccessResponseBodyIsCorrect(response);
        UserDelete.sendDeleteUser(user);
    }
}
