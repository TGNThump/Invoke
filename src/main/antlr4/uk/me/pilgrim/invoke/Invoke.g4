grammar Invoke;

document: definition+;

definition: typeDefinition;

typeDefinition: serviceTypeDefinition | objectTypeDefinition | enumTypeDefinition;

serviceTypeDefinition: 'service' name methodsDefinition?;

methodsDefinition: '{' methodDefinition+ '}';
methodDefinition: name methodParamsDefinition? ':' type_;
methodParamsDefinition: '(' methodParamDefinition+ ')';
methodParamDefinition: name ':' type_;

objectTypeDefinition : 'type' name fieldsDefinition?;

fieldsDefinition: '{'  fieldDefinition+ '}';
fieldDefinition: name ':' type_;

type_: optionalType '!'? | unionType;
optionalType: namedType | listType;

unionType: optionalType '||' optionalType;
namedType: name;
listType: '[' type_ ']';

enumTypeDefinition:  'enum' name enumValuesDefinition?;
enumValuesDefinition: '{' enumValueDefinition+  '}';
enumValueDefinition: enumValue;

enumValue: name;

name: NAME;

//Start lexer
NAME: [_A-Za-z] [_0-9A-Za-z]*;

PUNCTUATOR: '!'
    | '(' | ')'
    | ':'
    | '||'
    | '[' | ']'
    | '{' | '}'
    ;

WS: [ \t\n\r]+ -> skip;
COMMA: ',' -> skip;
LineComment
    :   '#' ~[\r\n]*
        -> skip
    ;

UNICODE_BOM: (UTF8_BOM
    | UTF16_BOM
    | UTF32_BOM
    ) -> skip
    ;

UTF8_BOM: '\uEFBBBF';
UTF16_BOM: '\uFEFF';
UTF32_BOM: '\u0000FEFF';