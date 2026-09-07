package com.petdog.api.tests;

import com.petdog.api.client.PetStoreClient;
import com.petdog.api.data.TestDataFactory;
import com.petdog.api.data.TestUser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

/**
 * Contract tests for Petstore user and session operations.
 *
 * <p>Every test creates its own user. The tests can therefore be executed in
 * any order or individually without relying on another test's data.</p>
 */
public class UserApiTest {

    /**
     * Verifies that Petstore accepts a complete user payload.
     */
    @Test(description = "Create a unique PetDog user")
    public void createUser_returnsSuccessResponse() {
        // Given
        TestUser user = TestDataFactory.newUser();

        // When
        Response response = PetStoreClient.createUser(user.payload());

        // Then
        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("message", equalTo(String.valueOf(user.id())));
    }

    /**
     * Verifies login with credentials from a user created in this same test.
     */
    @Test(description = "Login with a newly created user")
    public void login_withNewlyCreatedUser_returnsSessionMessage() {
        // Given
        TestUser user = TestDataFactory.newUser();
        PetStoreClient.createUser(user.payload())
                .then()
                .statusCode(200)
                .body("code", equalTo(200));

        // When
        Response response = PetStoreClient.login(user.username(), user.password());

        // Then
        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("message", startsWith("logged in user session:"));

        String sessionMessage = response.jsonPath().getString("message");
        Assert.assertTrue(
                sessionMessage.matches("logged in user session:\\d+"),
                "Login response should contain a numeric session identifier"
        );
    }

    /**
     * Verifies logout after establishing a session in this same test.
     */
    @Test(description = "Logout from an authenticated session")
    public void logout_afterLogin_returnsSuccessResponse() {
        // Given
        TestUser user = TestDataFactory.newUser();
        PetStoreClient.createUser(user.payload())
                .then()
                .statusCode(200);
        PetStoreClient.login(user.username(), user.password())
                .then()
                .statusCode(200)
                .body("code", equalTo(200));

        // When
        Response response = PetStoreClient.logout();

        // Then
        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("message", equalTo("ok"));
    }
}
