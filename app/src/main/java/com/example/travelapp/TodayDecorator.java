package com.example.travelapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.threeten.bp.LocalDate;


public class TodayDecorator implements DayViewDecorator {

    private CalendarDay date;
    private Drawable drawable;

    public TodayDecorator(Context context) {
        date = CalendarDay.today();
        drawable = ContextCompat.getDrawable(context, R.drawable.today_background);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
//        view.addSpan(new RelativeSizeSpan(1.4f));
//        view.addSpan(new BackgroundColorSpan(Color.parseColor("#FCBACB")));
        view.setSelectionDrawable(drawable);
    }

    public void setDate(LocalDate date) {
        this.date = CalendarDay.from(date);
    }
}
