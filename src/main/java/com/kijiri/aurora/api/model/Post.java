package com.kijiri.aurora.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "idx_post_created_at", columnList = "createdAt"),
        @Index(name = "idx_post_user", columnList = "user_id"),
        @Index(name = "idx_post_status", columnList = "publishStatus")
})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostContent> contentList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostMedia> mediaList;
    
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "post")
    private Set<PostCategory> postCategories;

    @OneToMany(mappedBy = "post")
    private Set<PostTag> postTags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikes;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PublishStatus publishStatus;
    
    private Long likeCount = 0L;
    private Long commentCount = 0L;
    private boolean edited;
    
    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(insertable = false)
    @UpdateTimestamp
    private Instant updatedAt;
}
