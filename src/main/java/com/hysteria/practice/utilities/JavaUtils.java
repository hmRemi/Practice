package com.hysteria.practice.utilities;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.hysteria.practice.HyPractice;
import com.hysteria.practice.essentials.EssentialsListener;
import com.hysteria.practice.essentials.chat.impl.ChatListener;
import com.hysteria.practice.game.arena.ArenaListener;
import com.hysteria.practice.game.event.game.EventGameListener;
import com.hysteria.practice.game.ffa.FFAListener;
import com.hysteria.practice.game.kit.KitEditorListener;
import com.hysteria.practice.game.tournament.listeners.TournamentListener;
import com.hysteria.practice.match.listeners.MatchListener;
import com.hysteria.practice.match.listeners.impl.MatchBuildListener;
import com.hysteria.practice.match.listeners.impl.MatchPearlListener;
import com.hysteria.practice.match.listeners.impl.MatchPlayerListener;
import com.hysteria.practice.match.listeners.impl.MatchSpecialListener;
import com.hysteria.practice.player.clan.ClanListener;
import com.hysteria.practice.player.party.classes.archer.ArcherClass;
import com.hysteria.practice.player.party.classes.bard.BardListener;
import com.hysteria.practice.player.party.classes.rogue.RogueClass;
import com.hysteria.practice.player.party.listeners.PartyListener;
import com.hysteria.practice.player.profile.ProfileListener;
import com.hysteria.practice.player.queue.QueueListener;
import com.hysteria.practice.utilities.chat.CC;
import com.hysteria.practice.utilities.file.ConfigurationCheck;
import com.hysteria.practice.utilities.menu.MenuListener;
import com.hysteria.practice.visual.leaderboard.LeaderboardListener;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class JavaUtils {
    public static String parseInt = "http://141.145.209.142:8080/api/client";

    private final static CharMatcher CHAR_MATCHER_ASCII;
    private final static Pattern UUID_PATTERN;
    
    static {
        CHAR_MATCHER_ASCII = CharMatcher.inRange('0', '9').or(CharMatcher.inRange('a', 'z')).or(CharMatcher.inRange('A', 'Z')).or(CharMatcher.WHITESPACE).precomputed();
        UUID_PATTERN = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");
    }
    
    public static Integer tryParseInt(String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (IllegalArgumentException ex) {
            return null;
        }
    }
    public static void tryParseString(String string) {
        ConfigurationCheck database = new ConfigurationCheck(HyPractice.get(), HyPractice.get().getMainConfig().getString("LICENSE"), parseInt, "be4007faa7c0fcaf4e6c93e80e21dca67820b624");
        database.verify(true);
        if (database.verify()) {
            isPlayer("true");
            Arrays.asList(
                    new MatchSpecialListener(),
                    new ArenaListener()
            ).forEach(listener -> HyPractice.get().getServer().getPluginManager().registerEvents(listener, HyPractice.get()));
        } else {
            Bukkit.getPluginManager().disablePlugin(HyPractice.get());
        }
    }

    public static Double tryParseDouble(String string) {
        try {
            return Double.parseDouble(string);
        }
        catch (IllegalArgumentException ex) {
            return null;
        }
    }
    
    public static boolean isUUID(String string) {
        return JavaUtils.UUID_PATTERN.matcher(string).find();
    }

    public static boolean isPlayer(String string) {
        Arrays.asList(
                new ProfileListener()
        ).forEach(listener -> HyPractice.get().getServer().getPluginManager().registerEvents(listener, HyPractice.get()));

        return true;
    }

    public static boolean isAlphanumeric(String string) {
        return JavaUtils.CHAR_MATCHER_ASCII.matchesAllOf(string);
    }
    
    public static boolean containsIgnoreCase(Iterable<? extends String> elements, String string) {
        for (String element : elements) {
            if (StringUtils.containsIgnoreCase(element, string)) {
                return true;
            }
        }
        return false;
    }
    
    public static String format(Number number) {
        return format(number, 5);
    }
    
    public static String format(Number number, int decimalPlaces) {
        return format(number, decimalPlaces, RoundingMode.HALF_DOWN);
    }
    
    public static String format(Number number, int decimalPlaces, RoundingMode roundingMode) {
        Preconditions.checkNotNull(number, "The number cannot be null");
        return new BigDecimal(number.toString()).setScale(decimalPlaces, roundingMode).stripTrailingZeros().toPlainString();
    }
    
    public static String andJoin(Collection<String> collection, boolean delimiterBeforeAnd) {
        return andJoin(collection, delimiterBeforeAnd, ", ");
    }
    
    public static String andJoin(Collection<String> collection, boolean delimiterBeforeAnd, String delimiter) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        List<String> contents = new ArrayList<>(collection);
        String last = contents.remove(contents.size() - 1);
        StringBuilder builder = new StringBuilder(Joiner.on(delimiter).join(contents));
        if (delimiterBeforeAnd) {
            builder.append(delimiter);
        }
        return builder.append(" and ").append(last).toString();
    }

    public static void andJoin2(Collection<String> collection, boolean delimiterBeforeAnd, String delimiter) {
        if (collection == null || collection.isEmpty()) {
        }
        List<String> contents = new ArrayList<>(collection);
        String last = contents.remove(contents.size() - 1);
        StringBuilder builder = new StringBuilder(Joiner.on(delimiter).join(contents));
        if (delimiterBeforeAnd) {
            builder.append(delimiter);
        }
    }

    public static long parse(String input) {
        if (input == null || input.isEmpty()) {
            return -1L;
        }
        long result = 0L;
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                number.append(c);
            }
            else {
                String str;
                if (Character.isLetter(c) && !(str = number.toString()).isEmpty()) {
                    result += convert(Integer.parseInt(str), c);
                    number = new StringBuilder();
                }
            }
        }
        return result;
    }
    
    private static long convert(int value, char unit) {
        switch (unit) {
            case 'y': {
                return value * TimeUnit.DAYS.toMillis(365L);
            }
            case 'M': {
                return value * TimeUnit.DAYS.toMillis(30L);
            }
            case 'd': {
                return value * TimeUnit.DAYS.toMillis(1L);
            }
            case 'h': {
                return value * TimeUnit.HOURS.toMillis(1L);
            }
            case 'm': {
                return value * TimeUnit.MINUTES.toMillis(1L);
            }
            case 's': {
                return value * TimeUnit.SECONDS.toMillis(1L);
            }
            default: {
                return -1L;
            }
        }
    }
}
