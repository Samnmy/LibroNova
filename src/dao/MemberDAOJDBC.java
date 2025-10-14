package dao;

import config.DatabaseConfig;
import domain.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberDAOJDBC implements MemberDAO {

    @Override
    public void save(Member member) {
        String sql = "INSERT INTO members (id_number, first_name, last_name, email, phone, membership_date, active) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, member.getIdNumber());
            stmt.setString(2, member.getFirstName());
            stmt.setString(3, member.getLastName());
            stmt.setString(4, member.getEmail());
            stmt.setString(5, member.getPhone());
            stmt.setDate(6, Date.valueOf(member.getMembershipDate()));
            stmt.setBoolean(7, member.getActive());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    member.setId(generatedKeys.getInt(1));
                }
            }

            System.out.println("Member saved: " + member.getIdNumber()); // ✅ Simplificado

        } catch (SQLException e) {
            System.err.println("Error saving member: " + e.getMessage()); // ✅ Simplificado
            throw new RuntimeException("Error saving member", e);
        }
    }

    @Override
    public Optional<Member> findById(Integer id) {
        String sql = "SELECT * FROM members WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToMember(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding member by id: " + id + " - " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Member> findByIdNumber(String idNumber) {
        String sql = "SELECT * FROM members WHERE id_number = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToMember(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding member by ID number: " + idNumber + " - " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY first_name, last_name";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding all members: " + e.getMessage());
        }

        return members;
    }

    @Override
    public List<Member> findActiveMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE active = TRUE ORDER BY first_name, last_name";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding active members: " + e.getMessage());
        }

        return members;
    }

    @Override
    public void update(Member member) {
        String sql = "UPDATE members SET id_number = ?, first_name = ?, last_name = ?, email = ?, phone = ?, active = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getIdNumber());
            stmt.setString(2, member.getFirstName());
            stmt.setString(3, member.getLastName());
            stmt.setString(4, member.getEmail());
            stmt.setString(5, member.getPhone());
            stmt.setBoolean(6, member.getActive());
            stmt.setInt(7, member.getId());

            stmt.executeUpdate();
            System.out.println("Member updated: " + member.getIdNumber()); // ✅ Simplificado

        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage()); // ✅ Simplificado
            throw new RuntimeException("Error updating member", e);
        }
    }

    @Override
    public void deactivateMember(Integer id) {
        String sql = "UPDATE members SET active = FALSE WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Member deactivated: " + id); // ✅ Simplificado

        } catch (SQLException e) {
            System.err.println("Error deactivating member: " + id + " - " + e.getMessage()); // ✅ Simplificado
            throw new RuntimeException("Error deactivating member", e);
        }
    }

    @Override
    public boolean isIdNumberUnique(String idNumber) {
        String sql = "SELECT COUNT(*) FROM members WHERE id_number = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (SQLException e) {
            System.err.println("Error checking ID number uniqueness: " + idNumber + " - " + e.getMessage());
        }

        return false;
    }

    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getInt("id"));
        member.setIdNumber(rs.getString("id_number"));
        member.setFirstName(rs.getString("first_name"));
        member.setLastName(rs.getString("last_name"));
        member.setEmail(rs.getString("email"));
        member.setPhone(rs.getString("phone"));
        member.setMembershipDate(rs.getDate("membership_date").toLocalDate());
        member.setActive(rs.getBoolean("active"));
        member.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return member;
    }
}