package view;

import domain.Member;
import javax.swing.JOptionPane;
import java.util.List;

public class MemberView {

    // Method to display form for adding a new member
    public Member showAddMemberForm() {
        // Show input dialogs to get member information from user
        String idNumber = JOptionPane.showInputDialog("Enter the identification number:");
        String firstName = JOptionPane.showInputDialog("Enter the first name:");
        String lastName = JOptionPane.showInputDialog("Enter the last name:");
        String email = JOptionPane.showInputDialog("Enter the email:");
        String phone = JOptionPane.showInputDialog("Enter the phone number:");

        // Create and return new Member object with collected data
        return new Member(idNumber, firstName, lastName, email, phone);
    }

    // Method to display form for updating an existing member
    public Member showUpdateMemberForm(Member member) {
        // Show input dialogs with current values as defaults
        String firstName = JOptionPane.showInputDialog("Enter the new first name:", member.getFirstName());
        String lastName = JOptionPane.showInputDialog("Enter the new last name:", member.getLastName());
        String email = JOptionPane.showInputDialog("Enter the new email:", member.getEmail());
        String phone = JOptionPane.showInputDialog("Enter the new phone number:", member.getPhone());

        // Update member object with new values
        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setEmail(email);
        member.setPhone(phone);

        // Return updated member object
        return member;
    }

    // Method to ask user for identification number input
    public String askForIdNumber() {
        return JOptionPane.showInputDialog("Enter the member identification number:");
    }

    // Method to display list of members in a dialog
    public void displayMembers(List<Member> members) {
        // Check if members list is empty
        if (members.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No members found.");
            return;
        }

        // Build formatted string with all members information
        StringBuilder sb = new StringBuilder();
        sb.append("=== MEMBER LIST ===\n\n");

        for (Member member : members) {
            sb.append("ID: ").append(member.getIdNumber()).append("\n");
            sb.append("Name: ").append(member.getFullName()).append("\n");
            sb.append("Email: ").append(member.getEmail()).append("\n");
            sb.append("Phone: ").append(member.getPhone()).append("\n");
            sb.append("Active: ").append(member.getActive() ? "Yes" : "No").append("\n");
            sb.append("Membership Date: ").append(member.getMembershipDate()).append("\n");
            sb.append("------------------------\n");
        }

        // Display members information in dialog
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    // Method to confirm member deactivation with user
    public boolean confirmDeactivation(Member member) {
        int response = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to deactivate the member?\n" +
                        "Name: " + member.getFullName() + "\n" +
                        "ID: " + member.getIdNumber(),
                "Confirm Deactivation",
                JOptionPane.YES_NO_OPTION);

        // Return true if user confirms deactivation
        return response == JOptionPane.YES_OPTION;
    }

    // Method to show success message dialog
    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to show error message dialog
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}