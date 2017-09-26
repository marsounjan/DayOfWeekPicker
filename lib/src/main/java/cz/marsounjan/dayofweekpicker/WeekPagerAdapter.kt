package cz.marsounjan.dayofweekpicker

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dwp_week.view.*
import org.joda.time.DateTime
import java.util.*


/**
 * Created by Jan Marsoun on 25/09/17.
 * marsounjan@gmail.com
 */
class WeekPagerAdapter(
        val context : Context,
        val weekAdapterProvider : (position : Int) -> DayOfWeekAdapter,
        val weekAdapterMap : MutableMap<Int, DayOfWeekAdapter>

) : PagerAdapter(){

    internal val initialPosition = Int.MAX_VALUE / 2

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {

        weekAdapterMap.put(
                position,
                weekAdapterProvider.invoke(position)
        )

        val inflater = LayoutInflater.from(context);
        val weekLayout = inflater.inflate(R.layout.dwp_week, container, false) as ViewGroup;
        weekLayout.dwp_WeekRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        weekLayout.dwp_WeekRecycler.adapter = weekAdapterMap.get(position)
        container?.addView(weekLayout);
        return weekLayout;
    }

    override fun destroyItem(container: ViewGroup?, position: Int, view: Any?) {
        (view as View?)?.let{
            weekAdapterMap.remove(position)
            container?.removeView(it)
        }

    }

    override fun isViewFromObject(view: View?, obj: Any?): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return Int.MAX_VALUE
    }
}