package rip.crystal.practice.visual.leaderboard.variables;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import rip.crystal.practice.utilities.chat.CC;
import rip.crystal.practice.visual.leaderboard.Leaderboard;
import lombok.AllArgsConstructor;
import rip.crystal.practice.visual.leaderboard.entry.LeaderboardKitsEntry;

import java.util.List;

@AllArgsConstructor
public class TopKitElo implements PlaceholderReplacer {

    public String kit;
    public int pos;

    @Override
    public String update() {
        try {
            List<LeaderboardKitsEntry> kitsEntryList = Leaderboard.getKitLeaderboards().get(kit);
            if(kit == null) {
                return " ";
            }

            if(kitsEntryList == null) {
                return " ";
            }

            if (kitsEntryList.get(pos) == null) {
                return " ";
            }
            int profile = kitsEntryList.get(pos).getElo();
            return CC.translate(String.valueOf(profile));
        } catch (IndexOutOfBoundsException e) {
            return " ";
        }
    }
}
