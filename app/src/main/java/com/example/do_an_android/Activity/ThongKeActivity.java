package com.example.do_an_android.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Adapter.ThongKe1Adapter;
import com.example.do_an_android.Adapter.ThongKe2Adapter;
import com.example.do_an_android.Adapter.ThongKe3Adapter;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.Model.ThongKe2Model;
import com.example.do_an_android.Model.ThongKe3Model;
import com.example.do_an_android.R;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

public class ThongKeActivity extends AppCompatActivity {
    RecyclerView RecyclerThongKe1, RecyclerThongKe2, RecyclerThongKe3;
    PieChart pieChart;
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    String[] spinnerOptions = {"TẤT CẢ", "CHỌN NGÀY"};
    private boolean isChonNgaySelected = false;
    Calendar selectedDate;
    SimpleDateFormat dateFormatter;

    public ArrayList<ThongKe2Model> thongKe2List;
    public ArrayList<ThongKe3Model> thongKe3List;
    public ArrayList<ThongKe3Model> thongKe1List;
    public ThongKe2Adapter thongKe2Adapter;
    public ThongKe3Adapter thongKe3Adapter;
    public ThongKe1Adapter thongKe1Adapter;
    private String phantram;

    public void exportToPDF() {
        try {
            String pdfFilePath = Environment.getExternalStorageDirectory() + "/Documents/ThongKeBanLinhKienMayTinh.pdf";
            PdfWriter writer = new PdfWriter(pdfFilePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            PdfFont timesNewRoman = PdfFontFactory.createFont("assets/times.ttf", "Identity-H");

            Paragraph title = new Paragraph("BÁO CÁO DOANH THU BÁN LINH KIỆN MÁY TÍNH").setFont(timesNewRoman).setFontSize(18).setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 1})).useAllAvailableWidth();
            table.addCell(new Paragraph("TÊN LINH KIỆN").setFont(timesNewRoman).setBold().setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Paragraph("DOANH THU").setFont(timesNewRoman).setBold().setTextAlignment(TextAlignment.CENTER));
            int totalRevenue = 0;
            for (ThongKe2Model model : thongKe2List) {
                if (!model.getTongtienban().equals("0")) {
                    table.addCell(new Paragraph(model.getTenllinhkien()).setFont(timesNewRoman).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Paragraph(model.getTongtienban()).setFont(timesNewRoman).setTextAlignment(TextAlignment.CENTER));
                    totalRevenue += Integer.parseInt(model.getTongtienban());
                }
            }
            table.addCell(new Paragraph("TỔNG CỘNG").setFont(timesNewRoman).setBold().setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Paragraph(String.valueOf(totalRevenue)).setFont(timesNewRoman).setBold().setTextAlignment(TextAlignment.CENTER));

            document.add(table);
            Paragraph tableTitle = new Paragraph().setTextAlignment(TextAlignment.CENTER);
            tableTitle.add(new Text("BẢNG 1. ").setFont(timesNewRoman).setItalic().setBold());
            tableTitle.add(new Text("Doanh thu của từng loại linh kiện.").setFont(timesNewRoman).setItalic());
            document.add(tableTitle);

            document.add(new Paragraph("\n"));

