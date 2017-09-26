package cz.marsounjan.dayofweekpicker

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.joda.time.DateTime
import org.joda.time.Days
import java.util.*
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.dwp_day_container.view.*


/**
 * Created by Jan Marsoun on 25/09/17.
 * marsounjan@gmail.com
 */
abstract class DayOfWeekAdapter(
        private var startDay : Date,
        private var endDay : Date,
        var currentDate : Date = Date(),
        var selectedDate : Date?,
        private val dayDataMap: Map<Long, DayData>,
        val dateSelectedListener : DateSelectedListener? = null
) : RecyclerView.Adapter<DayOfWeekAdapter.Holder>() {

    interface DateSelectedListener{
        fun dateSelected(date : Date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val container : LinearLayout = inflater.inflate(R.layout.dwp_day_container, parent, false) as LinearLayout

        val width = parent.measuredWidth / itemCount
        container.setLayoutParams(RecyclerView.LayoutParams(width, RecyclerView.LayoutParams.MATCH_PARENT))

        inflater.inflate(getLayoutResource(viewType), container, true)

        return Holder(container)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        getBinderForViewType(getItemViewType(position))
                .bindItem(
                        position,
                        holder.itemView,
                        getDateForPosition(position),
                        getDayDataForPosition(position)
                )
        dateSelectedListener?.let{ listener ->
            holder.itemView.setOnClickListener(View.OnClickListener {
                listener.dateSelected(getDateForPosition(position))
            })
        }

    }

    override fun getItemCount(): Int {
        val start = DateTime(startDay)
        val end = DateTime(endDay)
        var daysBetween = Days.daysBetween(
                start,
                end
        ).days
        //since jodatime is calculating just whole days in between we must add one day if days do not match
        if(start.dayOfMonth() != end.dayOfMonth()){
            daysBetween = daysBetween.inc()
        }
        return daysBetween

    }

    abstract fun getBinderForViewType(viewType: Int): Binder<DayData>

    abstract fun getLayoutResource(viewType: Int): Int

    abstract fun getDefaultDayData(date : Date) : DayData

    protected fun getDateForPosition(position : Int) : Date {
        return DateTime(startDay).withTimeAtStartOfDay().plusDays(position).toDate()
    }

    private fun getDayDataForPosition(position : Int) : DayData {
        val currentDateTime = DateTime(currentDate)
        val dateTime = DateTime(startDay).withTimeAtStartOfDay().plusDays(position)
        var dayData = dayDataMap.get(dateTime.millis)
        if(dayData == null){
            dayData = getDefaultDayData(dateTime.toDate())
        }

        //init today/past
        if(currentDateTime.dayOfMonth() == dateTime.dayOfMonth()
                && currentDateTime.monthOfYear() == dateTime.monthOfYear()
                && currentDateTime.year() == dateTime.year()){
            dayData.isToday = true
            dayData.inPast = false
        }
        else{
            dayData.isToday = false
            dayData.inPast = dateTime.isBefore(currentDateTime)
        }

        //init selected day
        if(selectedDate != null){
            val seletedDateTime = DateTime(selectedDate)
            if(seletedDateTime.dayOfMonth() == dateTime.dayOfMonth()
                    && seletedDateTime.monthOfYear() == dateTime.monthOfYear()
                    && seletedDateTime.year() == dateTime.year()){
                dayData.isSelected = true
            }
            else{
                dayData.isSelected = false
            }
        }

        return dayData
    }

    abstract class Binder<T> {

        internal fun bindItem(position: Int, view: View, date : Date, additionalData: T){
            bindDataToItem(
                    position,
                    view.dwp_vDayContainer,
                    date,
                    additionalData
            )
        }

        abstract fun bindDataToItem(position: Int, view: View, date : Date, additionalData: T)
    }

    open class Holder(view: View) : RecyclerView.ViewHolder(view)

}

