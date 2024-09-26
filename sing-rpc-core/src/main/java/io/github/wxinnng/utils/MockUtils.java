package io.github.wxinnng.utils;

import cn.hutool.core.util.ReflectUtil;
import com.flextrade.jfixture.JFixture;
import org.checkerframework.checker.units.qual.Length;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class MockUtils {

    private static final Boolean[] bools   = new Boolean[] { true, false };

    private static final char[] words = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    private static final Random r  = new Random();

    private static final int   MAX_COLLECTION_LENGTH = 50;

    private static final int   MAX_STRING_LENGTH     = 30;

    /**
     * 生成指定类型的默认值对象（可自行完善默认值逻辑）
     *
     * @param clazz
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T getDefaultObject(Class<T> clazz) {
        if (clazz == Character.class || clazz == Character.TYPE) {
            return (T) (Character) words[r.nextInt(words.length)];
        } else if (clazz == Boolean.class || clazz == Boolean.TYPE) {
            return (T) (Boolean) bools[r.nextInt(bools.length)];
        } else if (clazz == Long.class || clazz == Long.TYPE) {
            return (T) (Long) r.nextLong();
        } else if (clazz == Integer.class || clazz == Integer.TYPE) {
            return (T) (Integer) r.nextInt();
        } else if (clazz == Short.class || clazz == Short.TYPE) {
            return (T) (Short) new Integer(r.nextInt(127)).shortValue();
        } else if (clazz == Float.class || clazz == Float.TYPE) {
            return (T) (Float) r.nextFloat();
        } else if (clazz == Double.class || clazz == Double.TYPE) {
            return (T) (Double) r.nextDouble();
        } else if (clazz == String.class) {
            return (T) randString(r.nextInt(MAX_STRING_LENGTH));
        }

        try {
            T instance = clazz.newInstance();

            for (Field f : ReflectUtil.getFields(clazz)) {
                f.setAccessible(true);
                Length lengthAnno = f.getAnnotation(Length.class);

                if (f.getType() == Character.TYPE) {
                    f.setChar(instance, words[r.nextInt(words.length)]);
                } else if (f.getType() == Character.class) {
                    f.set(instance, words[r.nextInt(words.length)]);
                } else if (f.getType() == Boolean.TYPE) {
                    f.setBoolean(instance, bools[r.nextInt(bools.length)]);
                } else if (f.getType() == Boolean.class) {
                    f.set(instance, bools[r.nextInt(bools.length)]);
                } else if (f.getType() == Long.TYPE) {
                    f.setLong(instance, r.nextLong());
                } else if (f.getType() == Long.class) {
                    f.set(instance, r.nextLong());
                } else if (f.getType() == Integer.TYPE) {
                    f.setInt(instance, r.nextInt());
                } else if (f.getType() == Integer.class) {
                    f.set(instance, r.nextInt());
                } else if (f.getType() == Short.TYPE) {
                    f.setShort(instance, new Integer(r.nextInt(127)).shortValue());
                } else if (f.getType() == Short.class) {
                    f.set(instance, new Integer(r.nextInt(127)).shortValue());
                } else if (f.getType() == Float.TYPE) {
                    f.setFloat(instance, r.nextFloat());
                } else if (f.getType() == Float.class) {
                    f.set(instance, r.nextFloat());
                } else if (f.getType() == Double.TYPE) {
                    f.setDouble(instance, r.nextDouble());
                } else if (f.getType() == Double.class) {
                    f.set(instance, r.nextDouble());
                } else if (f.getType() == String.class) {
                    f.set(instance, randString(r.nextInt(MAX_STRING_LENGTH)));
                } else if (f.getType() == List.class) {
                    int size = r.nextInt(MAX_COLLECTION_LENGTH);
                    List<Object> list = new ArrayList<Object>(size);
                    ParameterizedType pt = null;
                    for (int i = 0; i < size; i++) {
                        pt = (ParameterizedType) f.getGenericType();
                        list.add(getDefaultObject((Class) pt.getActualTypeArguments()[0]));
                    }
                    f.set(instance, list);
                } else if (f.getType() == Map.class) {
                    int size = r.nextInt(MAX_COLLECTION_LENGTH);
                    Map<Object, Object> map = new HashMap<Object, Object>();
                    ParameterizedType pt = null;
                    for (int i = 0; i < size; i++) {
                        pt = (ParameterizedType) f.getGenericType();
                        map.put(getDefaultObject((Class) pt.getActualTypeArguments()[0]),
                                getDefaultObject((Class) pt.getActualTypeArguments()[1]));
                    }
                    f.set(instance, map);
                } else if (f.getType() == Date.class) {
                    f.set(instance, new Date());
                } else {
                    f.set(instance, getDefaultObject(f.getType()));
                }
            }

            return instance;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String randString(int count) {
        if (count == 0) {
            count = 1;
        }

        int length = words.length;
        char[] text = new char[count];
        for (int i = 0; i < count; i++) {
            text[i] = words[r.nextInt(length)];
        }

        return new String(text);
    }



}
