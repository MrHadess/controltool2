package com.mh.controltool2.method.type;

public enum TypeEnum {

    Unmatched,// unmatched arg type,may be data arg is null handler
    SupportDefaultValue,// program support default value sample (http req resp)
    InputObject,// Autowired or Bean annotation
    ConfigValue,// read properties file from key

//    program method
    RequestBody,
    RequestParam,
    RequestHeader,
    PathVariable

}