            Table table2 = new Table(UnitValue.createPercentArray(new float[]{2, 1, 1})).useAllAvailableWidth();
            table2.addCell(new Paragraph("TÊN LINH KIỆN").setFont(timesNewRoman).setBold().setTextAlignment(TextAlignment.CENTER));
            table2.addCell(new Paragraph("SỐ LƯỢNG").setFont(timesNewRoman).setBold().setTextAlignment(TextAlignment.CENTER));
            table2.addCell(new Paragraph("%").setFont(timesNewRoman).setBold().setTextAlignment(TextAlignment.CENTER));
            double totalPercentage = 0.0;
            int totalQuantity = 0;
            for (ThongKe3Model model : thongKe3List) {
                if (model.getSoluongban() != 0) {
                    table2.addCell(new Cell().add(new Paragraph(model.getTenllinhkien()).setFont(timesNewRoman).setTextAlignment(TextAlignment.CENTER)));
                    table2.addCell(new Cell().add(new Paragraph(String.valueOf(model.getSoluongban())).setFont(timesNewRoman).setTextAlignment(TextAlignment.CENTER)));
                    table2.addCell(new Cell().add(new Paragraph(String.valueOf(model.getPhantram())).setFont(timesNewRoman).setTextAlignment(TextAlignment.CENTER)));

                    totalQuantity += model.getSoluongban();
                    totalPercentage += model.getPhantram();
                }
            }
            table2.addCell(new Paragraph("TỔNG CỘNG").setFont(timesNewRoman).setBold().setTextAlignment(TextAlignment.CENTER));
            table2.addCell(new Paragraph(String.valueOf(totalQuantity)).setFont(timesNewRoman).setBold().setTextAlignment(TextAlignment.CENTER));
            table2.addCell(new Paragraph(String.valueOf(totalPercentage)).setFont(timesNewRoman).setBold().setTextAlignment(TextAlignment.CENTER));
            document.add(table2);
            Paragraph tableTitle2 = new Paragraph().setTextAlignment(TextAlignment.CENTER);
            tableTitle2.add(new Text("BẢNG 2. ").setFont(timesNewRoman).setItalic().setBold());
            tableTitle2.add(new Text("Số lượng bán và % tương ứng của từng loại linh kiện.").setFont(timesNewRoman).setItalic());
            document.add(tableTitle2);

            document.close();

