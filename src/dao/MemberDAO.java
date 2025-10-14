package dao;

import domain.Member;
import java.util.List;
import java.util.Optional;

public interface MemberDAO {
    void save(Member member);
    Optional<Member> findById(Integer id);
    Optional<Member> findByIdNumber(String idNumber);
    List<Member> findAll();
    List<Member> findActiveMembers();
    void update(Member member);
    void deactivateMember(Integer id);
    boolean isIdNumberUnique(String idNumber);
}