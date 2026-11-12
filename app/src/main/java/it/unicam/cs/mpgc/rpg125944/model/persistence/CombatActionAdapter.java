package it.unicam.cs.mpgc.rpg125944.model.persistence;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.unicam.cs.mpgc.rpg125944.model.combat.CombatAction;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

/**
 * Adapter Gson per {@link CombatAction}: sceglie il {@link CombatActionCodec} adatto
 * in base al tipo dell'azione (in scrittura) o al campo "type" del JSON (in lettura).
 */
public class CombatActionAdapter implements JsonSerializer<CombatAction>, JsonDeserializer<CombatAction> {
    private static final String TYPE = "type";

    private final List<CombatActionCodec> codecs;

    public CombatActionAdapter(List<CombatActionCodec> codecs) {
        this.codecs = List.copyOf(Objects.requireNonNull(codecs, "codecs non puo' essere null"));
    }

    @Override
    public JsonElement serialize(CombatAction action, Type typeOfSrc, JsonSerializationContext context) {
        CombatActionCodec codec = codecs.stream()
                .filter(candidate -> candidate.supports(action))
                .findFirst()
                .orElseThrow(() -> new JsonParseException(
                        "azione di combattimento non supportata: " + action.getClass().getSimpleName()));
        JsonObject json = codec.encode(action);
        json.addProperty(TYPE, codec.getType());
        return json;
    }

    @Override
    public CombatAction deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) {
        if (!element.isJsonObject() || !element.getAsJsonObject().has(TYPE)) {
            throw new JsonParseException("tipo di azione mancante");
        }
        JsonObject json = element.getAsJsonObject();
        String type = json.get(TYPE).getAsString();
        return codecs.stream()
                .filter(candidate -> candidate.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new JsonParseException("tipo di azione non supportato: " + type))
                .decode(json);
    }
}
