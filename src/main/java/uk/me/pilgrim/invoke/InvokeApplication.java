package uk.me.pilgrim.invoke;

import lombok.extern.log4j.Log4j2;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import uk.me.pilgrim.invoke.exceptions.ServicesException;
import uk.me.pilgrim.invoke.model.*;
import uk.me.pilgrim.invoke.model.refs.*;
import uk.me.pilgrim.invoke.model.service.InvokeMethod;
import uk.me.pilgrim.invoke.model.service.InvokeService;
import uk.me.pilgrim.invoke.model.types.InvokeEnumType;
import uk.me.pilgrim.invoke.model.types.InvokeObjectType;

@Log4j2
@SpringBootApplication
public class InvokeApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(InvokeApplication.class, args);
    }

    @Value("classpath:test.invoke")
    private Resource resource;

    @Override
    public void run(String... args) {
        try {
            CharStream input = CharStreams.fromStream(resource.getInputStream());
            InvokeLexer lexer = new InvokeLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            InvokeParser parser = new InvokeParser(tokens);
            LOGGER.info(generate(parser));
        } catch (ServicesException ex){
            if (!ex.isLogged()) LOGGER.error(ex);
        } catch (Exception ex){
            LOGGER.error(ex);
        }
    }

    private InvokeAPI generate(InvokeParser parser){
        InvokeAPI api = new InvokeAPI();

        for (InvokeParser.DefinitionContext definition : parser.document().definition()) {
            if (definition.serviceTypeDefinition() != null){
                InvokeService invokeService = new InvokeService(definition.serviceTypeDefinition().name().getText(), new NodeLocation(parser, definition.serviceTypeDefinition()));

                for (InvokeParser.MethodDefinitionContext method : definition.serviceTypeDefinition().methodsDefinition().methodDefinition()) {
                    InvokeMethod invokeMethod = new InvokeMethod(method.name().getText(), getTypeRef(parser, method.type_()), new NodeLocation(parser, method));

                    for (InvokeParser.MethodParamDefinitionContext param : method.methodParamsDefinition().methodParamDefinition()) {
                        InvokeTypeRef previousValue = invokeMethod.getParameters().put(param.name().getText(), getTypeRef(parser, param.type_()));
                        if (previousValue != null) throw new IllegalArgumentException();
                    }

                    invokeService.getMethods().add(invokeMethod);
                }

                api.addService(invokeService);
            } else if (definition.enumTypeDefinition() != null){
                InvokeEnumType invokeEnum = new InvokeEnumType(definition.enumTypeDefinition().name().getText(), new NodeLocation(parser, definition.enumTypeDefinition()));
                for (InvokeParser.EnumValueDefinitionContext enumValueDefinition : definition.enumTypeDefinition().enumValuesDefinition().enumValueDefinition()) {
                    boolean optionAdded = invokeEnum.getEnumOptions().add(enumValueDefinition.getText());
                    if (!optionAdded) throw new IllegalArgumentException();
                }

                api.addType(invokeEnum);
            } else if (definition.objectTypeDefinition() != null){
                InvokeObjectType invokeObject = new InvokeObjectType(definition.objectTypeDefinition().name().getText(), new NodeLocation(parser, definition.objectTypeDefinition()));

                for (InvokeParser.FieldDefinitionContext fieldDefinition : definition.objectTypeDefinition().fieldsDefinition().fieldDefinition()) {
                    invokeObject.getFields().put(fieldDefinition.name().getText(), getTypeRef(parser, fieldDefinition.type_()));
                }

                api.addType(invokeObject);
            } else throw new IllegalStateException();
        }

        return api;
    }

    private InvokeTypeRef getTypeRef(InvokeParser parser, InvokeParser.Type_Context type){
        /*
            type_: namedOrListType | requiredNamedOrListType | unionType;
            requiredNamedOrListType: namedOrListType '!';
            namedOrListType: namedType | listType;

            unionType: namedOrListType '||' namedOrListType;
            namedType: name;
            listType: '[' type_ ']';
         */

        if (type.namedOrListType() != null){
            return getTypeRef(parser, type.namedOrListType(), true);
        } else if (type.requiredNamedOrListType() != null){
            return getTypeRef(parser, type.requiredNamedOrListType().namedOrListType(), false);
        } else if (type.unionType() != null){
            return new InvokeUnionTypeRef(
                    getTypeRef(parser, type.unionType().namedOrListType(0), false),
                    getTypeRef(parser, type.unionType().namedOrListType(1), false),
                    new NodeLocation(parser, type.unionType())
            );
        } else throw new IllegalStateException();
    }

    private InvokeTypeRef getTypeRef(InvokeParser parser, InvokeParser.NamedOrListTypeContext type, boolean optional){
        InvokeTypeRef subType;

        if (type.namedType() != null){
            subType = new InvokeUnitTypeRef(type.namedType().getText(), new NodeLocation(parser, type.namedType()));
        } else if (type.listType() != null){
            subType = new InvokeListTypeRef(getTypeRef(parser, type.listType().type_()), new NodeLocation(parser, type.listType()));
        } else throw new IllegalStateException();

        if (optional) return new InvokeOptionalTypeRef(subType, new NodeLocation(parser, type));
        else return subType;
    }
}
