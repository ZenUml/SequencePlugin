package selfMessage;

public class SelfMessage {
    public void clientMethod() {
        internalMethod(1);
    }

    private void internalMethod(int i) {

    }

    public void clientMethod2() {
        int i = 10;
        internalMethodA(i);
    }

    private void internalMethodA(int i) {
        internalMethodB(100, 1000);
    }

    private void internalMethodB(int i, int i1) {
        internalMethodC(i1, i);
    }

    private void internalMethodC(int i, int i1) {

    }
}
