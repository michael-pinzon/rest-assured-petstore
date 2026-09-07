package com.petdog.api.client;

import com.petdog.api.config.ApiConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Small endpoint client that keeps HTTP details out of the test classes.
 */
public final class PetStoreClient {

    private PetStoreClient() {
        // Utility class.
    }

    /**
     * Creates the common request specification for Petstore v2.
     *
     * @return request specification with base URL and JSON response handling
     */
    public static RequestSpecification request() {
        return given()
                .baseUri(ApiConfig.baseUri())
                .basePath(ApiConfig.basePath())
                .accept(ContentType.JSON);
    }

    /**
     * Sends a create-user request.
     *
     * @param user user payload
     * @return API response
     */
    public static Response createUser(Map<String, Object> user) {
        return request()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/user");
    }

    /**
     * Sends a login request.
     *
     * @param username username to authenticate
     * @param password password to authenticate
     * @return API response
     */
    public static Response login(String username, String password) {
        return request()
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get("/user/login");
    }

    /**
     * Sends a logout request for the current session.
     *
     * @return API response
     */
    public static Response logout() {
        return request()
                .when()
                .get("/user/logout");
    }

    /**
     * Retrieves pets filtered by status.
     *
     * @param status Petstore status filter
     * @return API response
     */
    public static Response findPetsByStatus(String status) {
        return request()
                .queryParam("status", status)
                .when()
                .get("/pet/findByStatus");
    }

    /**
     * Retrieves one pet by identifier.
     *
     * @param petId pet identifier
     * @return API response
     */
    public static Response getPetById(long petId) {
        return request()
                .pathParam("petId", petId)
                .when()
                .get("/pet/{petId}");
    }

    /**
     * Places an order for a pet.
     *
     * @param order order payload
     * @return API response
     */
    public static Response placeOrder(Map<String, Object> order) {
        return request()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post("/store/order");
    }

    /**
     * Finds one available pet for a test that needs a valid independent
     * fixture. The lookup is intentionally performed by the caller's test
     * setup instead of reusing state produced by another test.
     *
     * @return identifier of the first available pet
     * @throws IllegalStateException if the API has no available pet
     */
    public static long firstAvailablePetId() {
        Response response = findPetsByStatus("available");
        response.then().statusCode(200);

        List<Number> petIds = response.jsonPath().getList("id");
        if (petIds == null || petIds.isEmpty() || petIds.get(0) == null) {
            throw new IllegalStateException("Petstore returned no available pet with an id");
        }
        return petIds.get(0).longValue();
    }
}
