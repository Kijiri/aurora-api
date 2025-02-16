package com.kijiri.aurora.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PostContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private PostSection postSection;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "grid_row", nullable = false)
    private int row;
    
    @Column(name = "grid_col", nullable = false)
    private int col;
    
    @Column(nullable = false)
    private int length;
    
    @Column(nullable = false)
    private int height;
}
