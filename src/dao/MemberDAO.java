package dao;

import domain.Member;
import java.util.List;
import java.util.Optional;

public interface MemberDAO {
    // Saves a new member to the database
    void save(Member member);
    // Finds a member by their unique identifier, returns Optional to handle null cases
    Optional<Member> findById(Integer id);
    // Finds a member by their ID number, returns Optional to handle null cases
    Optional<Member> findByIdNumber(String idNumber);
    // Retrieves all members from the database
    List<Member> findAll();
    // Retrieves only active members (members with active status)
    List<Member> findActiveMembers();
    // Updates an existing member in the database
    void update(Member member);
    // Deactivates a member (soft delete by setting active status to false)
    void deactivateMember(Integer id);
    // Checks if an ID number is unique in the database (for validation)
    boolean isIdNumberUnique(String idNumber);
}