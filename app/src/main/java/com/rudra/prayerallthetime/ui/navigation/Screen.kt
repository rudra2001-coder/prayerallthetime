package com.rudra.prayerallthetime.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String = "",
    val icon: ImageVector? = null,
    val emoji: String? = null
) {
    object Dashboard : Screen("dashboard", "Home", Icons.Default.Home, "🏠")
    object Explore : Screen("explore", "More", Icons.Default.MoreHoriz, "⚙️")
    object Prayers : Screen("prayers", "Prayer", Icons.Default.Mosque, "🕋")
    object QuranHadith : Screen("quran_hadith", "Quran", Icons.Default.MenuBook, "📖")
    object Community : Screen("community", "Community", Icons.Default.Groups, "👥")
    object Hadith : Screen("hadith", "Hadith", Icons.Default.MenuBook, "📜")
    object Analytics : Screen("analytics", "Stats", Icons.Default.BarChart, "📊")
    object Settings : Screen("settings", "Settings", Icons.Default.Settings, "⚙️")
    
    object Ramadan : Screen("ramadan", "Ramadan", Icons.Default.NightsStay, "🌙")
    object Worship : Screen("worship", "Worship", Icons.Default.FormatListBulleted, "📿")
    object Family : Screen("family", "Family", Icons.Default.People, "👨‍👩‍👧")
    object Qibla : Screen("qibla", "Qibla", Icons.Default.Explore, "🧭")
    
    object Tasbeeh : Screen("tasbeeh", "Tasbeeh", emoji = "📿")
    object Wudu : Screen("wudu", "Wudu", emoji = "💧")
    object Tahajjud : Screen("tahajjud", "Tahajjud", emoji = "🌙")
    object Charity : Screen("charity", "Charity", emoji = "🤲")
    object Calendar : Screen("calendar", "Calendar", emoji = "📅")
    object Profile : Screen("profile", "Profile", emoji = "👤")
    object Notifications : Screen("notifications", "Notifications", emoji = "🔔")
    object Achievements : Screen("achievements", "Achievements", emoji = "🏆")
    object Streaks : Screen("streaks", "Streaks", emoji = "🔥")
    object Charts : Screen("charts", "Charts", emoji = "📈")
    object StreakDetails : Screen("streak_details", "Streak Details")
    object RamadanTimer : Screen("ramadan_timer", "Ramadan Timer")
    object Taraweeh : Screen("taraweeh", "Taraweeh")
    object FamilyMember : Screen("family_member", "Family Member")
    object PrayerTimes : Screen("prayer_times", "Prayer Times", emoji = "🕋")
    object Report : Screen("report", "Report", Icons.Default.Assessment, "📊")
    
    object Habits : Screen("habits", "Faith Habits", Icons.Default.AssignmentTurnedIn, "📈")
    object Duas : Screen("duas", "Dua Library", Icons.Default.MenuBook, "🤲")
    
    // Quran Screens
    object SurahList : Screen("surah_list", "Surah List")
    object SurahDetail : Screen("surah_detail/{surahNumber}", "Surah Detail")

    // New Feature Screen
    object NearbyMosques : Screen("nearby_mosques", "Nearby Mosques", Icons.Default.Place, "🕌")


    companion object {
        // 5 main tabs as requested: Home, Quran, Prayer, Community, More
        val bottomNavItems = listOf(Dashboard, QuranHadith, Prayers, Community, Explore).filterNotNull()
        
        fun allScreens() = listOf(
            Dashboard, Explore, Prayers, QuranHadith, Hadith, Analytics, Settings,
            Ramadan, Worship, Family, Qibla, Tasbeeh, Wudu, Tahajjud,
            Charity, Calendar, Profile, Notifications, Achievements,
            Streaks, Charts, StreakDetails, RamadanTimer, Taraweeh,
            FamilyMember, PrayerTimes, Report, Habits, Duas, NearbyMosques,
            SurahList, SurahDetail
        ).filterNotNull()
    }
}
