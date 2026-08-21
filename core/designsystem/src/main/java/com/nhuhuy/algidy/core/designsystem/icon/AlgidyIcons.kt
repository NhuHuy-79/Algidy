package com.nhuhuy.algidy.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.Label
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.AutoMode
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Brightness5
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.DocumentScanner
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material.icons.rounded.FileUpload
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.FlashOff
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.Gavel
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Hive
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Inventory
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.ModeEdit
import androidx.compose.material.icons.rounded.NewReleases
import androidx.compose.material.icons.rounded.NotificationImportant
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SortByAlpha
import androidx.compose.material.icons.rounded.StackedLineChart
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material.icons.rounded.Widgets
import com.nhuhuy.algidy.core.designsystem.R

object AlgidyIcons {

    val NavigateBack = IconProvider.ImageVectorIcon(Icons.AutoMirrored.Rounded.ArrowBack)
    val Close = IconProvider.ImageVectorIcon(Icons.Rounded.Close)
    val ConsumeFood = IconProvider.DrawableResourceIcon(R.drawable.ic_fork_spoon)
    val WasteFood = IconProvider.DrawableResourceIcon(R.drawable.ic_delete)

    object BottomBar {
        val UnselectedInventory = IconProvider.ImageVectorIcon(Icons.Outlined.Inventory)
        val SelectedInventory = IconProvider.ImageVectorIcon(Icons.Rounded.Inventory)
        val UnselectedAnalytics = IconProvider.ImageVectorIcon(Icons.Outlined.Analytics)
        val SelectedAnalytics = IconProvider.ImageVectorIcon(Icons.Rounded.Analytics)
        val UnselectedSettings = IconProvider.ImageVectorIcon(Icons.Outlined.Settings)
        val SelectedSettings = IconProvider.ImageVectorIcon(Icons.Rounded.Settings)
    }

    object FoodEntry {
        val AddFood = IconProvider.ImageVectorIcon(Icons.Rounded.Add)
        val AddCategory = IconProvider.ImageVectorIcon(Icons.Rounded.Add)
        val EditFood = IconProvider.ImageVectorIcon(Icons.Rounded.Edit)
        val EditMode = IconProvider.ImageVectorIcon(Icons.Rounded.ModeEdit)
        val ExpiryDate = IconProvider.ImageVectorIcon(Icons.Rounded.Timer)
        val PickImage = IconProvider.ImageVectorIcon(Icons.Rounded.Image)
    }

    object Inventory {
        val AddFood = IconProvider.ImageVectorIcon(Icons.Rounded.Add)
        val EditFood = IconProvider.ImageVectorIcon(Icons.Rounded.Edit)
        val SearchFood = IconProvider.ImageVectorIcon(Icons.Rounded.Search)
        val ScanFood = IconProvider.DrawableResourceIcon(R.drawable.ic_scan_barcode)
        val FilterFood = IconProvider.ImageVectorIcon(Icons.Rounded.FilterList)
        val DetailFood = IconProvider.DrawableResourceIcon(R.drawable.food_detail)
        val SortByExpiry = IconProvider.ImageVectorIcon(Icons.Rounded.Today)
        val ExpiryOnly = IconProvider.ImageVectorIcon(Icons.Rounded.Warning)
        val SortByName = IconProvider.ImageVectorIcon(Icons.Rounded.SortByAlpha)
        val ResetFilter = IconProvider.ImageVectorIcon(Icons.Rounded.Restore)
        val Category = IconProvider.ImageVectorIcon(Icons.AutoMirrored.Rounded.Label)
        val ExpandToolbar = IconProvider.ImageVectorIcon(Icons.Rounded.ExpandMore)
        val CloseToolbar = IconProvider.ImageVectorIcon(Icons.Rounded.ExpandLess)
        val RemainDays = IconProvider.ImageVectorIcon(Icons.Rounded.Timer)
        val SelectAll = IconProvider.ImageVectorIcon(Icons.Rounded.SelectAll)
        val AddCategory = IconProvider.ImageVectorIcon(Icons.Rounded.Add)
        val DeleteCategory = IconProvider.ImageVectorIcon(Icons.Rounded.Delete)
        val LastHistory = IconProvider.ImageVectorIcon(Icons.Rounded.History)
        val HistoryAction = IconProvider.ImageVectorIcon(Icons.AutoMirrored.Rounded.ArrowForward)
    }

