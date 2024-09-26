package io.github.wxinnng.serializer;

import lombok.extern.slf4j.Slf4j;

import java.io.*;


@Slf4j
public class JDKSerializer implements Serializer{
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        log.info("serialize by jdk");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        log.info("deserialize by jdk");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        try {
            return (T) clazz.cast(objectInputStream.readObject());
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        } finally {
            objectInputStream.close();
        }
    }
}
