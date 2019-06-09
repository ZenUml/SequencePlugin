package differentClass;

public class FirstClass {
    public void clientMethod() {
        SecondClass secondClass = new SecondClass();
        secondClass.method1();
    }

    private class SecondClass {
        void method1() {

        }
    }
}
