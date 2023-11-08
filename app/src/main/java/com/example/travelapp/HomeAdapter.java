package com.example.travelapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.databinding.CompleteDialogBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<HomeData> homeDataList;
    private AlertDialog listDialog;
    private AlertDialog completeDialog;
    PreferenceManager preferenceManager = new PreferenceManager();
    private String editDetail;
    private String editPlace;
    private String editMemo;
    private String textCategory;

    public HomeAdapter(ArrayList<HomeData> homeDataList) {
        this.homeDataList = homeDataList;
    }

    public void addItem(HomeData homeData) {
        homeDataList.add(0, homeData);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        HomeData homeData = homeDataList.get(position);
        holder.setItem(homeData);
    }

    @Override
    public int getItemCount() {
        return homeDataList.size();
    }

    // 뷰홀더
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryImage;
        private TextView detailItemTextView;
        private TextView placeItemTextView;
        private TextView memoItemTextView;
        private EditText withEditText;
        private EditText whenEditText;
        private ImageView calendarImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            detailItemTextView = (TextView) itemView.findViewById(R.id.detailItemTextView);
            placeItemTextView = (TextView) itemView.findViewById(R.id.placeItemTextview);
            memoItemTextView = (TextView) itemView.findViewById(R.id.memoItemTextview);
            categoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setItems(R.array.recyclerView_array, dialogListener);
                    builder.setNegativeButton("취소", null);
                    listDialog = builder.create();
                    listDialog.show();
                }
            });
        }

        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = getAdapterPosition();
                String key = homeDataList.get(position).getDate();
                if (dialog == listDialog) {
                    String[] lists = {"완료하기", "수정하기", "삭제하기"};
                    String list = lists[which];

                    switch (list) {
                        case "완료하기":
                            completeView();
                            break;
                        case "수정하기":
                            modifyView(key);
                            break;
                        case "삭제하기":
                            deleteView(key, position);
                            break;
                        default:
                            break;
                    }
                } else if (dialog == completeDialog) {
                 Log.d("입성", "입성");
                 changeData(key, position);
                }
            }
        };

        public void setItem(HomeData homeData) {
            detailItemTextView.setText(homeData.getHome_detail());
            placeItemTextView.setText(homeData.getHome_place());
            memoItemTextView.setText(homeData.getHome_memo());
            categoryImage.setImageResource(homeData.getHome_image_id(homeData.getHome_category()));
        }

        // 완료 처리
        public void completeView() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
            View dialogView = inflater.inflate(R.layout.complete_dialog, null);
            builder.setView(dialogView);

            withEditText = dialogView.findViewById(R.id.withEditText);
            whenEditText = dialogView.findViewById(R.id.whenEditText);
            calendarImageView = dialogView.findViewById(R.id.calendar_icon);

            calendarImageView.setOnClickListener(view -> {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dateDialog = new DatePickerDialog(itemView.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        whenEditText.setText(year + "/" + (month+1) + "/" + dayOfMonth);
                    }
                }, year, month, day);
                dateDialog.show();
            });

            builder.setPositiveButton("확인", dialogListener);
            builder.setNegativeButton("취소", null);
            completeDialog = builder.create();
            completeDialog.show();
        }

        //완료 후 recyclerview 삭제
        public void changeData(String key, int position) {

            String withText = withEditText.getText().toString();

            String dateText = whenEditText.getText().toString();
            String yearText = dateText.split("/")[0];
            String monthText = dateText.split("/")[1];
            String dayText = dateText.split("/")[2];

            // 기존 데이터 가져오기
            String value = preferenceManager.getString("main_data", itemView.getContext(), key);

            try {
                JSONObject jsonObject = new JSONObject(value);
                editDetail = (String) jsonObject.getString("detail");
                editPlace = (String) jsonObject.getString("place");
                editMemo = (String) jsonObject.getString("memo");
                textCategory = (String) jsonObject.getString("category");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            String save_form = "{\"detail\":\"" + editDetail + "\",\"place\":\"" + editPlace + "\",\"memo\":\"" + editMemo + "\",\"category\":\"" + textCategory + "\",\"withText\":\"" + withText + "\",\"yearText\":\"" + yearText + "\",\"monthText\":\"" + monthText + "\",\"dayText\":\"" + dayText + "\"}";

            Log.d("입성5", save_form);

            // key값이 겹치지 않도록 현재 시간으로 부여
            long now = System.currentTimeMillis();
            Date mDate = new Date(now);
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String getTime = simpleDate.format(mDate).toString();

            preferenceManager.setNewString(itemView.getContext(), getTime, save_form);
            preferenceManager.removeKey("main_data", itemView.getContext(), key);
            homeDataList.remove(position);
            notifyItemRemoved(position);
        }

        // recyclerview 수정
        public void modifyView(String key) {
            Intent intent = new Intent(itemView.getContext(), MainModifyActivity.class);
            intent.putExtra("key", key);
            itemView.getContext().startActivity(intent);
        }

        // recyclerview 삭제
        public void deleteView(String key, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("알림");
            builder.setMessage("정말 삭제 하시겠습니까?");
            builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    preferenceManager.removeKey("main_data", itemView.getContext(), key);
                    homeDataList.remove(position);
                    notifyItemRemoved(position);
                }
            });
            builder.setNegativeButton("취소", null);
            builder.create().show();
        }

    }
}
