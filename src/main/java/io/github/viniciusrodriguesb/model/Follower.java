package io.github.viniciusrodriguesb.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "followers")
@Entity
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "followers_id")
    private User followerId;
}
