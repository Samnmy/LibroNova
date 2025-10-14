package controller;

import domain.Member;
import exceptions.BusinessException;
import service.MemberService;
import util.CSVExportUtil;
import view.MemberView;
import java.util.List;

public class MemberController {
    // Handles business logic for member operations
    private MemberService memberService;
    // Handles user interface for member-related screens
    private MemberView memberView;

    // Constructor - creates new instances of service and view
    public MemberController() {
        this.memberService = new MemberService();
        this.memberView = new MemberView();
    }

    // Handles the complete process of adding a new member
    public void addMember() {
        try {
            // Get member information from user input
            Member member = memberView.showAddMemberForm();
            // Save member to database through service layer
            memberService.addMember(member);
            // Show confirmation message to user
            memberView.showSuccessMessage("Member added successfully");
        } catch (BusinessException e) {
            // Show specific error message for business rule violations
            memberView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Show generic error message for unexpected problems
            memberView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }

    // Retrieves and displays all members in the system
    public void showAllMembers() {
        // Get complete list of members from database
        List<Member> members = memberService.getAllMembers();
        // Display the list to the user
        memberView.displayMembers(members);
    }

    // Retrieves and displays only active members
    public void showActiveMembers() {
        // Get list of active members from database
        List<Member> members = memberService.getActiveMembers();
        // Display active members to user
        memberView.displayMembers(members);
    }

    // Handles updating an existing member's information
    public void updateMember() {
        try {
            // Get ID number to identify which member to update
            String idNumber = memberView.askForIdNumber();
            // Find the member in database, throw error if not found
            Member member = memberService.getMemberByIdNumber(idNumber)
                    .orElseThrow(() -> new BusinessException("Member not found"));

            // Get updated information from user
            Member updatedMember = memberView.showUpdateMemberForm(member);
            // Save changes to database
            memberService.updateMember(updatedMember);
            // Confirm successful update to user
            memberView.showSuccessMessage("Member updated successfully");

        } catch (BusinessException e) {
            // Show specific error message
            memberView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Show generic error message
            memberView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }

    // Exports all members to a CSV file for reporting
    public void exportMembersToCSV() {
        try {
            // Get all members from database
            List<Member> members = memberService.getAllMembers();
            // Export to CSV file with automatic filename
            String filename = CSVExportUtil.exportMembersToCSV(members);
            // Confirm export success to user
            memberView.showSuccessMessage("Members exported successfully to: " + filename);
        } catch (Exception e) {
            // Show error if export fails
            memberView.showErrorMessage("Error exporting members: " + e.getMessage());
        }
    }

    // Handles deactivating a member (soft delete - keeps record but marks as inactive)
    public void deactivateMember() {
        try {
            // Get ID number to identify which member to deactivate
            String idNumber = memberView.askForIdNumber();
            // Find the member in database, throw error if not found
            Member member = memberService.getMemberByIdNumber(idNumber)
                    .orElseThrow(() -> new BusinessException("Member not found"));

            // Ask user to confirm deactivation
            if (memberView.confirmDeactivation(member)) {
                // Deactivate member in database (soft delete)
                memberService.deactivateMember(member.getId());
                // Confirm successful deactivation
                memberView.showSuccessMessage("Member deactivated successfully");
            }

        } catch (BusinessException e) {
            // Show specific error message (e.g., member has active loans)
            memberView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            // Show generic error message
            memberView.showErrorMessage("Unexpected error: " + e.getMessage());
        }
    }
}