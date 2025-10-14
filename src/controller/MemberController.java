package controller;

import domain.Member;
import exceptions.BusinessException;
import service.MemberService;
import view.MemberView;
import java.util.List;

public class MemberController {
    // Service layer dependency for member business logic operations
    private MemberService memberService;
    // View layer dependency for member-related user interface interactions
    private MemberView memberView;

    // Constructor initializes member service and view dependencies
    public MemberController() {
        this.memberService = new MemberService();
        this.memberView = new MemberView();
    }

    // Method to handle adding a new member
    public void addMember() {
        try {
            // Get member data from view layer
            Member member = memberView.showAddMemberForm();
            // Call service to add member to database
            memberService.addMember(member);
            // Show success message to user
            memberView.showSuccessMessage("Member added successfully");
        } catch (BusinessException e) {
            // Handle business rule violations (e.g., duplicate ID number, invalid data)
            memberView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            memberView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }

    // Method to retrieve and display all members
    public void showAllMembers() {
        // Get all members from service layer (both active and inactive)
        List<Member> members = memberService.getAllMembers();
        // Display members through view layer
        memberView.displayMembers(members);
    }

    // Method to retrieve and display only active members
    public void showActiveMembers() {
        // Get active members from service layer (members with active status)
        List<Member> members = memberService.getActiveMembers();
        // Display active members through view layer
        memberView.displayMembers(members);
    }

    // Method to handle updating an existing member
    public void updateMember() {
        try {
            // Get ID number from view layer to identify member
            String idNumber = memberView.askForIdNumber();
            // Retrieve member from service layer, throw exception if not found
            Member member = memberService.getMemberByIdNumber(idNumber)
                    .orElseThrow(() -> new BusinessException("Member not found"));

            // Get updated member data from view layer
            Member updatedMember = memberView.showUpdateMemberForm(member);
            // Call service to update member in database
            memberService.updateMember(updatedMember);
            // Show success message to user
            memberView.showSuccessMessage("Member updated successfully");

        } catch (BusinessException e) {
            // Handle business rule violations
            memberView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            memberView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }

    // Method to handle deactivating a member (soft delete)
    public void deactivateMember() {
        try {
            // Get ID number from view layer to identify member
            String idNumber = memberView.askForIdNumber();
            // Retrieve member from service layer, throw exception if not found
            Member member = memberService.getMemberByIdNumber(idNumber)
                    .orElseThrow(() -> new BusinessException("Member not found"));

            // Confirm deactivation with user through view layer
            if (memberView.confirmDeactivation(member)) {
                // Call service to deactivate member in database
                memberService.deactivateMember(member.getId());
                // Show success message to user
                memberView.showSuccessMessage("Member deactivated successfully");
            }

        } catch (BusinessException e) {
            // Handle business rule violations
            memberView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            memberView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }
}