package io.npee.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckedTest {

    @Test
    void checked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void checked_throw() {
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyCheckedException.class);
    }

    /**
     * 체크 예외 (extends Exception)
     */
    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }

    /**
     * Checked 예외는
     * 예외를 잡아서 처리하거나
     * 던지거나
     * 둘 중에 하나를 선택해야 한다.
     */
    static class Service {
        Repository repository = new Repository();

        /**
         * 예외 처리
         */
        public void callCatch() {
            try {
                repository.call();
            } catch (MyCheckedException e) {
                // Exception 으로 잡아도 같이 잡힌다.
                // 예외 처리 로직
                log.error("예외 처리 message={}", e.getMessage(), e);
            }
        }

        /**
         * Checked 예외를 밖으로 던지는 코드
         * Checked 예외를 잡지 않고 밖으로 던지려면
         * throws 예외를 메서드에 필수로 선언해야 한다.
         * @throws MyCheckedException
         */
        public void callThrow() throws MyCheckedException {
            repository.call();
        }
    }

    static class Repository {
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }

}
