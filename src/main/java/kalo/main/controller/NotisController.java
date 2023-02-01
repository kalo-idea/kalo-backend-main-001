// package kalo.main.controller;

// import java.util.List;

// import org.springframework.data.domain.Sort;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.web.PageableDefault;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;

// import kalo.main.domain.Notis;
// import kalo.main.service.NotisService;
// import lombok.RequiredArgsConstructor;

// @RestController
// @RequiredArgsConstructor
// public class NotisController {
//     private final NotisService notisService;

//     @GetMapping("/getMyNotis")
//     public List<Notis> getMyNotis(@PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, Long userId) {
//         return notisService.getMyNotis(pageable, userId);
//     }

//     @GetMapping("/countMyNotis")
//     public Long countMyNotis(Long userId) {
//         return notisService.checkMyNotis(userId);
//     }
// }
