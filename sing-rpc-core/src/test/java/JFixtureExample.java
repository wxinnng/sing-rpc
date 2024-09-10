import com.flextrade.jfixture.JFixture;

public class JFixtureExample {
    public static <T> T createDefaultInstance(Class<T> clazz) {
        JFixture fixture = new JFixture();
        return fixture.create(clazz);
    }

    public static void main(String[] args) {
        MyBean bean = createDefaultInstance(MyBean.class);
        System.out.println(bean);
    }
}

class MyBean {
    private String name;
    private int age;

    // Getters and Setters

    @Override
    public String toString() {
        return "MyBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
