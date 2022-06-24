package design_pattern;

public class StaticBlock {
    /**
     * The static block is first executed as soon as the class is loaded before the main(); method is invoked and therefore before main() is called,
     * System.exit(0) initiates VM shut down.
     */
    static {
        System.out.println("hello world");
    }
    public static void main(String[] args) {

    }
}
