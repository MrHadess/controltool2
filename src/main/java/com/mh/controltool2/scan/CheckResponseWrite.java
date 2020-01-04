package com.mh.controltool2.scan;


public class CheckResponseWrite {


    /*
    * 0不写数据
    * 1直写数据
    * 2需先序列化后写入数据
    */
    @SuppressWarnings("WeakerAccess")
    public ReturnStateToDo responseHandlerSate(String returnType){

        CheckReturnEnum checkReturnEnum = CheckReturnEnum.getByValue(returnType);
        if (checkReturnEnum == null) return ReturnStateToDo.SerializableObjectToWrite;
        switch (checkReturnEnum) {
            case baseVoid:
                return ReturnStateToDo.NothingWrite;
            default:
                return ReturnStateToDo.WriteNow;
        }
    }



    private enum CheckReturnEnum{

        typeString("java.lang.String"),
        typeDouble("java.lang.Double"),
        typeFloat("java.lang.Float"),
        typeInt("java.lang.Integer"),
        typeLong("java.lang.Long"),
        typeBoolean("Java.lang.Boolean"),

        baseVoid("void"),

        baseDouble("double"),
        baseFloat("float"),
        baseInt("int"),
        baseLong("long"),
        baseBoolean("boolean");


        String data;
        CheckReturnEnum(String s){
            data = s;
        }

        private static CheckReturnEnum getByValue(String s){
            for (CheckReturnEnum item:values()){
                if (item.data.equals(s)) return item;
            }
            return null;
        }

    }

    public enum ReturnStateToDo{

        NothingWrite(0),
        WriteNow(1),
        SerializableObjectToWrite(2);

        int state;
        ReturnStateToDo(int i){
            state = i;
        }

        public static ReturnStateToDo getByValue(int i){
            for (ReturnStateToDo item:values()){
                if (item.state == i) return item;
            }
            return null;
        }

    }
}
