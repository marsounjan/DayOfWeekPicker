package cz.marsounjan.dayofweekpicker

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.TextViewCompat
import android.view.LayoutInflater
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.dwp_main.view.*
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*
import android.os.Parcel




/**
 * Created by Jan Marsoun on 25/09/17.
 * marsounjan@gmail.com
 */
open class DayOfWeekPicker : FrameLayout {

    var weekPagerAdapter: WeekPagerAdapter
    internal val weekAdapterMap: MutableMap<Int, DayOfWeekAdapter> = mutableMapOf()
    internal val dayDataMap: MutableMap<Long, DayData> = mutableMapOf()

    var currentDate: Date = Date()
        set(value) {
            field = value
            refreshWeekAdapters()
        }

    var selectedDate: Date? = null
        set(value) {
            field = value
            refreshWeekAdapters()
        }

    var bgDefault: Drawable = ColorDrawable(Color.TRANSPARENT)
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var bgSelected: Drawable = ContextCompat.getDrawable(context, R.drawable.dwp_bg_date_selected_default)
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var bgToday: Drawable = ContextCompat.getDrawable(context, R.drawable.dwp_bg_date_today_default)
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var bgPast: Drawable = bgDefault
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var txtColorDefault: Int = ContextCompat.getColor(context, R.color.default_textColor)
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var txtColorSelected: Int = ContextCompat.getColor(context, R.color.default_textColorSelected)
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var txtColorToday: Int = ContextCompat.getColor(context, R.color.default_textColorToday)
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var txtColorPast: Int = ContextCompat.getColor(context, R.color.default_textColorPast)
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var indicatorColor: Int = ContextCompat.getColor(context, R.color.default_indicatorColor)
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var indicatorColorSelected: Int = ContextCompat.getColor(context, R.color.default_indicatorColorSelected)
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var indicatorColorToday: Int = ContextCompat.getColor(context, R.color.default_indicatorColorToday)
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var indicatorColorPast: Int = ContextCompat.getColor(context, R.color.default_indicatorColorPast)
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var indicatorWidth: Int = context.resources.getDimensionPixelSize(R.dimen.indicator_width)
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var selectedDayElevationEnabled: Boolean = true
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var selectedDayElevationPx: Int = 10
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var monthLabelEnabled: Boolean = true
        set(value) {
            field = value
            setupMonthLabel()
        }

    var monthLabelTextAppearanceResId: Int = R.style.DwpMonthLabelTextAppearance
        set(value) {
            field = value
            setupMonthLabel()
        }

    var weekDayTextAppearanceResId: Int = R.style.DwpDateWeekDayTextAppearance
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var dayNumberTextAppearanceResId: Int = R.style.DwpDateDayNumTextAppearance
        set(value) {
            field = value
            setupDefaultBinder()
        }

    var monthLabelDateFormat: String = "MMM"
        set(value) {
            field = value
            setupMonthLabel()
        }

    internal var defaultBinder: DayOfWeekBinder = DayOfWeekBinder(
            bgDefault,
            bgSelected,
            bgToday,
            bgPast,

            txtColorDefault,
            txtColorSelected,
            txtColorToday,
            txtColorPast,

            indicatorColor,
            indicatorColorSelected,
            indicatorColorToday,
            indicatorColorPast,
            indicatorWidth,

            selectedDayElevationEnabled,
            selectedDayElevationPx,

            weekDayTextAppearanceResId,
            dayNumberTextAppearanceResId
    )

    var dateSelectedListener: DateSelectedListener? = null
    var onVisibleDateSetChangedListener: OnVisibleDateSetChangedListener? = null

    interface DateSelectedListener {
        fun onDateSelected(date: Date)
    }

    interface OnVisibleDateSetChangedListener {
        fun onVisibleDateSetChanged(visibleDateSet: VisibleDateSet)
    }

    constructor(context: Context) :
            this(context, null)

