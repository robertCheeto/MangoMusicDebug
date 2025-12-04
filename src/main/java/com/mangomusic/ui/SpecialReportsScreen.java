package com.mangomusic.ui;

import com.mangomusic.data.ReportsDao;
import com.mangomusic.models.ReportResult;
import com.mangomusic.util.ConsoleColors;
import com.mangomusic.util.InputValidator;

import java.util.List;

public class SpecialReportsScreen {

    private final ReportsDao reportsDao;

    public SpecialReportsScreen(ReportsDao reportsDao) {
        this.reportsDao = reportsDao;
    }

    public void display() {
        boolean running = true;

        while (running) {
            InputValidator.clearScreen();
            displayMenu();

            int choice = InputValidator.getIntInRange("Select an option: ", 0, 3);

            switch (choice) {
                case 1:
                    showMangoMusicMapped();
                    break;
                case 2:
                    showMostPlayedAlbumsByGenre();
                    break;
                case 3:
                    showUserDiversityScore();
                    break;
                case 4:
                    //@TODO - Create report
//                    showPeakListeningHours();
                    break;
                case 0:
                    running = false;
                    break;
            }
        }
    }

    private void displayMenu() {
        ConsoleColors.printHeader("SPECIAL REPORTS");

        System.out.println("\nPERSONALIZED ANALYTICS:");
        System.out.println("1. MangoMusic Mapped (Year in Review)");
        System.out.println("2. Most Played Albums by Genre");
        System.out.println("3. User Listening Diversity Score");
        System.out.println("4. Peak Listening Hours Analysis");

        System.out.println("\n0. Back to main menu");
        System.out.println();
    }

    private void showMangoMusicMapped() {
        InputValidator.clearScreen();
        ConsoleColors.printHeader("🎵 MANGOMUSIC MAPPED 🎵");
        System.out.println("Your personalized year in review\n");

        int userId = InputValidator.getIntInRange("Enter user ID: ", 1, Integer.MAX_VALUE);

        ReportResult mapped = reportsDao.getMangoMusicMapped(userId);

        int year = mapped.getInt("year");

        if (mapped.getInt("total_plays") == 0) {
            ConsoleColors.printWarning("No listening data found for user ID " + userId + " in " + year + ".");
        } else {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("YOUR " + year + " LISTENING STORY");
            System.out.println("=".repeat(70));

            System.out.println("\n🎧 LISTENING STATS:");
            System.out.println("   Total Plays: " + mapped.getInt("total_plays"));
            System.out.println("   Albums Explored: " + mapped.getInt("unique_albums"));
            System.out.println("   Artists Discovered: " + mapped.getInt("unique_artists"));
            System.out.println("   Completion Rate: " +
                    String.format("%.1f%%", (mapped.getInt("completed_plays") * 100.0 / mapped.getInt("total_plays"))));

            System.out.println("\n⭐ YOUR TOP PICKS:");
            System.out.println("   #1 Artist: " + mapped.getString("top_artist") +
                    " (" + mapped.getInt("top_artist_plays") + " plays)");
            System.out.println("   Favorite Genre: " + mapped.getString("top_genre"));
            System.out.println("   Most Active Month: " + mapped.getString("top_month") +
                    " (" + mapped.getInt("top_month_plays") + " plays)");

            System.out.println("\n🔥 FUN FACTS:");
            System.out.println("   Longest Listening Streak: " + mapped.getInt("longest_streak") + " days");
            System.out.println("   Listener Personality: " + mapped.getString("listener_personality"));

            System.out.println("\n" + "=".repeat(70));
            System.out.println("Thanks for making " + year + " a year full of music! 🎶");
            System.out.println("=".repeat(70));
        }

        InputValidator.pressEnterToContinue();
    }

    private void showMostPlayedAlbumsByGenre() {
        InputValidator.clearScreen();
        ConsoleColors.printSection("Most Played Albums By Genre");
        System.out.println("Shows the Top 5 Played Albums By Genre\n");

        List<ReportResult> results = reportsDao.getMostPlayedAlbumsByGenre();

        if (results.isEmpty()) {
            ConsoleColors.printWarning("No data available for this report.");
        } else {
            System.out.printf("%-15s %20s %20s %20s%n", "Album", "Artist", "Play Count", "Rank");
            System.out.println("-".repeat(100));
            int i = 0;

            for (ReportResult result : results) {
                if (i % 5 == 0 && i != 0) {
                    System.out.println("----");
                }
                System.out.printf("%-6s %-30s %-30s %30d %30d%n",
                        result.getString("genre"),
                        result.getString("album_title"),
                        result.getString("artist_name"),
                        result.getInt("play_count"),
                        result.getInt("genre_rank"));
                i++;
            }
        }

        InputValidator.pressEnterToContinue();
    }

    private void showUserDiversityScore(){
        InputValidator.clearScreen();
        ConsoleColors.printSection("Top 100 User Diversity Scores");
        System.out.println("Shows the Top 100 User Diversity Scores\n");

        List<ReportResult> results = reportsDao.getUserDiversityScore();

        if (results.isEmpty()) {
            ConsoleColors.printWarning("No data available for this report.");
        } else {
            System.out.printf("%-15s %20s %20s %20s %20s %20s %20s%n", "User_ID", "Username", "Sub_Type", "Distinct_Artists", "Distinct_Genres", "Total_Plays", "Diversity_Score");
            System.out.println("-".repeat(100));
            int i = 0;

            for (ReportResult result : results) {
                System.out.printf("%-6d %-30s %-30s %10d %10d %10d %10.2f%n",
                        result.getInt("User_ID"),
                        result.getString("Username"),
                        result.getString("Sub_Type"),
                        result.getInt("Distinct_Artists"),
                        result.getInt("Distinct_Genres"),
                        result.getInt("Total_Plays"),
                        result.getDouble("Diversity_Score"));
            }
        }
    }
}