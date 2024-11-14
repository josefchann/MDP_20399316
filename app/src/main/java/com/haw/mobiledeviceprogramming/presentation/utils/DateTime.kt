package com.haw.mobiledeviceprogramming.presentation.utils

data class Date(
    val title: String,
)

data class Time (
    val title: String,
)

val sampleDate = listOf(
    Date("Sunday, 12 June"),
    Date("Monday, 13 June"),
    Date("Tuesday, 14 June"),
    Date("Wednesday, 15 June"),
    Date("Thursday, 16 June"),
)

val sampleTime = listOf(
    Time("11.00 - 12.00 PM"),
    Time("12.00 - 01.00 PM"),
    Time("01.00 - 02.00 PM"),
)
