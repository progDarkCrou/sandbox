package web.vo;

/**
 * Created by avorona on 06.10.15.
 */
public class ErrorAnswer extends Answer {

    private String error;

    public ErrorAnswer(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
