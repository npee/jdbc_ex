package io.npee.jdbc.repsoitory;

import io.npee.jdbc.domain.Member;
import io.npee.jdbc.repsoitory.ex.MyDbException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * V0 - DriverManager
 * V1 - DataSource, JdbcUtils
 * V2 - ConnectionParam
 * V3 - TransactionManger, DataSourceUtils
 * V4_1 - Throw RuntimeException, Repository Interface
 * V4_2 - SQLException Translator
 */
@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository {

    private final DataSource dataSource;
    private final SQLExceptionTranslator translator;

    public MemberRepositoryV4_2(DataSource dataSource) {
        this.dataSource = dataSource;
        this.translator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            throw translator.translate("save", sql, e);
        } finally {
            close(conn, pstmt, null);
        }
    }

    @Override
    public Member findById(String memberId) {

        String sql = "select * from member where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }
        } catch (SQLException e) {
            throw translator.translate("findById", sql, e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize {}", resultSize);
        } catch (SQLException e) {
            throw translator.translate("update", sql, e);
        } finally {
            close(conn, pstmt, null);
        }
    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw translator.translate("delete", sql, e);
        } finally {
            close(conn, pstmt, null);
        }
    }

    // JDBC Utils??? ???????????? ??????.
    private void close(Connection conn, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        // ???????????? ???????????? ??????????????? DataSourceUtils??? ???????????? ??????.
        DataSourceUtils.releaseConnection(conn, dataSource);
        // JdbcUtils.closeConnection(conn);
    }

    private Connection getConnection() throws SQLException {
        // ???????????? ???????????? ??????????????? DataSourceUtils??? ???????????? ??????.
        Connection conn = DataSourceUtils.getConnection(dataSource);
        // Connection conn = dataSource.getConnection();
        log.info("conn={}, class={}", conn, conn.getClass());
        return conn;
    }
}
