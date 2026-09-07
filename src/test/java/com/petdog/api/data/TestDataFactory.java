package com.petdog.api.data;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Builds isolated payloads for tests that write data to the public API.
 */
public final class TestDataFactory {

    private static final AtomicLong SEQUENCE = new AtomicLong(System.currentTimeMillis());

    private TestDataFactory() {
        // Utility class.
    }

    /**
     * Creates a unique user so a test run does not depend on a pre-existing
     * account or on another test's data.
     *
     * @return user credentials and the request payload
     */
    public static TestUser newUser() {
        long id = nextId();
        String suffix = id + "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String username = "petdog_user_" + suffix;
        String password = "PetDog!21";

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", id);
        payload.put("username", username);
        payload.put("firstName", "PetDog");
        payload.put("lastName", "Tester");
        payload.put("email", "petdog." + suffix + "@example.com");
        payload.put("password", password);
        payload.put("phone", "+5715550000");
        payload.put("userStatus", 1);

        return new TestUser(id, username, password, Map.copyOf(payload));
    }

    /**
     * Creates a valid order payload for the supplied pet.
     *
     * @param petId available pet identifier
     * @return JSON-compatible order payload
     */
    public static Map<String, Object> newOrder(long petId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", nextId());
        payload.put("petId", petId);
        payload.put("quantity", 1);
        payload.put("shipDate", OffsetDateTime.now(ZoneOffset.UTC).toString());
        payload.put("status", "placed");
        payload.put("complete", true);
        return Map.copyOf(payload);
    }

    private static long nextId() {
        return SEQUENCE.getAndIncrement();
    }
}
