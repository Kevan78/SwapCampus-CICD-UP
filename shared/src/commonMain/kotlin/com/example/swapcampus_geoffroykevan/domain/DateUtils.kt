@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.example.swapcampus_geoffroykevan.domain

import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

fun today(): LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
