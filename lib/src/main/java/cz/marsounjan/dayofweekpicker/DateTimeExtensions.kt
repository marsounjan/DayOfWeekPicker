package cz.marsounjan.dayofweekpicker

import org.joda.time.DateTime
import org.joda.time.DateTimeConstants


/**
 * Created by Jan Marsoun on 25/09/17.
 * marsounjan@gmail.com
 * Copyright (c) 2017 Ubiquiti Networks, Inc. All rights reserved.
 */
fun DateTime.withTimeAtStartWeek() : DateTime {
    return this.withTimeAtStartOfDay().withDayOfWeek(DateTimeConstants.MONDAY)
}

fun DateTime.withTimeAtEndWeek() : DateTime {
    return this.withTimeAtStartOfDay().withDayOfWeek(DateTimeConstants.SUNDAY)
}