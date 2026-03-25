package demo.domain.ports.out;

public interface UserCredentialsPort {

    boolean isValid(String username, String password);
}
