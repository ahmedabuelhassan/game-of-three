package com.takeaway.gameofthree.model;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GAME", schema = "GAME_OF_THREE")
public class Game extends AuditModel {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    @Column(name = "TURN")
    private long turn;

    @Column(name = "CURRENT_NUMBER")
    private int currentNumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "game",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private Set<Player> players;

    // for handling the bi-directional relation.
    public void addPlayer(Player player) {
        if (players == null) {
            players = new LinkedHashSet<>();
        }
        players.add(player);
        player.setGame(this);
    }
}
