package com.petdog.api.tests;

import com.petdog.api.client.PetStoreClient;
import com.petdog.api.data.TestDataFactory;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Contract tests for Petstore order operations.
 */
public class OrderApiTest {

    /**
     * Selects an available pet and places a complete order in this test so it
     * does not depend on the result or execution order of another test.
     */
    @Test(description = "Create an order for an available pet")
    public void placeOrder_forAvailablePet_returnsPlacedOrder() {
        // Given
        long petId = PetStoreClient.firstAvailablePetId();
        Map<String, Object> order = TestDataFactory.newOrder(petId);
        long orderId = ((Number) order.get("id")).longValue();

        // When
        Response response = PetStoreClient.placeOrder(order);

        // Then
        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON);

        Assert.assertEquals(response.jsonPath().getLong("id"), orderId);
        Assert.assertEquals(response.jsonPath().getLong("petId"), petId);
        Assert.assertEquals(response.jsonPath().getInt("quantity"), 1);
        Assert.assertEquals(response.jsonPath().getString("status"), "placed");
        Assert.assertTrue(response.jsonPath().getBoolean("complete"));
        Assert.assertNotNull(response.jsonPath().getString("shipDate"));
    }
}
