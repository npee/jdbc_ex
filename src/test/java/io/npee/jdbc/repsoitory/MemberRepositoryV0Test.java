package io.npee.jdbc.repsoitory;

import io.npee.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepositoryV0 =new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {

        Member member = new Member("memberV5", 10000);
        memberRepositoryV0.save(member);

        Member findMember = memberRepositoryV0.findById(member.getMemberId());
        log.info("findMember {}", findMember);
        Assertions.assertThat(findMember).isEqualTo(member);

        memberRepositoryV0.update(member.getMemberId(), 15000);
        Member updatedMember = memberRepositoryV0.findById(member.getMemberId());
        log.info("updatedMember {}", updatedMember);
        Assertions.assertThat(updatedMember.getMoney()).isEqualTo(15000);

        memberRepositoryV0.delete(member.getMemberId());
        Assertions.assertThatThrownBy(() -> memberRepositoryV0.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

    }
}