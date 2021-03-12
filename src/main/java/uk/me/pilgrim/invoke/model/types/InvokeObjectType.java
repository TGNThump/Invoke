package uk.me.pilgrim.invoke.model.types;

import lombok.Data;
import uk.me.pilgrim.invoke.model.NodeLocation;
import uk.me.pilgrim.invoke.model.refs.InvokeTypeRef;

import java.util.HashMap;
import java.util.Map;

@Data
public class InvokeObjectType implements InvokeType {
    private final String name;
    private final Map<String, InvokeTypeRef> fields = new HashMap<>();
    private final NodeLocation location;
}
