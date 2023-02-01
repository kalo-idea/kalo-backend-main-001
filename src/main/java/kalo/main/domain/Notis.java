// package kalo.main.domain;

// import javax.persistence.Column;
// import javax.persistence.Entity;
// import javax.persistence.FetchType;
// import javax.persistence.GeneratedValue;
// import javax.persistence.GenerationType;
// import javax.persistence.Id;
// import javax.persistence.JoinColumn;
// import javax.persistence.ManyToOne;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;

// @Entity
// @Getter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class Notis extends BaseEntity {
    
//     @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//     Long id;

//     String image;

//     String title;

//     String content;

//     @Column(columnDefinition = "TINYINT", length = 1)
//     Boolean check;

//     @Column(columnDefinition = "TINYINT", length = 1)
//     Boolean display;
    
//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name="sender_id")
//     User sender;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name="receiver_id")
//     User receiver;

//     Long targetId;

//     String targetUrl;

//     String target;
// }
