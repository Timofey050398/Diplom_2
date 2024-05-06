package bodyclasses.response.Ingredients;

import bodyclasses.Constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class GetIngredients {
    private boolean success;
    public List<Data> data;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
    @Step("Send GET /api/ingredients")
    public static Response sendGetIngredients(){
        Response response = given()
                .get(Constants.INGREDIENTS);
        return response;
    }
    @Step("Сохранить список хешей всех ингредиентов")
    public List<String> getIngredientsHash(Response response) {
        GetIngredients ingredients = response.body().as(GetIngredients.class);
        List<Data> ingredientsData = ingredients.getData();
        List<String> ingredientsHash = new ArrayList<>();
        if (ingredientsData != null) {
            for (Data ingredientData : ingredientsData) {
                ingredientsHash.add(ingredientData.get_id());
            }
        }
        return ingredientsHash;
    }
    @Step("Сохранить список хешей булок")
    public List<String> getBunsHash(Response response) {
        GetIngredients ingredients = response.body().as(GetIngredients.class);
        List<Data> ingredientsData = ingredients.getData();
        List<String> bunsHash = new ArrayList<>();
        if (ingredientsData != null) {
            for (Data ingredientData : ingredientsData) {
                if("bun".equals(ingredientData.getType())){
                    bunsHash.add(ingredientData.get_id());
                }
            }
        }
        return bunsHash;
    }
    @Step("Сохранить список хешей соусов")
    public List<String> getSaucesHash(Response response) {
        GetIngredients ingredients = response.body().as(GetIngredients.class);
        List<Data> ingredientsData = ingredients.getData();
        List<String> saucesHash = new ArrayList<>();
        if (ingredientsData != null) {
            for (Data ingredientData : ingredientsData) {
                if("sauce".equals(ingredientData.getType())){
                    saucesHash.add(ingredientData.get_id());
                }
            }
        }
        return saucesHash;
    }
    @Step("Сохранить список хешей начинок")
    public List<String> getMainsHash(Response response) {
        GetIngredients ingredients = response.body().as(GetIngredients.class);
        List<Data> ingredientsData = ingredients.getData();
        List<String> mainsHash = new ArrayList<>();
        if (ingredientsData != null) {
            for (Data ingredientData : ingredientsData) {
                if("main".equals(ingredientData.getType())){
                    mainsHash.add(ingredientData.get_id());
                }
            }
        }
        return mainsHash;
    }
    @Step("Добавить случайные начинки")
    public static List<String> addRandomMains(List<String> mainsHash){
        Random random = new Random();
        List<String> randomMains = new ArrayList<>();
        int countMains = random.nextInt(mainsHash.size()+1);
        for(int i =0; i<countMains;i++){
            int randomMain = random.nextInt(mainsHash.size());
            randomMains.add(mainsHash.get(randomMain));
        }
        return randomMains;
    }
    @Step("Добавить случайные соусы")
    public static List<String> addRandomSauces(List<String> saucesHash){
        Random random = new Random();
        List<String> randomSauces = new ArrayList<>();
        int countSauces = random.nextInt(saucesHash.size()+1);
        for(int i =0; i<countSauces;i++){
            int randomMain = random.nextInt(saucesHash.size());
            randomSauces.add(saucesHash.get(randomMain));
        }
        return randomSauces;
    }
    @Step("Добавить случайную булку")
    public static String addRandomBun(List<String> bunsHash){
        Random random = new Random();
        int randomBun = random.nextInt(bunsHash.size());
        String bun = bunsHash.get(randomBun);
        return bun;
    }
    @Step("Собрать случайное тело запроса заказа с одной булкой и случайным кол-вом соусов и начинок")
    public static List<String> buildRandomOrder(List<String> saucesHash,List<String> mainsHash,List<String> bunsHash){
        List<String> orderBody = new ArrayList<>();
        orderBody.add(GetIngredients.addRandomBun(bunsHash));
        orderBody.addAll(saucesHash);
        orderBody.addAll(mainsHash);
        return orderBody;
    }
}
