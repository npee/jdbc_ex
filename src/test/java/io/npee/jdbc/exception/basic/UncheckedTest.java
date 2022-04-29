package io.npee.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UncheckedTest {

    @Test
    void unchecked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_throw() {
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyUncheckedException.class);
    }

    /**
     * RuntimeException을 상속받은 예외는 언체크 예외가 된다.
     */
    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    static class Repository /* throws MyUncheckedException 생략 가능 */{
        public void call() {
            throw new MyUncheckedException("ex");
        }
    }

    /**
     * Unchecked 예외는
     * 예외를 잡거나,
     * 던지지 않아도 된다.
     * 별도로 처리하지 않으면 자동으로 throws 처리된다.
     */
    static class Service {
        Repository repository = new Repository();

        /**
         * 예외 잡기
         */
        public void callCatch() {
            try {
                repository.call();
            } catch (MyUncheckedException e) {
                // 예외 처리 로직
                log.error("예외 처리 message={}", e.getMessage(), e);
            }
        }

        /**
         * 예외 던지기
         * throws 생략 가능
         */
        public void callThrow() {
            repository.call();
        }
    }

}
