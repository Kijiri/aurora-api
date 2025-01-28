package com.kijiri.aurora.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PostMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_section_id", nullable = false)
    private PostSection postSection;

    @ManyToOne
    @JoinColumn(name = "media_id", nullable = false)
    private Media media;
    
    @Column(name = "grid_row", nullable = false)
    private int row;

    @Column(name = "grid_col", nullable = false)
    private int col;

    @Column(nullable = false)
    private int length;

    @Column(nullable = false)
    private int height;
}
