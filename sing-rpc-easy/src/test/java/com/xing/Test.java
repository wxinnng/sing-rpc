package com.xing;

import com.xing.model.User;
import com.xing.serializer.JSONSerializer;
import com.xing.serializer.KryoSerializer;
import com.xing.serializer.Serializer;

import java.io.IOException;

public class Test {


    public static void main(String[] args) {
        User user = new User();
        user.setName("wx");
        JSONSerializer jsonSerializer = new JSONSerializer();
        try {
            byte[] serialize = jsonSerializer.serialize(user);
            System.out.println(new String(serialize));
            System.out.println(jsonSerializer.deserialize(serialize, User.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
