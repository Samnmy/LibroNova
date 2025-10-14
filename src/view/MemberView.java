package view;

import domain.Member;
import javax.swing.JOptionPane;
import java.util.List;

public class MemberView {

    public Member showAddMemberForm() {
        String idNumber = JOptionPane.showInputDialog("Ingrese el número de identificación:");
        String firstName = JOptionPane.showInputDialog("Ingrese el nombre:");
        String lastName = JOptionPane.showInputDialog("Ingrese el apellido:");
        String email = JOptionPane.showInputDialog("Ingrese el email:");
        String phone = JOptionPane.showInputDialog("Ingrese el teléfono:");

        return new Member(idNumber, firstName, lastName, email, phone);
    }

    public Member showUpdateMemberForm(Member member) {
        String firstName = JOptionPane.showInputDialog("Ingrese el nuevo nombre:", member.getFirstName());
        String lastName = JOptionPane.showInputDialog("Ingrese el nuevo apellido:", member.getLastName());
        String email = JOptionPane.showInputDialog("Ingrese el nuevo email:", member.getEmail());
        String phone = JOptionPane.showInputDialog("Ingrese el nuevo teléfono:", member.getPhone());

        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setEmail(email);
        member.setPhone(phone);

        return member;
    }

    public String askForIdNumber() {
        return JOptionPane.showInputDialog("Ingrese el número de identificación del socio:");
    }

    public void displayMembers(List<Member> members) {
        if (members.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron socios.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== LISTA DE SOCIOS ===\n\n");

        for (Member member : members) {
            sb.append("ID: ").append(member.getIdNumber()).append("\n");
            sb.append("Nombre: ").append(member.getFullName()).append("\n");
            sb.append("Email: ").append(member.getEmail()).append("\n");
            sb.append("Teléfono: ").append(member.getPhone()).append("\n");
            sb.append("Activo: ").append(member.getActive() ? "Sí" : "No").append("\n");
            sb.append("Fecha de membresía: ").append(member.getMembershipDate()).append("\n");
            sb.append("------------------------\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public boolean confirmDeactivation(Member member) {
        int response = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de desactivar al socio?\n" +
                        "Nombre: " + member.getFullName() + "\n" +
                        "ID: " + member.getIdNumber(),
                "Confirmar Desactivación",
                JOptionPane.YES_NO_OPTION);

        return response == JOptionPane.YES_OPTION;
    }

    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}