            Snackbar.make(findViewById(android.R.id.content), "XUẤT FILE PDF THÀNH CÔNG. ĐÃ LƯU TẠI " + pdfFilePath, Snackbar.LENGTH_LONG)
                    .setAction("OK", v -> {
                    })
                    .show();

        } catch (IOException e) {
            Log.e("ExportToPDF", "Error exporting PDF", e);
            Toast.makeText(getApplicationContext(), "Lỗi khi xuất file PDF", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongke);
        pieChart = findViewById(R.id.piechart);
        spinner = findViewById(R.id.spinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        Button btnXuatFile = findViewById(R.id.button_xuatfile);
        btnXuatFile.setOnClickListener(v -> exportToPDF());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = spinnerOptions[position];
                isChonNgaySelected = selectedItem.equals("CHỌN NGÀY");
                if (selectedItem.equals("CHỌN NGÀY")) {
                    showDatePickerDialog();
                } else if (selectedItem.equals("TẤT CẢ")) {
                    setControl();
                    setThongKe1();
                    setThongKe2();
                    loadThongKe2();
                    setThongKe3();
                    loadThongKe3();
                    isChonNgaySelected = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        setControl();
        setThongKe1();
        setThongKe2();
        loadThongKe2();
        setThongKe3();
        loadThongKe3();

        thongKe1Adapter.setOnItemClickListener(thongKe3Model -> {
            phantram = String.valueOf(thongKe3Model.getPhantram());
            Toast.makeText(getApplicationContext(), "PHẦN TRĂM: " + phantram, Toast.LENGTH_SHORT).show();
        });
    }

    private String convertDateFormat(String inputDate) {
        String outputDate = "";
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            Date date = inputFormat.parse(inputDate);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            outputDate = outputFormat.format(date);
        } catch (Exception e) {
            Log.e("ConvertDateFormat", "Unknown error", e);
        }
        return outputDate;
    }

    private void showDatePickerDialog() {
        final Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(ThongKeActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            selectedDate = Calendar.getInstance();
            selectedDate.set(year, monthOfYear, dayOfMonth);
            String selectedDateStr = dateFormatter.format(selectedDate.getTime());
            String ngay = convertDateFormat(selectedDateStr);

            Toast.makeText(getApplicationContext(), "Đã chọn ngày: " + ngay, Toast.LENGTH_SHORT).show();
            onDateSelected(selectedDateStr);

            int position = 0;
            String selectedItem = spinnerOptions[position];
            isChonNgaySelected = selectedItem.equals("CHỌN NGÀY");
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void onDateSelected(String selectedDateStr) {
        String ngay = convertDateFormat(selectedDateStr);
        loadThongKe2TheoNgay(ngay);
        loadThongKe3TheoNgay(ngay);
        refreshRecyclerViews();
        isChonNgaySelected = false;
    }

    private void loadThongKe2() {
        RequestQueue queue = Volley.newRequestQueue(this);
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlThongKe2, response -> {
            thongKe2List.clear();
            int tongTien = 0;
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String tenLinhKien = jsonObject.getString("tenllinhkien");
                    String tongTienBan = jsonObject.getString("tongtienban");
                    thongKe2List.add(new ThongKe2Model(tenLinhKien, tongTienBan));
                    tongTien += Integer.parseInt(tongTienBan);
                } catch (JSONException e) {
                    Log.e("loadthongke2", "Unknown error", e);
                }
            }
            thongKe2Adapter.notifyDataSetChanged();
            TextView tongtien = findViewById(R.id.tong);
            tongtien.setText(String.valueOf(tongTien));
            isChonNgaySelected = false;
        }, error -> {
        });
        queue.add(jsonArrayRequest);
    }

    private void loadThongKe2TheoNgay(String ngay) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Server.urlThongKe2TheoNgay + "?ngay=" + ngay;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONArray response) {
                thongKe2List.clear();
                int tongTien = 0;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String tenLinhKien = jsonObject.getString("tenllinhkien");
                        String tongTienBan = jsonObject.getString("tongtienban");
                        thongKe2List.add(new ThongKe2Model(tenLinhKien, tongTienBan));
                        tongTien += Integer.parseInt(tongTienBan);
                    } catch (JSONException e) {
                    }
                }
                thongKe2Adapter.notifyDataSetChanged();
                TextView tongtien = findViewById(R.id.tong);
                tongtien.setText(String.valueOf(tongTien));
                isChonNgaySelected = false;
            }
        }, error -> {
        });
        queue.add(jsonArrayRequest);
    }

    private void loadThongKe3TheoNgay(String ngay) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Server.urlThongKe3TheoNgay + "?ngay=" + ngay;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONArray response) {
                thongKe3List.clear();
                List<ThongKe3Model> newDataList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String tenLinhKien = jsonObject.getString("tenllinhkien");
                        int soluongban = jsonObject.getInt("soluongban");
                        double phantram = jsonObject.getDouble("phantram");
                        thongKe3List.add(new ThongKe3Model(tenLinhKien, soluongban, phantram));
                        newDataList.add(new ThongKe3Model(tenLinhKien, soluongban, phantram));
                    } catch (JSONException ignored) {
                    }
                }

                if (thongKe3List.isEmpty()) {
                    pieChart.clearChart();
                    pieChart.addPieSlice(new PieModel("No Data", 100, Color.GRAY)); // Màu xám
                    pieChart.invalidate();
                } else {
                    thongKe3Adapter.notifyDataSetChanged();
                    thongKe1Adapter.updateData(newDataList);
                    pieChart.clearChart();
                    HashMap<String, Integer> colorMap = new HashMap<>();
                    thongKe1Adapter.setColorMap(colorMap);
                    for (ThongKe3Model model : thongKe3List) {
                        double phantram = model.getPhantram();
                        if (phantram > 0) {
                            int color = getRandomColor();
                            colorMap.put(model.getTenllinhkien(), color);
                            PieModel pieModel = new PieModel(model.getTenllinhkien(), (float) phantram, color);
                            pieChart.addPieSlice(pieModel);
                        }
                    }
                    thongKe1Adapter.setColorMap(colorMap);
                    pieChart.invalidate();
                    pieChart.startAnimation();
                }
                isChonNgaySelected = false;
            }
        }, error -> {
        });
        queue.add(jsonArrayRequest);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshRecyclerViews() {
        thongKe1List.clear();
        thongKe1Adapter.notifyDataSetChanged();
        thongKe2List.clear();
        thongKe2Adapter.notifyDataSetChanged();
        thongKe3List.clear();
        thongKe3Adapter.notifyDataSetChanged();
    }

    private void setControl() {
        RecyclerThongKe2 = findViewById(R.id.recycler_thongke_loailinhkien2);
        RecyclerThongKe3 = findViewById(R.id.recycler_thongke_loailinhkien3);
        RecyclerThongKe1 = findViewById(R.id.recycler_thongke_loailinhkien1);
    }

    private int getRandomColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private void loadThongKe3() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlThongKe3, new Response.Listener<JSONArray>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONArray response) {
                thongKe3List.clear();
                List<ThongKe3Model> newDataList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String tenLinhKien = jsonObject.getString("tenllinhkien");
                        int soluongban = jsonObject.getInt("soluongban");
                        double phantram = jsonObject.getDouble("phantram");
                        thongKe3List.add(new ThongKe3Model(tenLinhKien, soluongban, phantram));
                        newDataList.add(new ThongKe3Model(tenLinhKien, soluongban, phantram));
                    } catch (JSONException ignored) {
                    }
                }
                if (thongKe3List.isEmpty()) {
                    pieChart.clearChart();
                    pieChart.addPieSlice(new PieModel("No Data", 100, Color.GRAY));
                    pieChart.invalidate();
                } else {
                    thongKe3Adapter.notifyDataSetChanged();
                    thongKe1Adapter.updateData(newDataList);

                    pieChart.clearChart();
                    HashMap<String, Integer> colorMap = new HashMap<>();
                    thongKe1Adapter.setColorMap(colorMap);
                    for (ThongKe3Model model : thongKe3List) {
                        double phantram = model.getPhantram();
                        if (phantram > 0) {
                            int color = getRandomColor();
                            colorMap.put(model.getTenllinhkien(), color);
                            PieModel pieModel = new PieModel(model.getTenllinhkien(), (float) phantram, color);
                            pieChart.addPieSlice(pieModel);
                        }
                    }
                    thongKe1Adapter.setColorMap(colorMap);
                    pieChart.invalidate();
                    pieChart.startAnimation();
                }
                isChonNgaySelected = false;
            }
        }, error -> {
        });
        queue.add(jsonArrayRequest);
    }

    private void setThongKe1() {
        thongKe1List = new ArrayList<>();
        RecyclerThongKe1.setLayoutManager(new GridLayoutManager(this, 1));
        RecyclerThongKe1.setItemAnimator(new DefaultItemAnimator());
        thongKe1Adapter = new ThongKe1Adapter(this, thongKe1List);
        RecyclerThongKe1.setAdapter(thongKe1Adapter);
    }

    private void setThongKe2() {
        thongKe2List = new ArrayList<>();
        RecyclerThongKe2.setLayoutManager(new GridLayoutManager(this, 1));
        RecyclerThongKe2.setItemAnimator(new DefaultItemAnimator());
        thongKe2Adapter = new ThongKe2Adapter(this, R.layout.item_thongke_loailinhkien2, thongKe2List);
        RecyclerThongKe2.setAdapter(thongKe2Adapter);
    }

    private void setThongKe3() {
        thongKe3List = new ArrayList<>();
        RecyclerThongKe3.setLayoutManager(new GridLayoutManager(this, 1));
        RecyclerThongKe3.setItemAnimator(new DefaultItemAnimator());
        thongKe3Adapter = new ThongKe3Adapter(this, R.layout.item_thongke_loailinhkien3, thongKe3List);
        RecyclerThongKe3.setAdapter(thongKe3Adapter);
    }
}