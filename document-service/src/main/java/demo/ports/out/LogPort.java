package demo.ports.out;
public interface LogPort {
    void info(String msg);
    void error(String msg, Exception ex);
}