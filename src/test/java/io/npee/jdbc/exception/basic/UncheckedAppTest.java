package io.npee.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

@Slf4j
public class UncheckedAppTest {

    @Test
    void unchecked() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request());
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            log.error("ex", e);
        }
    }

    static class Controller {
        Service service = new Service();

        public void request() {
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        /**
         * 예외를 모두 던져야 한다.
         * @throws ConnectException
         * @throws SQLException
         */
        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class Repository {
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);
            }
        }

        private void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectionException("연결 실패");
        }
    }

    static class RuntimeConnectionException extends RuntimeException {
        public RuntimeConnectionException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }

}
