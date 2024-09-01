package com.xing.serializer;



import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data)) {
            HessianInput hessianInput = new HessianInput(byteArrayInputStream);
            return clazz.cast(hessianInput.readObject());
        }
    }
}
