package realworld.jaxrs.impl.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.jboss.resteasy.mock.MockHttpResponse;

/**
 * Assertions helper for the {@code UserWithToken} object.
 */
class UserWithTokenAssertions {
	private JsonObject userWithToken;

	static UserWithTokenAssertions assertUserWithToken(MockHttpResponse response) throws UnsupportedEncodingException {
		assertEquals(200, response.getStatus());
		JsonReader jsonReader = Json.createReader(new StringReader(response.getContentAsString()));
		JsonObject jobj = jsonReader.readObject();
		assertEquals(1, jobj.size());
		JsonObject userWithToken = jobj.getJsonObject("user");
		assertNotNull(userWithToken);
		assertEquals(5, userWithToken.size());
		return new UserWithTokenAssertions(userWithToken);
	}

	UserWithTokenAssertions(JsonObject userWithToken) {
		this.userWithToken = userWithToken;
	}

	UserWithTokenAssertions assertUsername(String username) {
		assertEquals(username, userWithToken.getString("username"));
		return this;
	}

	UserWithTokenAssertions assertEmail(String email) {
		assertEquals(email, userWithToken.getString("email"));
		return this;
	}

	UserWithTokenAssertions assertBio(String bio) {
		JsonValue bioVal = userWithToken.get("bio");
		assertEquals(bio, bioVal.getValueType() == JsonValue.ValueType.NULL ? null : userWithToken.getString("bio"));
		return this;
	}

	UserWithTokenAssertions assertImage(String image) {
		JsonValue imageVal = userWithToken.get("image");
		assertEquals(image, imageVal.getValueType() == JsonValue.ValueType.NULL ? null : userWithToken.getString("image"));
		return this;
	}

	UserWithTokenAssertions assertToken(String token) {
		assertEquals(token, userWithToken.getString("token"));
		return this;
	}
}
