package io.npee.jdbc.repsoitory;

import io.npee.jdbc.domain.Member;

public interface MemberRepository {

    Member save(Member member);
    Member findById(String memberId);
    void update(String memberId, int money);
    void delete(String memberId);
}
