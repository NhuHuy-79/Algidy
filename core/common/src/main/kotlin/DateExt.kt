fun Int.daysFromNow(): Long {
    return System.currentTimeMillis() + (this.toLong() * 24 * 60 * 60 * 1000)
}

fun Long.toReadableDate(): String {
    val date = java.util.Date(this)
    val format = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
    return format.format(date)
}