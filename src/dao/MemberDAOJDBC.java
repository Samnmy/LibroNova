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
        // SQL query to insert a new member record
        String sql = "INSERT INTO members (id_number, first_name, last_name, email, phone, membership_date, active) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters for the prepared statement
            stmt.setString(1, member.getIdNumber());
            stmt.setString(2, member.getFirstName());
            stmt.setString(3, member.getLastName());
            stmt.setString(4, member.getEmail());
            stmt.setString(5, member.getPhone());
            stmt.setDate(6, Date.valueOf(member.getMembershipDate()));
            stmt.setBoolean(7, member.getActive());

            // Execute the insert operation
            stmt.executeUpdate();

            // Retrieve the auto-generated member ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    member.setId(generatedKeys.getInt(1));
                }
            }

            // Print success message to console
            System.out.println("Member saved: " + member.getIdNumber());

        } catch (SQLException e) {
            // Print error message to console
            System.err.println("Error saving member: " + e.getMessage());
            throw new RuntimeException("Error saving member", e);
        }
    }

    @Override
    public Optional<Member> findById(Integer id) {
        // SQL query to find member by primary key
        String sql = "SELECT * FROM members WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the ID parameter
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            // If member found, map result set to Member object
            if (rs.next()) {
                return Optional.of(mapResultSetToMember(rs));
            }

        } catch (SQLException e) {
            // Print error message to console
            System.err.println("Error finding member by id: " + id + " - " + e.getMessage());
        }

        // Return empty Optional if no member found
        return Optional.empty();
    }

    @Override
    public Optional<Member> findByIdNumber(String idNumber) {
        // SQL query to find member by unique ID number
        String sql = "SELECT * FROM members WHERE id_number = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the ID number parameter
            stmt.setString(1, idNumber);
            ResultSet rs = stmt.executeQuery();

            // If member found, map result set to Member object
            if (rs.next()) {
                return Optional.of(mapResultSetToMember(rs));
            }

        } catch (SQLException e) {
            // Print error message to console
            System.err.println("Error finding member by ID number: " + idNumber + " - " + e.getMessage());
        }

        // Return empty Optional if no member found
        return Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        // Initialize list to store all members
        List<Member> members = new ArrayList<>();
        // SQL query to retrieve all members ordered by name
        String sql = "SELECT * FROM members ORDER BY first_name, last_name";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Iterate through all results and map to Member objects
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }

        } catch (SQLException e) {
            // Print error message to console
            System.err.println("Error finding all members: " + e.getMessage());
        }

        // Return list of members (empty list if none found)
        return members;
    }

    @Override
    public List<Member> findActiveMembers() {
        // Initialize list to store active members
        List<Member> members = new ArrayList<>();
        // SQL query to retrieve only active members
        String sql = "SELECT * FROM members WHERE active = TRUE ORDER BY first_name, last_name";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Iterate through results and map to Member objects
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }

        } catch (SQLException e) {
            // Print error message to console
            System.err.println("Error finding active members: " + e.getMessage());
        }

        // Return active members
        return members;
    }

    @Override
    public void update(Member member) {
        // SQL query to update existing member record
        String sql = "UPDATE members SET id_number = ?, first_name = ?, last_name = ?, email = ?, phone = ?, active = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set all update parameters
            stmt.setString(1, member.getIdNumber());
            stmt.setString(2, member.getFirstName());
            stmt.setString(3, member.getLastName());
            stmt.setString(4, member.getEmail());
            stmt.setString(5, member.getPhone());
            stmt.setBoolean(6, member.getActive());
            stmt.setInt(7, member.getId());

            // Execute the update operation
            stmt.executeUpdate();
            // Print success message to console
            System.out.println("Member updated: " + member.getIdNumber());

        } catch (SQLException e) {
            // Print error message to console
            System.err.println("Error updating member: " + e.getMessage());
            throw new RuntimeException("Error updating member", e);
        }
    }

    @Override
    public void deactivateMember(Integer id) {
        // SQL query to soft-delete member by setting active to false
        String sql = "UPDATE members SET active = FALSE WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the ID parameter
            stmt.setInt(1, id);
            // Execute the update operation
            stmt.executeUpdate();
            // Print success message to console
            System.out.println("Member deactivated: " + id);

        } catch (SQLException e) {
            // Print error message to console
            System.err.println("Error deactivating member: " + id + " - " + e.getMessage());
            throw new RuntimeException("Error deactivating member", e);
        }
    }

    @Override
    public boolean isIdNumberUnique(String idNumber) {
        // SQL query to check if ID number already exists
        String sql = "SELECT COUNT(*) FROM members WHERE id_number = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the ID number parameter
            stmt.setString(1, idNumber);
            ResultSet rs = stmt.executeQuery();

            // Return true if count is 0 (ID number is unique)
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (SQLException e) {
            // Print error message to console
            System.err.println("Error checking ID number uniqueness: " + idNumber + " - " + e.getMessage());
        }

        // Return false in case of error or non-unique ID number
        return false;
    }

    // Helper method to map ResultSet row to Member object
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