package cz.marsounjan.dayofweekpicker

import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.support.v4.view.ViewCompat
import android.support.v4.widget.TextViewCompat
import android.view.View
import kotlinx.android.synthetic.main.dwp_day.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Jan Marsoun on 25/09/17.
 * marsounjan@gmail.com
 */
class DayOfWeekBinder(
        var bgDefault : Drawable,
        var bgSelected : Drawable,
        var bgToday : Drawable,
        var bgPast : Drawable,
        var txtColorDefault : Int,
        var txtColorSelected : Int,
        var txtColorToday : Int,
        var txtColorPast : Int,
        var indicatorColorDefault: Int,
        var indicatorColorSelected : Int,
        var indicatorColorToday : Int,
        var indicatorColorPast : Int,
        var indicatorWidth : Int,
        var selectedDayElevationEnabled : Boolean,
        var selectedDayElevationPx : Int,
        var weekDayTextAppearanceResId : Int,
        var dayNumberTextAppearanceResId : Int
) : DayOfWeekAdapter.Binder<DayData>() {

    override fun bindDataToItem(position: Int, view: View, date : Date, data: DayData) {

        view.dwp_vDayName.text = SimpleDateFormat("EEE", Locale.getDefault()).format(date)
        view.dwp_vDayNumber.text = SimpleDateFormat("d", Locale.getDefault()).format(date)

        TextViewCompat.setTextAppearance(view.dwp_vDayName, weekDayTextAppearanceResId)
        TextViewCompat.setTextAppearance(view.dwp_vDayNumber, dayNumberTextAppearanceResId)

        val indicatorColor : Int
        if(data.isSelected){
            view.dwp_vDayLayout.background = bgSelected
            view.dwp_vDayName.setTextColor(txtColorSelected)
            view.dwp_vDayNumber.setTextColor(txtColorSelected)
            indicatorColor = indicatorColorSelected
        }
        else if(data.isToday){
            view.dwp_vDayLayout.background = bgToday
            view.dwp_vDayName.setTextColor(txtColorToday)
            view.dwp_vDayNumber.setTextColor(txtColorToday)
            indicatorColor = indicatorColorToday
        }
        else if(data.inPast){
            view.dwp_vDayLayout.background = bgPast
            view.dwp_vDayName.setTextColor(txtColorPast)
            view.dwp_vDayNumber.setTextColor(txtColorPast)
            indicatorColor = indicatorColorPast
        }
        else{
            view.dwp_vDayLayout.background = bgDefault
            view.dwp_vDayName.setTextColor(txtColorDefault)
            view.dwp_vDayNumber.setTextColor(txtColorDefault)
            indicatorColor = indicatorColorDefault
        }

        if(data.isSelected && selectedDayElevationEnabled){
            ViewCompat.setElevation(view.dwp_vDayLayout, selectedDayElevationPx.toFloat())
        }
        else{
            ViewCompat.setElevation(view.dwp_vDayLayout, 0f)
        }

        if(data.hasIndicator){

            val indicatorDrawable = ShapeDrawable(OvalShape())
            indicatorDrawable.intrinsicWidth = indicatorWidth
            indicatorDrawable.intrinsicHeight = indicatorWidth
            indicatorDrawable.paint.color = indicatorColor

            view.dwp_vDayIndicator.setImageDrawable(indicatorDrawable)
        }
        else{
            view.dwp_vDayIndicator.setImageDrawable(null)
        }

    }
}