// package kalo.main.repository;

// import java.util.List;

// import org.springframework.data.domain.Pageable;
// import org.springframework.data.jpa.repository.JpaRepository;

// import kalo.main.domain.Notis;

// public interface NotisRepository extends JpaRepository<Notis, Long> {
//     List<Notis> findNotisByReceiverIdAndDisplayAndDeleted(Pageable pageable, Long receiverId, Boolean deleted, Boolean display);
//     Long countNotisByReceiverIdAndDeletedAndDisplayAndCheck(Long receiverId, Boolean deleted, Boolean display, Boolean check);
// }
