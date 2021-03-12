package uk.me.pilgrim.invoke.model.service;

import lombok.Data;
import uk.me.pilgrim.invoke.model.InvokeNode;
import uk.me.pilgrim.invoke.model.NodeLocation;

import java.util.ArrayList;
import java.util.List;

@Data
public class InvokeService implements InvokeNode {
    private final String name;
    private final List<InvokeMethod> methods = new ArrayList<>();
    private final NodeLocation location;
}
