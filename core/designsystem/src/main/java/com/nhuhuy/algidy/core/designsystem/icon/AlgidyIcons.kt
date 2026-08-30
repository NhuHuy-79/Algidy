package com.nhuhuy.algidy.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.Label
import androidx.compose.material.icons.automirrored.rounded.PlaylistAdd
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.BrightnessAuto
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.CloudDownload
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DocumentScanner
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.FlashOff
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.Gavel
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Hive
import androidx.compose.material.icons.rounded.Inventory
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.ModeEdit
import androidx.compose.material.icons.rounded.NewReleases
import androidx.compose.material.icons.rounded.Notifications
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
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material.icons.rounded.WbSunny
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


    object Widget {
        val RefreshData = IconProvider.DrawableResourceIcon(R.drawable.ic_refresh)
        val WeekExpiryIcon = IconProvider.DrawableResourceIcon(R.drawable.ic_fridge)
        val ConsumedFood = IconProvider.DrawableResourceIcon(R.drawable.ic_fork_spoon)

        val TotalFood = IconProvider.DrawableResourceIcon(R.drawable.ic_pizza)

    }

    object FoodEntry {
        val AddFood = IconProvider.ImageVectorIcon(Icons.AutoMirrored.Rounded.PlaylistAdd)
        val AddCategory = IconProvider.DrawableResourceIcon(R.drawable.ic_new_label)
        val EditFood = IconProvider.ImageVectorIcon(Icons.Rounded.Edit)
        val EditMode = IconProvider.ImageVectorIcon(Icons.Rounded.ModeEdit)
        val StorageLocation = IconProvider.DrawableResourceIcon(R.drawable.ic_fridge)
        val ExpiryDate = IconProvider.ImageVectorIcon(Icons.Rounded.Timer)
        val PickImage = IconProvider.ImageVectorIcon(Icons.Rounded.AddPhotoAlternate)
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
        val AddImage = IconProvider.ImageVectorIcon(Icons.Rounded.AddPhotoAlternate)
        val AddBarcode = IconProvider.DrawableResourceIcon(R.drawable.ic_add_barcode)
        val FlashOff = IconProvider.ImageVectorIcon(Icons.Rounded.FlashOff)
        val AutoOff = IconProvider.ImageVectorIcon(Icons.Rounded.PhotoCamera)
        val Scanning = IconProvider.ImageVectorIcon(Icons.Rounded.DocumentScanner)
        val Failure = IconProvider.ImageVectorIcon(Icons.Rounded.ErrorOutline)
        val AddManually = IconProvider.ImageVectorIcon(Icons.Rounded.Add)
    }

    object Analytics {
        val WeeklyFoodChart = IconProvider.ImageVectorIcon(Icons.Rounded.BarChart)
        val MonthlyFreshness =
            IconProvider.DrawableResourceIcon(R.drawable.phosphor_chart_bar_horizontal)
        val SpoilageChart = IconProvider.ImageVectorIcon(Icons.Rounded.StackedLineChart)
    }

    object Settings {
        val ArrowForward = IconProvider.ImageVectorIcon(Icons.AutoMirrored.Rounded.ArrowForwardIos)
        val Appearance = IconProvider.ImageVectorIcon(Icons.Rounded.ColorLens)

        val DarkTheme = IconProvider.ImageVectorIcon(Icons.Rounded.DarkMode)
        val LightTheme = IconProvider.ImageVectorIcon(Icons.Rounded.WbSunny)
        val SystemTheme = IconProvider.ImageVectorIcon(Icons.Rounded.BrightnessAuto)
        val Notifications = IconProvider.ImageVectorIcon(Icons.Rounded.NotificationsActive)
        val YourData = IconProvider.ImageVectorIcon(Icons.Rounded.Hive)
        val OtherSetting = IconProvider.ImageVectorIcon(Icons.Rounded.Language)
        val WidgetDebug = IconProvider.ImageVectorIcon(Icons.Rounded.Widgets)
        val AboutApp = IconProvider.ImageVectorIcon(Icons.Rounded.Apps)
        val ExpandSetting = IconProvider.ImageVectorIcon(Icons.AutoMirrored.Rounded.ArrowForward)
        val Language = IconProvider.ImageVectorIcon(Icons.Rounded.Language)

        val CheckUpdate = IconProvider.ImageVectorIcon(Icons.Rounded.Update)

        val DailyReminder = IconProvider.ImageVectorIcon(Icons.Rounded.Alarm)
        val EditReminder = IconProvider.ImageVectorIcon(Icons.Rounded.ModeEdit)
        val ExpiryReminder = IconProvider.ImageVectorIcon(Icons.Rounded.Notifications)
        val DeleteReminder = IconProvider.DrawableResourceIcon(R.drawable.ic_delete)

        val ExportData = IconProvider.ImageVectorIcon(Icons.Rounded.CloudUpload)
        val ImportData = IconProvider.ImageVectorIcon(Icons.Rounded.CloudDownload)
        val DeleteAll = IconProvider.DrawableResourceIcon(R.drawable.ic_delete)

        val NewFeature = IconProvider.ImageVectorIcon(Icons.Rounded.NewReleases)
        val Feedback = IconProvider.ImageVectorIcon(Icons.Rounded.Feedback)
        val PrivatePolicy = IconProvider.ImageVectorIcon(Icons.Rounded.Policy)
        val License = IconProvider.ImageVectorIcon(Icons.Rounded.Gavel)
        val OpenSource = IconProvider.ImageVectorIcon(Icons.Rounded.Code)

        val Algidy = IconProvider.DrawableResourceIcon(R.drawable.ic_algidy)
        val Email = IconProvider.ImageVectorIcon(Icons.Rounded.AlternateEmail)
        val Github = IconProvider.DrawableResourceIcon(R.drawable.ic_github)
        val Instagram = IconProvider.DrawableResourceIcon(R.drawable.ic_instagram)
        val Discord = IconProvider.DrawableResourceIcon(R.drawable.ic_discord)
        val LinkedIn = IconProvider.DrawableResourceIcon(R.drawable.ic_linkedln)
    }

}


