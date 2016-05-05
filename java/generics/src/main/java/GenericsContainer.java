import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by avorona on 27.10.15.
 */
public class GenericsContainer<T> {

    private T innerObj;

    public GenericsContainer() {

    }

    public GenericsContainer(T innerObj) {
        this.innerObj = innerObj;
    }

    public static <Y> Y sayHello(Y obj) throws IllegalAccessException, InstantiationException {
        System.out.println(GenericsContainer.class.isInstance(obj) ? "Obj has the same class as GenericsContainer " :
                "Obj is not the same class as GenericsContainer");
        return (Y) obj.getClass().newInstance();
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        GenericsContainer<Object> container = new GenericsContainer<>(new Object());
        GenericsContainer.sayHello(new ArrayList<>());
        GenericsContainer.sayHello(Arrays.asList(container));
    }
}
