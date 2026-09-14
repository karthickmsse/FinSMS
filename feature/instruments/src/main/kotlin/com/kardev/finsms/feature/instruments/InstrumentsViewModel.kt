package com.kardev.finsms.feature.instruments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kardev.finsms.core.database.dao.InstrumentDao
import com.kardev.finsms.core.database.entity.InstrumentEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class InstrumentsViewModel @Inject constructor(
    instrumentDao: InstrumentDao
) : ViewModel() {
    val instruments = instrumentDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList<InstrumentEntity>())
}
