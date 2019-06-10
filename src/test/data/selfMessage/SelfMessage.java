package selfMessage;

public class SelfMessage {
    public void clientMethod() {
        internalMethod(1);
    }

    private void internalMethod(int i) {

    }

    public void clientMethod2() {
        internalMethodA(10);
    }

    private void internalMethodA(int i) {
        internalMethodB(100);
    }

    private void internalMethodB(int i) {

    }
}
