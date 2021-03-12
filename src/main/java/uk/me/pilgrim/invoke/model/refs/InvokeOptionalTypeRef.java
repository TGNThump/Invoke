package uk.me.pilgrim.invoke.model.refs;

import lombok.Data;
import uk.me.pilgrim.invoke.model.NodeLocation;

@Data
public class InvokeOptionalTypeRef implements InvokeTypeRef {
    private final InvokeTypeRef ref;
    private final NodeLocation location;
}
