package com.hysteria.practice.visual.leaderboard.variables;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import com.hysteria.practice.visual.leaderboard.Leaderboard;
import com.hysteria.practice.utilities.chat.CC;
import lombok.AllArgsConstructor;
import com.hysteria.practice.visual.leaderboard.entry.LeaderboardKitsEntry;

import java.util.List;

@AllArgsConstructor
public class TopKitName implements PlaceholderReplacer {

    public String kit;
    public int pos;

    @Override
    public String update() {
        try {
            List<LeaderboardKitsEntry> test = Leaderboard.getKitLeaderboards().get(kit);
            if(kit == null) {
                return " ";
            }

            if(test == null) {
                return " ";
            }

            if (test.get(pos) == null) {
                return " ";
            }
            LeaderboardKitsEntry profile = test.get(pos);
            return CC.translate(profile.getProfile().getName());
        } catch (IndexOutOfBoundsException e) {
            return " ";
        }
    }
}
