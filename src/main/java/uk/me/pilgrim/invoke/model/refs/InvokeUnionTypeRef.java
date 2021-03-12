package uk.me.pilgrim.invoke.model.refs;

import lombok.Data;
import uk.me.pilgrim.invoke.model.NodeLocation;

@Data
public class InvokeUnionTypeRef implements InvokeTypeRef {
    private final InvokeTypeRef a;
    private final InvokeTypeRef b;
    private final NodeLocation location;
}
