package service;

import domain.Member;
import exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MemberServiceTest {
    private MemberService memberService;

    // Set up a fresh MemberService before each test to ensure test isolation
    @BeforeEach
    void setUp() {
        memberService = new MemberService();
    }

    // Test that a valid member can be added without throwing exceptions
    @Test
    void testAddMember_ValidMember_Success() {
        // Arrange - create a member with all valid data
        Member member = new Member("TEST123", "John", "Doe", "john@test.com", "123456789");

        // Act & Assert - verify no exception is thrown when adding the member
        assertDoesNotThrow(() -> memberService.addMember(member));
    }

    // Test that adding a member with empty ID number throws the correct exception
    @Test
    void testAddMember_EmptyIdNumber_ThrowsException() {
        // Arrange - create a member with empty ID number
        Member member = new Member("", "John", "Doe", "john@test.com", "123456789");

        // Act & Assert - verify the correct exception with correct message is thrown
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            memberService.addMember(member);
        });

        assertEquals("Identification number is required", exception.getMessage());
    }

    // Test that adding a member with empty first name throws the correct exception
    @Test
    void testAddMember_EmptyFirstName_ThrowsException() {
        // Arrange - create a member with empty first name
        Member member = new Member("TEST123", "", "Doe", "john@test.com", "123456789");

        // Act & Assert - verify the correct exception is thrown
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            memberService.addMember(member);
        });

        assertEquals("First name is required", exception.getMessage());
    }

    // Test that adding a member with empty last name throws the correct exception
    @Test
    void testAddMember_EmptyLastName_ThrowsException() {
        // Arrange - create a member with empty last name
        Member member = new Member("TEST123", "John", "", "john@test.com", "123456789");

        // Act & Assert - verify the correct exception is thrown
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            memberService.addMember(member);
        });

        assertEquals("Last name is required", exception.getMessage());
    }

    // Test that adding a member with invalid email format throws the correct exception
    @Test
    void testAddMember_InvalidEmail_ThrowsException() {
        // Arrange - create a member with invalid email (missing @ symbol)
        Member member = new Member("TEST123", "John", "Doe", "invalid-email", "123456789");

        // Act & Assert - verify the correct exception is thrown
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            memberService.addMember(member);
        });

        assertEquals("Email does not have a valid format", exception.getMessage());
    }

    // Test that getAllMembers returns a list (even if empty)
    @Test
    void testGetAllMembers_ReturnsList() {
        // Act - retrieve all members from the system
        var members = memberService.getAllMembers();

        // Assert - verify the result is not null
        assertNotNull(members);
    }

    // Test that getActiveMembers returns a list of active members only
    @Test
    void testGetActiveMembers_ReturnsList() {
        // Act - retrieve only active (not deactivated) members
        var members = memberService.getActiveMembers();

        // Assert - verify the result is not null
        assertNotNull(members);
    }

    // Test that searching for non-existent member by ID number returns empty result
    @Test
    void testGetMemberByIdNumber_NonExistent_ReturnsEmpty() {
        // Act - search for a member that doesn't exist
        var member = memberService.getMemberByIdNumber("NONEXISTENT123");

        // Assert - verify the result is empty (no member found)
        assertTrue(member.isEmpty());
    }

    // Test that checking active status for non-existent member returns false
    @Test
    void testIsMemberActive_NonExistentMember_ReturnsFalse() {
        // Act - check if a non-existent member is active
        boolean active = memberService.isMemberActive(9999); // Non-existent member

        // Assert - should return false since member doesn't exist
        assertFalse(active);
    }

    // Test that member object correctly stores all its properties
    @Test
    void testMemberCreation_ValidData_SetsPropertiesCorrectly() {
        // Arrange & Act - create a member with specific data
        Member member = new Member("TEST123", "Jane", "Smith", "jane@test.com", "987654321");

        // Assert - verify all properties are set correctly
        assertEquals("TEST123", member.getIdNumber());
        assertEquals("Jane", member.getFirstName());
        assertEquals("Smith", member.getLastName());
        assertEquals("jane@test.com", member.getEmail());
        assertEquals("987654321", member.getPhone());
        assertNotNull(member.getMembershipDate()); // Should be set automatically
        assertTrue(member.getActive()); // Should be active by default
    }

    // Test that getFullName method returns correctly formatted full name
    @Test
    void testMemberGetFullName_ReturnsCorrectFormat() {
        // Arrange - create a member with first and last name
        Member member = new Member("TEST123", "John", "Doe", "john@test.com", "123456789");

        // Act - call the getFullName method
        String fullName = member.getFullName();

        // Assert - verify the format is "First Last"
        assertEquals("John Doe", fullName);
    }

    // Test that the member's toString method returns a properly formatted string
    @Test
    void testMemberToString_ReturnsFormattedString() {
        // Arrange - create a member with known data
        Member member = new Member("TEST123", "John", "Doe", "john@test.com", "123456789");

        // Act - call the toString method
        String result = member.toString();

        // Assert - verify the string contains all the important information
        assertTrue(result.contains("ID: TEST123"));
        assertTrue(result.contains("Name: John Doe"));
        assertTrue(result.contains("Active: Yes"));
    }
}