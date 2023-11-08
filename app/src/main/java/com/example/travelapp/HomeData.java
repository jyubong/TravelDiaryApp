package com.example.travelapp;

public class HomeData {
    private String home_detail;
    private String home_place;
    private String home_memo;
    private String home_category;
    private String home_image_id;
    private String date;

    // 생성자
    public HomeData(String date, String home_detail, String home_place, String home_memo, String home_category) {
        this.date = date;
        this.home_detail = home_detail;
        this.home_place = home_place;
        this.home_memo = home_memo;
        this.home_category = home_category;
    }

    public String getDate() { return date ; }

    public String getHome_detail() {
        return home_detail;
    }

    public void setHome_detail(String home_detail) {
        this.home_detail = home_detail;
    }

    public String getHome_place() {
        return home_place;
    }

    public void setHome_place(String home_place) {
        this.home_place = home_place;
    }

    public String getHome_memo() {
        return home_memo;
    }

    public void setHome_memo(String home_memo) {
        this.home_memo = home_memo;
    }

    public String getHome_category() {
        return home_category;
    }

    public void setHome_category(String home_category) {
        this.home_category = home_category;
    }

    public int getHome_image_id(String home_category) {
        switch (home_category) {
            case "맛집":
                return R.drawable.icon_food;
            case "카페":
                return R.drawable.icon_caffe;
            case "전시회":
                return R.drawable.icon_exhibit;
            case "축제":
                return R.drawable.icon_festival;
            case "공연":
                return R.drawable.icon_show;
            case "국내 여행":
                return R.drawable.icon_domestic;
            case "해외 여행":
                return R.drawable.icon_overseas;
            default:
                return R.drawable.icon_ex;
        }
    }
}
