package uk.me.pilgrim.invoke.model.refs;

import lombok.Data;
import uk.me.pilgrim.invoke.model.NodeLocation;

@Data
public class InvokeUnitTypeRef implements InvokeTypeRef {
    private final String ref;
    private final NodeLocation location;
}
