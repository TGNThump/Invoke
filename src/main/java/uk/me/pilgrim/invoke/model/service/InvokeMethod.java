package uk.me.pilgrim.invoke.model.service;

import lombok.Data;
import uk.me.pilgrim.invoke.model.InvokeNode;
import uk.me.pilgrim.invoke.model.NodeLocation;
import uk.me.pilgrim.invoke.model.refs.InvokeTypeRef;

import java.util.HashMap;
import java.util.Map;

@Data
public class InvokeMethod implements InvokeNode {
    private final String name;
    private final Map<String, InvokeTypeRef> parameters = new HashMap<>();
    private final InvokeTypeRef returnType;
    private final NodeLocation location;
}
