package com.petdog.api.data;

import java.util.Map;

/**
 * Immutable credentials and payload for one test user.
 *
 * @param id user identifier sent to Petstore
 * @param username unique username sent to Petstore
 * @param password password used by login tests
 * @param payload complete JSON-compatible user payload
 */
public record TestUser(long id, String username, String password, Map<String, Object> payload) {
}
