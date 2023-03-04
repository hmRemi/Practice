package com.hysteria.practice.utilities;

import com.hysteria.practice.HyPractice;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {

    public static void run(Runnable runnable) {
        HyPractice.get().getServer().getScheduler().runTask(HyPractice.get(), runnable);
    }

    public static void runAsync(Runnable runnable) {
        try {
            HyPractice.get().getServer().getScheduler().runTaskAsynchronously(HyPractice.get(), runnable);
        } catch (IllegalStateException e) {
            HyPractice.get().getServer().getScheduler().runTask(HyPractice.get(), runnable);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        HyPractice.get().getServer().getScheduler().runTaskTimer(HyPractice.get(), runnable, delay, timer);
    }

    public static int runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(HyPractice.get(), delay, timer);
        return runnable.getTaskId();
    }

    public static void runLater(Runnable runnable, long delay) {
        HyPractice.get().getServer().getScheduler().runTaskLater(HyPractice.get(), runnable, delay);
    }

    public static void runLaterAsync(Runnable runnable, long delay) {
        try {
            HyPractice.get().getServer().getScheduler().runTaskLaterAsynchronously(HyPractice.get(), runnable, delay);
        } catch (IllegalStateException e) {
            HyPractice.get().getServer().getScheduler().runTaskLater(HyPractice.get(), runnable, delay);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void runTimerAsync(Runnable runnable, long delay, long timer) {
        try {
            HyPractice.get().getServer().getScheduler().runTaskTimerAsynchronously(HyPractice.get(), runnable, delay, timer);
        } catch (IllegalStateException e) {
            HyPractice.get().getServer().getScheduler().runTaskTimer(HyPractice.get(), runnable, delay, timer);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
        HyPractice.get().getServer().getScheduler().runTaskTimerAsynchronously(HyPractice.get(), runnable, delay, timer);
    }

}
