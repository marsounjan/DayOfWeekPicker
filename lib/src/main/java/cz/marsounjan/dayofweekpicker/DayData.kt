package cz.marsounjan.dayofweekpicker

import java.util.*

/**
 * Created by Jan Marsoun on 25/09/17.
 * marsounjan@gmail.com
 */
open class DayData(
        val date : Date,
        var isSelected: Boolean = false,
        var isToday: Boolean = false,
        var inPast: Boolean = false,
        var hasIndicator : Boolean = false
)