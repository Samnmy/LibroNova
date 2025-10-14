package controller;

import domain.Member;
import exceptions.BusinessException;
import service.MemberService;
import view.MemberView;
import java.util.List;

public class MemberController {
    private MemberService memberService;
    private MemberView memberView;

    public MemberController() {
        this.memberService = new MemberService();
        this.memberView = new MemberView();
    }

    public void addMember() {
        try {
            Member member = memberView.showAddMemberForm();
            memberService.addMember(member);
            memberView.showSuccessMessage("Socio agregado exitosamente");
        } catch (BusinessException e) {
            memberView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            memberView.showErrorMessage("Error inesperado: " + e.getMessage());
        }
    }

    public void showAllMembers() {
        List<Member> members = memberService.getAllMembers();
        memberView.displayMembers(members);
    }

    public void showActiveMembers() {
        List<Member> members = memberService.getActiveMembers();
        memberView.displayMembers(members);
    }

    public void updateMember() {
        try {
            String idNumber = memberView.askForIdNumber();
            Member member = memberService.getMemberByIdNumber(idNumber)
                    .orElseThrow(() -> new BusinessException("Socio no encontrado"));

            Member updatedMember = memberView.showUpdateMemberForm(member);
            memberService.updateMember(updatedMember);
            memberView.showSuccessMessage("Socio actualizado exitosamente");

        } catch (BusinessException e) {
            memberView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            memberView.showErrorMessage("Error inesperado: " + e.getMessage());
        }
    }

    public void deactivateMember() {
        try {
            String idNumber = memberView.askForIdNumber();
            Member member = memberService.getMemberByIdNumber(idNumber)
                    .orElseThrow(() -> new BusinessException("Socio no encontrado"));

            if (memberView.confirmDeactivation(member)) {
                memberService.deactivateMember(member.getId());
                memberView.showSuccessMessage("Socio desactivado exitosamente");
            }

        } catch (BusinessException e) {
            memberView.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            memberView.showErrorMessage("Error inesperado: " + e.getMessage());
        }
    }
}