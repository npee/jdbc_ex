package io.npee.jdbc.repsoitory;

import io.npee.jdbc.connection.ConnectionConst;
import io.npee.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.net.URL;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static io.npee.jdbc.connection.ConnectionConst.*;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 memberRepositoryV1;

    @BeforeEach
    void beforeEach() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepositoryV1 = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException {

        Member member = new Member("memberV5", 10000);
        memberRepositoryV1.save(member);

        Member findMember = memberRepositoryV1.findById(member.getMemberId());
        log.info("findMember {}", findMember);
        Assertions.assertThat(findMember).isEqualTo(member);

        memberRepositoryV1.update(member.getMemberId(), 15000);
        Member updatedMember = memberRepositoryV1.findById(member.getMemberId());
        log.info("updatedMember {}", updatedMember);
        Assertions.assertThat(updatedMember.getMoney()).isEqualTo(15000);

        memberRepositoryV1.delete(member.getMemberId());
        Assertions.assertThatThrownBy(() -> memberRepositoryV1.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

    }
}