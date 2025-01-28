package com.kijiri.aurora.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PostSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    
    @OneToMany(mappedBy = "postSection", cascade = CascadeType.ALL)
    private List<PostContent> contentList;

    @OneToMany(mappedBy = "postSection", cascade = CascadeType.ALL)
    private List<PostMedia> mediaList;
}
