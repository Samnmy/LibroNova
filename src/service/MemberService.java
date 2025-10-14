package service;

import dao.MemberDAO;
import dao.MemberDAOJDBC;
import domain.Member;
import exceptions.BusinessException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class MemberService {
    private MemberDAO memberDAO;
    private static final Logger logger = Logger.getLogger(MemberService.class.getName());

    // Constructor - initializes the MemberService with database access
    public MemberService() {
        this.memberDAO = new MemberDAOJDBC();
        logger.info("MemberService initialized");
    }

    // Adds a new member to the system after validation
    public void addMember(Member member) throws BusinessException {
        logger.info("Attempting to add new member: " + member.getIdNumber());

        validateMember(member);

        // Check if ID number is unique in the system
        if (!memberDAO.isIdNumberUnique(member.getIdNumber())) {
            logger.warning("ID number already exists: " + member.getIdNumber());
            throw new BusinessException("Identification number already exists in the system: " + member.getIdNumber());
        }

        memberDAO.save(member);
        logger.info("Member added successfully: " + member.getIdNumber());
    }

    // Retrieves all members from the database
    public List<Member> getAllMembers() {
        logger.info("Retrieving all members");
        List<Member> members = memberDAO.findAll();
        logger.info("Retrieved " + members.size() + " members");
        return members;
    }

    // Gets only active members (not deactivated)
    public List<Member> getActiveMembers() {
        logger.info("Retrieving active members");
        List<Member> members = memberDAO.findActiveMembers();
        logger.info("Retrieved " + members.size() + " active members");
        return members;
    }

    // Finds a member by their unique identification number
    public Optional<Member> getMemberByIdNumber(String idNumber) {
        logger.info("Searching for member by ID number: " + idNumber);
        return memberDAO.findByIdNumber(idNumber);
    }

    // Updates an existing member's information
    public void updateMember(Member member) throws BusinessException {
        logger.info("Attempting to update member ID: " + member.getId());

        validateMember(member);

        // Check for ID number conflicts with other members
        Optional<Member> existingMember = memberDAO.findByIdNumber(member.getIdNumber());
        if (existingMember.isPresent() && !existingMember.get().getId().equals(member.getId())) {
            logger.warning("ID number conflict during update: " + member.getIdNumber());
            throw new BusinessException("Identification number already exists in the system: " + member.getIdNumber());
        }

        memberDAO.update(member);
        logger.info("Member updated successfully: " + member.getIdNumber());
    }

    // Deactivates a member (soft delete) if they have no active loans
    public void deactivateMember(Integer id) throws BusinessException {
        logger.info("Attempting to deactivate member ID: " + id);

        // Check if member has any active loans before deactivation
        LoanService loanService = new LoanService();
        int activeLoans = loanService.countActiveLoansByMember(id);

        if (activeLoans > 0) {
            logger.warning("Cannot deactivate member with active loans: " + id + " (loans: " + activeLoans + ")");
            throw new BusinessException("Cannot deactivate member because they have " + activeLoans + " active loans");
        }

        memberDAO.deactivateMember(id);
        logger.info("Member deactivated successfully: " + id);
    }

    // Checks if a member is currently active
    public boolean isMemberActive(Integer memberId) {
        logger.fine("Checking member active status - Member ID: " + memberId);
        Optional<Member> member = memberDAO.findById(memberId);
        boolean active = member.isPresent() && member.get().getActive();
        logger.fine("Member ID " + memberId + " active: " + active);
        return active;
    }

    // Validates member data meets all business rules
    private void validateMember(Member member) throws BusinessException {
        logger.fine("Validating member data");

        // Check required fields are not empty
        if (member.getIdNumber() == null || member.getIdNumber().trim().isEmpty()) {
            logger.warning("Member validation failed: ID number is required");
            throw new BusinessException("Identification number is required");
        }

        if (member.getFirstName() == null || member.getFirstName().trim().isEmpty()) {
            logger.warning("Member validation failed: First name is required");
            throw new BusinessException("First name is required");
        }

        if (member.getLastName() == null || member.getLastName().trim().isEmpty()) {
            logger.warning("Member validation failed: Last name is required");
            throw new BusinessException("Last name is required");
        }

        // Validate email format if provided
        if (member.getEmail() != null && !member.getEmail().contains("@")) {
            logger.warning("Member validation failed: Invalid email format");
            throw new BusinessException("Email does not have a valid format");
        }

        logger.fine("Member validation passed");
    }
}