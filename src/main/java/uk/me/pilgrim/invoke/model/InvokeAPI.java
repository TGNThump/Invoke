package uk.me.pilgrim.invoke.model;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import uk.me.pilgrim.invoke.exceptions.CompilerExceptionType;
import uk.me.pilgrim.invoke.exceptions.ServicesException;
import uk.me.pilgrim.invoke.model.service.InvokeService;
import uk.me.pilgrim.invoke.model.types.InvokeType;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@ToString
public class InvokeAPI {
    private final Map<String, InvokeService> services = new HashMap<>();
    private final Map<String, InvokeType> types = new HashMap<>();

    public void checkNoDuplicateNames(InvokeNode node){
        InvokeNode other = services.get(node.getName());
        if (other == null) other = types.get(node.getName());
        if (other == null) return;

        throw new ServicesException(CompilerExceptionType.DUPLICATE_OBJECT_NAME, "Duplicate node (type, enum or service) name `{}` at {} and {}", node.getName(), node.getLocation(), other.getLocation()).logWarn(LOGGER);
    }

    public void addService(InvokeService invokeService){
        checkNoDuplicateNames(invokeService);
        services.put(invokeService.getName(), invokeService);
    }

    public void addType(InvokeType invokeType){
        checkNoDuplicateNames(invokeType);
        types.put(invokeType.getName(), invokeType);
    }
}
