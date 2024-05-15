import bodyclasses.Constants;
import bodyclasses.request.*;
import bodyclasses.response.Ingredients.GetIngredients;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Тесты метода создания заказа POST /api/orders")
public class OrderCreateTest {
    private List<String> bunsHash;
    private List<String> saucesHash;
    private List<String> mainsHash;
    Faker faker = new Faker();

    List<String> requestBody = new ArrayList<>();
    @Before
    @DisplayName("Предусловие: запросить ингридиенты")
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
        GetIngredients allIngredients = new GetIngredients();
        Response getIngredients = GetIngredients.sendGetIngredients();
        bunsHash = allIngredients.getBunsHash(getIngredients);
        saucesHash = allIngredients.getSaucesHash(getIngredients);
        mainsHash = allIngredients.getMainsHash(getIngredients);
    }
    @Test
    @DisplayName("Заказ неавторизованным пользователем")
    public void correctOrderUnauthorisedUser(){
        OrderCreate order = new OrderCreate();
        order.setIngredients(GetIngredients.buildRandomOrder(saucesHash,mainsHash,bunsHash));
        Response response = OrderCreate.sendPostOrderCreateUnauthorized(order);
        OrderCreate.verifyResponseStructure(response);
    }
    @Test
    @DisplayName("Заказ авторизованным пользователем")
    public void correctOrderAuthorisedUser(){
        User user = new UserCreate();
        UserCreate.sendPostUserCreate(user);
        OrderCreate order = new OrderCreate();
        order.setIngredients(GetIngredients.buildRandomOrder(saucesHash,mainsHash,bunsHash));
        Response response = OrderCreate.sendPostOrderCreateAuthorized(user,order);
        OrderCreate.verifyResponseStructure(response);
        UserDelete.sendDeleteUser(user);
    }
    @Test
    @DisplayName("Отправить заказ без ингридиентов")
    public void sendOrderWithoutIngredients(){
        OrderCreate order = new OrderCreate();
        order.setIngredients(requestBody);
        Response response = OrderCreate.sendPostOrderCreateUnauthorized(order);
        OrderCreate.compareResponseWithoutIngredients(response);
    }
    @Test
    @DisplayName("Отправить заказ с не верным хешем ингредиента")
    public void sendOrderWithWrongIngredients(){
        OrderCreate order = new OrderCreate();
        String ingredient = faker.name().firstName();
        order.addIngredient(ingredient);
        Response response = OrderCreate.sendPostOrderCreateUnauthorized(order);
        OrderCreate.compareResponseWithInvalidHash(response);
    }
}