    constructor(context: Context, attrs: AttributeSet?) :
            this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.dwp_main, this, true)

        weekPagerAdapter = WeekPagerAdapter(
                context = this.context,
                weekAdapterProvider = weekAdapterProvider,
                weekAdapterMap = weekAdapterMap
        )
        this.dwp_vWeekPager.setAdapter(weekPagerAdapter);
        this.dwp_vWeekPager.setCurrentItem(weekPagerAdapter.initialPosition, false)
        this.dwp_vWeekPager.addOnPageChangeListener(onWeekChangeListener)

        handleAttributes(context, attrs, defStyleAttr)

        setupDefaultBinder()
        setupMonthLabel()
    }

    private fun handleAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs != null) {
            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.DayOfWeekPicker)
            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_bg_default)) {
                bgDefault = attrArray.getDrawable(R.styleable.DayOfWeekPicker_bg_default)
            }
            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_bg_selected)) {
                bgSelected = attrArray.getDrawable(R.styleable.DayOfWeekPicker_bg_selected)
            }
            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_bg_today)) {
                bgToday = attrArray.getDrawable(R.styleable.DayOfWeekPicker_bg_today)
            }
            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_bg_past)) {
                bgPast = attrArray.getDrawable(R.styleable.DayOfWeekPicker_bg_past)
            }

            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_textColor_default)) {
                txtColorDefault = attrArray.getColor(
                        R.styleable.DayOfWeekPicker_textColor_default,
                        txtColorDefault
                )
            }
            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_textColor_selected)) {
                txtColorSelected = attrArray.getColor(
                        R.styleable.DayOfWeekPicker_textColor_selected,
                        txtColorSelected
                )
            }
            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_textColor_today)) {
                txtColorToday = attrArray.getColor(
                        R.styleable.DayOfWeekPicker_textColor_today,
                        txtColorToday
                )
            }
            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_textColor_past)) {
                txtColorPast = attrArray.getColor(
                        R.styleable.DayOfWeekPicker_textColor_past,
                        txtColorPast
                )
            }

            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_indicatorColor_default)) {
                indicatorColor = attrArray.getColor(
                        R.styleable.DayOfWeekPicker_indicatorColor_default,
                        indicatorColor
                )
            }
            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_indicatorColor_selected)) {
                indicatorColorSelected = attrArray.getColor(
                        R.styleable.DayOfWeekPicker_indicatorColor_selected,
                        indicatorColorSelected
                )
            }
            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_indicatorColor_today)) {
                indicatorColorToday = attrArray.getColor(
                        R.styleable.DayOfWeekPicker_indicatorColor_today,
                        indicatorColorToday
                )
            }
            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_indicatorColor_past)) {
                indicatorColorPast = attrArray.getColor(
                        R.styleable.DayOfWeekPicker_indicatorColor_past,
                        indicatorColorPast
                )
            }

            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_selectedDateElevationEnabled)) {
                selectedDayElevationEnabled = attrArray.getBoolean(
                        R.styleable.DayOfWeekPicker_selectedDateElevationEnabled,
                        selectedDayElevationEnabled
                )
            }
            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_selectedDateElevation)) {
                selectedDayElevationPx = attrArray.getDimensionPixelSize(
                        R.styleable.DayOfWeekPicker_selectedDateElevation,
                        selectedDayElevationPx
                )
            }
            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_monthLabelEnabled)) {
                monthLabelEnabled = attrArray.getBoolean(
                        R.styleable.DayOfWeekPicker_monthLabelEnabled,
                        monthLabelEnabled
                )
            }
            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_weekDayTextAppearance)) {
                weekDayTextAppearanceResId = attrArray.getResourceId(
                        R.styleable.DayOfWeekPicker_weekDayTextAppearance,
                        weekDayTextAppearanceResId
                )
            }
            if (attrArray.hasValue(R.styleable.DayOfWeekPicker_dayNumberTextAppearance)) {
                dayNumberTextAppearanceResId = attrArray.getResourceId(
                        R.styleable.DayOfWeekPicker_dayNumberTextAppearance,
                        dayNumberTextAppearanceResId
                )
            }
            attrArray.recycle()
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        return super.onSaveInstanceState()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
    }

    class SavedState : View.BaseSavedState {
        var stateToSave: Int = 0

        constructor(`in`: Parcel) : super(`in`) {
            this.stateToSave = `in`.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(this.stateToSave)
        }

        companion object {
            @JvmField val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(parcel: Parcel): SavedState {
                    return SavedState(parcel)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    open val weekAdapterProvider: (position: Int) -> DayOfWeekAdapter = { position ->
        val workingBaseTime = moveCurrentDateBasedOnPagerPosition(position)

        DefaultDayOfWeekAdapter(
                workingBaseTime.withTimeAtStartWeek().toDate(),
                workingBaseTime.withTimeAtEndWeek().toDate(),
                currentDate,
                selectedDate,
                object : DayOfWeekAdapter.DateSelectedListener {
                    override fun dateSelected(date: Date) {
                        this@DayOfWeekPicker.dateSelected(date)
                    }
                },
                dayDataMap,
                defaultBinder,
                R.layout.dwp_day,
                { date ->
                    DayData(date = date)
                }
        )
    }


    internal val onWeekChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            //do nothing
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            //do nothing
        }

        override fun onPageSelected(position: Int) {
            onVisibleDateSetChangedListener?.let {
                it.onVisibleDateSetChanged(getVisibleDateSetForPagerPosition(position))
            }
            setupMonthLabel()
        }
    }

    private fun moveCurrentDateBasedOnPagerPosition(position: Int): DateTime {
        return DateTime(currentDate).plusDays((position - weekPagerAdapter.initialPosition) * 7)
    }

    private fun getVisibleDateSetForPagerPosition(position : Int) : VisibleDateSet {
        val movedCurrentDate = moveCurrentDateBasedOnPagerPosition(position)
        return VisibleDateSet(
                movedCurrentDate.withTimeAtStartWeek().toDate(),
                movedCurrentDate.withTimeAtEndWeek().toDate()
        )
    }

    fun getVisibleDateSet() : VisibleDateSet {
        return getVisibleDateSetForPagerPosition(dwp_vWeekPager.currentItem)
    }

    protected fun dateSelected(date: Date) {

        selectedDate = date
        dateSelectedListener?.onDateSelected(date)
    }

    private fun setupDefaultBinder() {
        defaultBinder.bgDefault = bgDefault
        defaultBinder.bgSelected = bgSelected
        defaultBinder.bgToday = bgToday
        defaultBinder.bgPast = bgPast

        defaultBinder.txtColorDefault = txtColorDefault
        defaultBinder.txtColorSelected = txtColorSelected
        defaultBinder.txtColorToday = txtColorToday
        defaultBinder.txtColorPast = txtColorPast

        defaultBinder.indicatorColorDefault = indicatorColor
        defaultBinder.indicatorColorSelected = indicatorColorSelected
        defaultBinder.indicatorColorToday = indicatorColorToday
        defaultBinder.indicatorColorPast = indicatorColorPast
        defaultBinder.indicatorWidth = indicatorWidth

        defaultBinder.selectedDayElevationEnabled = selectedDayElevationEnabled
        defaultBinder.selectedDayElevationPx = selectedDayElevationPx

        defaultBinder.weekDayTextAppearanceResId = weekDayTextAppearanceResId
        defaultBinder.dayNumberTextAppearanceResId = dayNumberTextAppearanceResId
    }

    private fun setupMonthLabel() {

        dwp_vMonthName.visibility = if (monthLabelEnabled) View.VISIBLE else View.GONE
        if (monthLabelEnabled) {
            TextViewCompat.setTextAppearance(dwp_vMonthName, monthLabelTextAppearanceResId)

            dwp_vMonthName.text =
                    SimpleDateFormat(monthLabelDateFormat, Locale.getDefault())
                            .format(moveCurrentDateBasedOnPagerPosition(dwp_vWeekPager.currentItem).withTimeAtStartWeek().toDate())
        }

    }

    fun setDefaultDayViewBinder(defaultBinder: DayOfWeekBinder) {
        this.defaultBinder
        setupDefaultBinder()
    }

    fun refreshWeekAdapters() {
        weekAdapterMap.values
                .filter { it is DefaultDayOfWeekAdapter }
                .forEach {
                    (it as DefaultDayOfWeekAdapter).let {

                        it.selectedDate = selectedDate
                        it.currentDate = currentDate

                        it.notifyDataSetChanged()
                    }
                }
    }

    fun removeDayData(date : Date){
        dayDataMap.remove(DateTime(date).withTimeAtStartOfDay().millis)
    }

    fun putDayData(data : DayData){
        dayDataMap.put(DateTime(data.date).withTimeAtStartOfDay().millis, data)
    }

    fun putDayData(data : List<DayData>){
        for(dayData in data){
            dayDataMap.put(DateTime(dayData.date).withTimeAtStartOfDay().millis, dayData)
        }
    }

    fun clearDateData(){
        dayDataMap.clear()
    }


}