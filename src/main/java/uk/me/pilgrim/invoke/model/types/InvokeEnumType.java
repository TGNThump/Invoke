package uk.me.pilgrim.invoke.model.types;

import lombok.Data;
import uk.me.pilgrim.invoke.model.NodeLocation;

import java.util.HashSet;
import java.util.Set;

@Data
public class InvokeEnumType implements InvokeType {
    private final String name;
    private final Set<String> enumOptions = new HashSet<>();
    private final NodeLocation location;
}
