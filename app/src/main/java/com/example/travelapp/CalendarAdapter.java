package com.example.travelapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private ArrayList<CalendarData> calendarDataList = new ArrayList<>();
    private CalendarData calendarData;

    public CalendarAdapter(ArrayList<CalendarData> calendarDataList) {
        this.calendarDataList = calendarDataList;
    }
    public void addItem(CalendarData calendarData) {
        calendarDataList.add(0, calendarData);
    }

    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_calendar, parent, false) ;
        CalendarAdapter.ViewHolder viewHolder = new CalendarAdapter.ViewHolder(view) ;

        return viewHolder;
    }

    @Override
    public int getItemCount() { return calendarDataList.size(); }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final CalendarAdapter.ViewHolder holder, final int position) {
        calendarData = calendarDataList.get(position);
        holder.setItem(calendarData);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView detailTextView;
        private Button detailButton;
        ViewHolder(View itemView){
            super(itemView);
            detailTextView = itemView.findViewById(R.id.calendar_detailTextView);
            detailButton = itemView.findViewById(R.id.calendar_detailButton);

            // 메모 아이템 안에 있는 상세보기 버튼을 클릭하여 상세보기로 이동
            detailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context vContext = view.getContext();

                    Intent intent = new Intent(vContext, CalendarDetailActivity.class);
                    intent.putExtra("key", calendarDataList.get(getAdapterPosition()).getDate());
                    vContext.startActivity(intent);
                }
            });
        }

        public void setItem(CalendarData calendarData) {
            detailTextView.setText(calendarData.getCalendar_detail());
        }
    }

}
