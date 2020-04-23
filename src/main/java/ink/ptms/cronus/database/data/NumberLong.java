package ink.ptms.cronus.database.data;

import io.izzel.taboolib.internal.gson.*;

import java.lang.reflect.Type;

/**
 * @Author sky
 * @Since 2020-03-13 16:49
 */
public class NumberLong implements JsonSerializer, JsonDeserializer {

    @Override
    public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return jsonElement.isJsonPrimitive() ? jsonElement.getAsLong() : jsonElement.getAsJsonObject().get("$numberLong").getAsLong();
    }

    @Override
    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive((long) o);
    }
}
