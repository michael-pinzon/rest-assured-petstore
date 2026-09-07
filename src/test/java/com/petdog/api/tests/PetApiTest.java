package com.petdog.api.tests;

import com.petdog.api.client.PetStoreClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contract tests for Petstore read operations.
 */
public class PetApiTest {

    /**
     * Verifies that the status filter returns a non-empty collection whose
     * pets all report the requested status.
     */
    @Test(description = "List all pets with available status")
    public void findPetsByStatus_available_returnsAvailablePets() {
        // When
        Response response = PetStoreClient.findPetsByStatus("available");

        // Then
        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON);

        List<Map<String, Object>> pets = response.jsonPath().getList("$");
        Assert.assertFalse(pets.isEmpty(), "The available-pets response should not be empty");
        for (Map<String, Object> pet : pets) {
            Assert.assertEquals(pet.get("status"), "available");
            Assert.assertNotNull(pet.get("id"), "Each pet should include an id");
        }
    }

    /**
     * Selects a currently available pet in this test, then verifies its
     * detail endpoint returns the same identifier and a valid Pet payload.
     */
    @Test(description = "Get a specific pet by id")
    public void getPetById_forAvailablePet_returnsPetDetails() {
        // Given: choose the fixture independently for this test.
        long petId = PetStoreClient.firstAvailablePetId();

        // When
        Response response = PetStoreClient.getPetById(petId);

        // Then
        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON);

        Assert.assertEquals(response.jsonPath().getLong("id"), petId);
        String petName = response.jsonPath().getString("name");
        Assert.assertNotNull(petName, "The pet should include a name");
        Assert.assertFalse(petName.isBlank(), "The pet name should not be blank");
        Assert.assertNotNull(
                response.jsonPath().getList("photoUrls"),
                "The pet should include the photoUrls field"
        );
        Assert.assertTrue(
                Set.of("available", "pending", "sold").contains(response.jsonPath().getString("status")),
                "The pet status should be one of the documented Petstore values"
        );
    }
}
