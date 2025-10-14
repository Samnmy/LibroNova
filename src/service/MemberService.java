package service;

import dao.MemberDAO;
import dao.MemberDAOJDBC;
import domain.Member;
import exceptions.BusinessException;
import java.util.List;
import java.util.Optional;

public class MemberService {
    private MemberDAO memberDAO;

    public MemberService() {
        this.memberDAO = new MemberDAOJDBC();
    }

    public void addMember(Member member) throws BusinessException {
        validateMember(member);

        if (!memberDAO.isIdNumberUnique(member.getIdNumber())) {
            throw new BusinessException("El número de identificación ya existe en el sistema: " + member.getIdNumber());
        }

        memberDAO.save(member);
    }

    public List<Member> getAllMembers() {
        return memberDAO.findAll();
    }

    public List<Member> getActiveMembers() {
        return memberDAO.findActiveMembers();
    }

    public Optional<Member> getMemberByIdNumber(String idNumber) {
        return memberDAO.findByIdNumber(idNumber);
    }

    public void updateMember(Member member) throws BusinessException {
        validateMember(member);

        Optional<Member> existingMember = memberDAO.findByIdNumber(member.getIdNumber());
        if (existingMember.isPresent() && !existingMember.get().getId().equals(member.getId())) {
            throw new BusinessException("El número de identificación ya existe en el sistema: " + member.getIdNumber());
        }

        memberDAO.update(member);
    }

    public void deactivateMember(Integer id) throws BusinessException {
        // Check if member has active loans
        LoanService loanService = new LoanService();
        int activeLoans = loanService.countActiveLoansByMember(id);

        if (activeLoans > 0) {
            throw new BusinessException("No se puede desactivar el socio porque tiene " + activeLoans + " préstamos activos");
        }

        memberDAO.deactivateMember(id);
    }

    public boolean isMemberActive(Integer memberId) {
        Optional<Member> member = memberDAO.findById(memberId);
        return member.isPresent() && member.get().getActive();
    }

    private void validateMember(Member member) throws BusinessException {
        if (member.getIdNumber() == null || member.getIdNumber().trim().isEmpty()) {
            throw new BusinessException("El número de identificación es obligatorio");
        }

        if (member.getFirstName() == null || member.getFirstName().trim().isEmpty()) {
            throw new BusinessException("El nombre es obligatorio");
        }

        if (member.getLastName() == null || member.getLastName().trim().isEmpty()) {
            throw new BusinessException("El apellido es obligatorio");
        }

        if (member.getEmail() != null && !member.getEmail().contains("@")) {
            throw new BusinessException("El email no tiene un formato válido");
        }
    }
}