package cz.marsounjan.dayofweekpicker

import android.support.annotation.LayoutRes
import java.util.*


/**
 * Created by Jan Marsoun on 25/09/17.
 * marsounjan@gmail.com
 */
class DefaultDayOfWeekAdapter(
        startDay : Date,
        endDay : Date,
        currentDate : Date = Date(),
        selectedDate : Date?,
        dateSelectedListener: DayOfWeekAdapter.DateSelectedListener? = null,
        dayDataMap : Map<Long, DayData>,
        val defaultBinder : DayOfWeekAdapter.Binder<DayData>,
        @LayoutRes val defaultDayCellResource : Int,
        val defaultDayDataProvider : (Date) -> DayData
) : DayOfWeekAdapter(
        startDay = startDay,
        endDay = endDay,
        currentDate = currentDate,
        selectedDate = selectedDate,
        dateSelectedListener = dateSelectedListener,
        dayDataMap = dayDataMap
) {
    init{
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getDateForPosition(position).time
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getBinderForViewType(viewType: Int): DayOfWeekAdapter.Binder<DayData> {
        return defaultBinder
    }

    override fun getLayoutResource(viewType: Int): Int {
        return defaultDayCellResource
    }

    override fun getDefaultDayData(date: Date): DayData {
        return defaultDayDataProvider.invoke(date)
    }
}

