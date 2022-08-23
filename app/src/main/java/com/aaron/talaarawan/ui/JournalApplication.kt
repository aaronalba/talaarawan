package com.aaron.talaarawan.ui

import android.app.Application
import com.aaron.talaarawan.data.JournalRoomDatabase

class JournalApplication : Application() {
    val database: JournalRoomDatabase by lazy { JournalRoomDatabase.getDatabase(this) }
}