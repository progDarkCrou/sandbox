package web.vo;

/**
 * Created by avorona on 05.10.15.
 */
public class InitializationVO {

    private String hello;

    public InitializationVO() {
    }

    public InitializationVO(String hello) {
        this.hello = hello;
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }
}
