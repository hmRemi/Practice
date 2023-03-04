package com.hysteria.practice.visual.leaderboard.entry;

import com.hysteria.practice.player.profile.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeaderboardKitsEntry {

    private final Profile profile;
    private final int elo;

}
