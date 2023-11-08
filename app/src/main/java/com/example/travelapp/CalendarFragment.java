package com.example.travelapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CalendarFragment extends Fragment {
    ActionBar actionBar;
    private MaterialCalendarView calendarView;
    private PreferenceManager pref;
    private RecyclerView calendarRecyclerView;
    private CalendarAdapter calendarAdapter;
    private SharedPreferences sharedPreferences;
    private ArrayList<CalendarData> calendarDataList;
    private ArrayList<CalendarData> primaryCalendarDataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

//        pref.newClear(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // 초기화
        init(view);

        // 툴바
        actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("기록하고 싶은");

        // 캘린더
        calendarView = view.findViewById(R.id.calendarView);
        setCalendarView();

        calendarView.setOnTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재날짜로 다이얼로그 띄우기
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dateDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    }
                }, year, month, day);
                dateDialog.show();
            }
        });

        // 리사이클러뷰
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        calendarRecyclerView.setHasFixedSize(true);
        calendarRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        calendarAdapter = new CalendarAdapter(calendarDataList);
        calendarRecyclerView.setAdapter(calendarAdapter);

        // 데이터 가져오기
        getSharedPreferencesData();

        // 초기 날짜 뷰
        CalendarDay today = CalendarDay.today();
        compareDate(String.valueOf(today.getYear()), String.valueOf(today.getMonth()), String.valueOf(today.getDay()));

        // 캘린더 날짜 변경
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                calendarDataList.clear();

                String clickedYear = String.valueOf(date.getYear());
                String clickedMonth = String.valueOf(date.getMonth());
                String clickedDay = String.valueOf(date.getDay());

                compareDate(clickedYear, clickedMonth, clickedDay);
            }
        });

        // 점찍기
        for (int i = 0; i < primaryCalendarDataList.size(); i++) {
            int year = Integer.parseInt(primaryCalendarDataList.get(i).getCalendar_year());
            int month = Integer.parseInt(primaryCalendarDataList.get(i).getCalendar_month());
            int day = Integer.parseInt(primaryCalendarDataList.get(i).getCalendar_day());
            calendarView.addDecorators(new EventDecorator(Color.RED, Collections.singleton(CalendarDay.from(year, month, day)), this.getActivity()));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        calendarAdapter.notifyDataSetChanged();
    }

    private void init(View view) {
        pref = new PreferenceManager();
        calendarDataList = new ArrayList<>();
        primaryCalendarDataList = new ArrayList<>();
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu,inflater);
//        inflater.inflate(R.menu.menu_calendar, menu);
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.menu_calendar_add) {
//            Toast toast = Toast.makeText(getContext(), "add click", Toast.LENGTH_SHORT);
//            toast.show();
//
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void setCalendarView() {
        MaterialCalendarView.StateBuilder builder = calendarView.state().edit();
        builder.setMinimumDate(CalendarDay.from(2017, 1, 1));
        builder.setMaximumDate(CalendarDay.from(2030, 12, 31));
        builder.commit();

        // 좌우 화살표 사이 연, 월의 폰트 스타일
        calendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader);

        // 좌우 화살표 가운데의 연/월이 보이는 방식 커스텀
        calendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                // CalendarDay라는 클래스는 LocalDate 클래스를 기반으로 만들어진 클래스다
                // 때문에 MaterialCalendarView에서 연/월 보여주기를 커스텀하려면 CalendarDay 객체의 getDate()로 연/월을 구한 다음 LocalDate 객체에 넣어서
                // LocalDate로 변환하는 처리가 필요하다
                LocalDate inputText = day.getDate();
                String[] calendarHeaderElements = inputText.toString().split("-");
                StringBuilder calendarHeaderBuilder = new StringBuilder();
                calendarHeaderBuilder.append(calendarHeaderElements[0])
                        .append(" ")
                        .append(calendarHeaderElements[1]);
                return calendarHeaderBuilder.toString();
            }
        });

        // decorator 달력 효과
        calendarView.addDecorators(new SundayDecorator(), new SaturdayDecorator(), new TodayDecorator(this.getContext()));
    }

    public void getSharedPreferencesData() {
        // 쉐어드 모든 키 벨류 가져오기
        sharedPreferences = this.getActivity().getSharedPreferences("complete_data", MODE_PRIVATE);
        Collection<?> col_val = sharedPreferences.getAll().values();
        Iterator<?> it_val = col_val.iterator();
        Collection<?> col_key = sharedPreferences.getAll().keySet();
        Iterator<?> it_key = col_key.iterator();

        while (it_val.hasNext() && it_key.hasNext()) {
            String key = (String) it_key.next();
            String value = (String) it_val.next();
            try {
                JSONObject jsonObject = new JSONObject(value);
                String detail = (String) jsonObject.getString("detail");
                String place = (String) jsonObject.getString("place");
                String memo = (String) jsonObject.getString("memo");
                String category = (String) jsonObject.getString("category");
                String with = (String) jsonObject.getString("withText");
                String year = (String) jsonObject.getString("yearText");
                String month = (String) jsonObject.getString("monthText");
                String day = (String) jsonObject.getString("dayText");

//                if (clickedYear.equals(year) && clickedMonth.equals(month) && clickedDay.equals(day)) {
//                    calendarAdapter.addItem(new CalendarData(key, detail, place, memo, category, year, month, day));
//                }

//                calendarAdapter.addItem(new CalendarData(key, detail, place, memo, category, year, month, day));
                primaryCalendarDataList.add(new CalendarData(key, detail, place, memo, category, year, month, day));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
//            calendarAdapter.notifyDataSetChanged();
        }
    }

    // 선택 날짜와 데이터 날짜 비교
    public void compareDate(String year, String month, String day) {
        for (int i = 0; i < primaryCalendarDataList.size(); i++) {
            String dataYear = primaryCalendarDataList.get(i).getCalendar_year();
            String dataMonth = primaryCalendarDataList.get(i).getCalendar_month();
            String dataDay = primaryCalendarDataList.get(i).getCalendar_day();

            if(year.equals(dataYear) && month.equals(dataMonth) && day.equals(dataDay)) {
                calendarAdapter.addItem(primaryCalendarDataList.get(i));
            }
            calendarAdapter.notifyDataSetChanged();
        }
    }

}