    object Scanner {
        val FlashOn = IconProvider.ImageVectorIcon(Icons.Rounded.FlashOn)
        val AddImage = IconProvider.DrawableResourceIcon(R.drawable.image_plus)
        val AddBarcode = IconProvider.DrawableResourceIcon(R.drawable.circle_plus)
        val FlashOff = IconProvider.ImageVectorIcon(Icons.Rounded.FlashOff)
        val AutoOff = IconProvider.ImageVectorIcon(Icons.Rounded.PhotoCamera)
        val Scanning = IconProvider.ImageVectorIcon(Icons.Rounded.DocumentScanner)
        val Failure = IconProvider.ImageVectorIcon(Icons.Rounded.ErrorOutline)
        val AddManually = IconProvider.ImageVectorIcon(Icons.Rounded.Add)
    }

    object Analytics {
        val FoodStreak = IconProvider.ImageVectorIcon(Icons.Rounded.CalendarMonth)
        val Other = IconProvider.ImageVectorIcon(Icons.Rounded.Fastfood)
        val WeeklyChart = IconProvider.ImageVectorIcon(Icons.Rounded.BarChart)
        val SpoilageChart = IconProvider.ImageVectorIcon(Icons.Rounded.StackedLineChart)
    }

    object Settings {
        val Appearance = IconProvider.ImageVectorIcon(Icons.Rounded.ColorLens)

        val DarkTheme = IconProvider.ImageVectorIcon(Icons.Rounded.DarkMode)
        val LightTheme = IconProvider.ImageVectorIcon(Icons.Rounded.Brightness5)
        val SystemTheme = IconProvider.ImageVectorIcon(Icons.Rounded.AutoMode)
        val Notifications = IconProvider.ImageVectorIcon(Icons.Rounded.NotificationsActive)
        val YourData = IconProvider.ImageVectorIcon(Icons.Rounded.Hive)
        val OtherSetting = IconProvider.ImageVectorIcon(Icons.Rounded.Language)
        val WidgetDebug = IconProvider.ImageVectorIcon(Icons.Rounded.Widgets)
        val AboutApp = IconProvider.ImageVectorIcon(Icons.Rounded.Apps)
        val ExpandSetting = IconProvider.ImageVectorIcon(Icons.AutoMirrored.Rounded.ArrowForward)
        val Language = IconProvider.ImageVectorIcon(Icons.Rounded.Language)

        val DailyReminder = IconProvider.ImageVectorIcon(Icons.Rounded.Alarm)
        val EditReminder = IconProvider.ImageVectorIcon(Icons.Rounded.Edit)
        val ExpiryReminder = IconProvider.ImageVectorIcon(Icons.Rounded.NotificationImportant)
        val DeleteReminder = IconProvider.ImageVectorIcon(Icons.Rounded.Delete)

        val ExportData = IconProvider.ImageVectorIcon(Icons.Rounded.FileUpload)
        val ImportData = IconProvider.ImageVectorIcon(Icons.Rounded.FileDownload)
        val DeleteAll = IconProvider.ImageVectorIcon(Icons.Rounded.DeleteForever)

        val NewFeature = IconProvider.ImageVectorIcon(Icons.Rounded.NewReleases)
        val Feedback = IconProvider.ImageVectorIcon(Icons.Rounded.Feedback)
        val PrivatePolicy = IconProvider.ImageVectorIcon(Icons.Rounded.Policy)
        val License = IconProvider.ImageVectorIcon(Icons.Rounded.Gavel)
        val OpenSource = IconProvider.ImageVectorIcon(Icons.Rounded.Code)

    }

}


