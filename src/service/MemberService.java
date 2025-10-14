package service;

import dao.MemberDAO;
import dao.MemberDAOJDBC;
import domain.Member;
import exceptions.BusinessException;
import java.util.List;
import java.util.Optional;

public class MemberService {
    // Data Access Object for member operations
    private MemberDAO memberDAO;

    // Constructor initializes the MemberDAO implementation
    public MemberService() {
        this.memberDAO = new MemberDAOJDBC();
    }

    // Method to add a new member with validation
    public void addMember(Member member) throws BusinessException {
        validateMember(member); // Validate member data

        // Check if ID number is unique
        if (!memberDAO.isIdNumberUnique(member.getIdNumber())) {
            throw new BusinessException("Identification number already exists in the system: " + member.getIdNumber());
        }

        // Save the member to database
        memberDAO.save(member);
    }

    // Method to retrieve all members
    public List<Member> getAllMembers() {
        return memberDAO.findAll();
    }

    // Method to retrieve only active members
    public List<Member> getActiveMembers() {
        return memberDAO.findActiveMembers();
    }

    // Method to find a member by identification number
    public Optional<Member> getMemberByIdNumber(String idNumber) {
        return memberDAO.findByIdNumber(idNumber);
    }

    // Method to update an existing member with validation
    public void updateMember(Member member) throws BusinessException {
        validateMember(member); // Validate member data

        // Check ID number uniqueness excluding current member
        Optional<Member> existingMember = memberDAO.findByIdNumber(member.getIdNumber());
        if (existingMember.isPresent() && !existingMember.get().getId().equals(member.getId())) {
            throw new BusinessException("Identification number already exists in the system: " + member.getIdNumber());
        }

        // Update the member in database
        memberDAO.update(member);
    }

    // Method to deactivate a member with validation
    public void deactivateMember(Integer id) throws BusinessException {
        // Check if member has active loans
        LoanService loanService = new LoanService();
        int activeLoans = loanService.countActiveLoansByMember(id);

        // Prevent deactivation if member has active loans
        if (activeLoans > 0) {
            throw new BusinessException("Cannot deactivate member because they have " + activeLoans + " active loans");
        }

        // Deactivate the member in database
        memberDAO.deactivateMember(id);
    }

    // Method to check if a member is active
    public boolean isMemberActive(Integer memberId) {
        Optional<Member> member = memberDAO.findById(memberId);
        return member.isPresent() && member.get().getActive();
    }

    // Private method to validate member data
    private void validateMember(Member member) throws BusinessException {
        // Validate ID number is not null or empty
        if (member.getIdNumber() == null || member.getIdNumber().trim().isEmpty()) {
            throw new BusinessException("Identification number is required");
        }

        // Validate first name is not null or empty
        if (member.getFirstName() == null || member.getFirstName().trim().isEmpty()) {
            throw new BusinessException("First name is required");
        }

        // Validate last name is not null or empty
        if (member.getLastName() == null || member.getLastName().trim().isEmpty()) {
            throw new BusinessException("Last name is required");
        }

        // Validate email format if provided
        if (member.getEmail() != null && !member.getEmail().contains("@")) {
            throw new BusinessException("Email does not have a valid format");
        }
    }
}