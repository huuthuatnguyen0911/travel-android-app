package com.appbanhang.activity;

import android.widget.SearchView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.appbanhang.R;
import com.appbanhang.adapter.LoaiSanPhamAdapter;
import com.appbanhang.model.GioHang;
import com.appbanhang.model.LoaiSanPham;
import com.appbanhang.model.SanPham;
import com.appbanhang.adapter.SanPhamAdapter;
import com.appbanhang.ultil.CheckConnection;
import com.appbanhang.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<LoaiSanPham> mangLoaisanpham;
    LoaiSanPhamAdapter loaiSanPhamAdapter;

    ArrayList<SanPham> mangSanpham;
    ArrayList<SanPham> mangSanpham2;
    SanPhamAdapter sanphamAdapter;
    DrawerLayout drawerLayout;
    Animation in, out;
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationView;
    ListView lvManHinhChinh;
    Button Logout;
    SearchView searchView;
    int id = 0;
    public static ArrayList<GioHang> mangGioHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();

        searchView.setOnQueryTextListener ( new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sanphamAdapter.getFilter ().filter ( newText );

                return false;
            }
        } );

        Logout.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intentL = new Intent (MainActivity.this, ActivityLogin.class);
                startActivity ( intentL );
                finish ();
                Toast.makeText ( MainActivity.this, "Bạn đã Log Out thành công", Toast.LENGTH_SHORT ).show ();
            }
        } );

        if (CheckConnection.haveNetworkConnection(MainActivity.this)) {
            //actionBar();
            actiontoolbar ();
            addEEventViewFlipper();
            getdulieuSpmoinhat();
            getdulieuloaisanpham();
            CatchOnItemListView();
        } else {
            CheckConnection.showToast_Short(MainActivity.this, "khong co ket noi intenret");
            finish();
        }
    }


    private void CatchOnItemListView() {
        lvManHinhChinh.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //trang chính
                    case 0:
                        if (CheckConnection.haveNetworkConnection ( getApplicationContext () )) {
                            Intent intent = new Intent ( MainActivity.this, MainActivity.class );
                            startActivity ( intent );
                        } else {
                            CheckConnection.showToast_Short ( getApplicationContext (), "Bạn hãy kiểm tra lại kết nối //" );
                        }
                        drawerLayout.closeDrawer ( GravityCompat.START );
                        break;
                    //Tour miền trung
                    case 1:
                        if(CheckConnection.haveNetworkConnection ( getApplicationContext () )){
                            Intent intent = new Intent (MainActivity.this, TourMienTrungActivity.class);
                            startActivity ( intent );
                            intent.putExtra ( "idloaisanpham", mangLoaisanpham.get ( position ).getId () );
                        }else{
                            CheckConnection.showToast_Short ( getApplicationContext (),"Bạn hãy kiểm tra lại kết nối //" );
                        }
                        drawerLayout.closeDrawer ( GravityCompat.START );
                        break;
                    //Tour miền bắc
                    case 2:
                        if(CheckConnection.haveNetworkConnection ( getApplicationContext () )){
                            Intent intent = new Intent (MainActivity.this, TourMienBacActivity.class);
                            startActivity ( intent );
                            intent.putExtra ( "idloaisanpham", mangLoaisanpham.get ( position ).getId () );
                        }else{
                            CheckConnection.showToast_Short ( getApplicationContext (),"Bạn hãy kiểm tra lại kết nối //" );
                        }
                        drawerLayout.closeDrawer ( GravityCompat.START );
                        break;
                    //Tour miền nam
                    case 3:
                        if(CheckConnection.haveNetworkConnection ( getApplicationContext () )){
                            Intent intent = new Intent (MainActivity.this, TourMienNamActivity.class);
                            startActivity ( intent );
                            intent.putExtra ( "idloaisanpham", mangLoaisanpham.get ( position ).getId () );
                        }else{
                            CheckConnection.showToast_Short ( getApplicationContext (),"Bạn hãy kiểm tra lại kết nối //" );
                        }
                        drawerLayout.closeDrawer ( GravityCompat.START );
                        break;
//                    //Liên hệ
                    case 4:
                        if(CheckConnection.haveNetworkConnection ( getApplicationContext () )){
                            Intent intent = new Intent (MainActivity.this, LienHeActivity.class);

                            startActivity ( intent );
                        }else{
                            CheckConnection.showToast_Short ( getApplicationContext (),"Bạn hãy kiểm tra lại kết nối //" );
                        }
                        drawerLayout.closeDrawer ( GravityCompat.START );
                        break;
//                    //Thông Tin
//                    case 5:
//                        if(CheckConnection.haveNetworkConnection ( getApplicationContext () )){
//                            Intent intent = new Intent (MainActivity.this,thongtinactivity.class);
//                            startActivity ( intent );
//                        }else{
//                            CheckConnection.showToast_Short ( getApplicationContext (),"Bạn hãy kiểm tra lại kết nối //" );
//                        }
//                        drawerLayout.closeDrawer ( GravityCompat.START );
//                        break;
                }
            }
        } );
    }

    //get loai san pham
    private void getdulieuloaisanpham() {
        final RequestQueue request = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest json = new JsonArrayRequest(Server.duongdanloaisanpham,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int id = 0;
                String tenloaisanpham = "";
                String hinhanhloaisanpham = "";
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        id = object.getInt("id");
                        tenloaisanpham = object.getString("tenloaisanpham");
                        hinhanhloaisanpham = object.getString("hinhanhloaisanpham");
                        mangLoaisanpham.add(new LoaiSanPham (id,tenloaisanpham,hinhanhloaisanpham));
                        loaiSanPhamAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mangLoaisanpham.add ( new LoaiSanPham ( 4,"Liên Hệ", "https://voip24h.vn/wp-content/uploads/2019/03/phone-png-mb-phone-icon-256.png" ) );
                mangLoaisanpham.add ( new LoaiSanPham ( 5,"Thông Tin", "https://upload.wikimedia.org/wikipedia/commons/0/01/Windows_Terminal_Logo_256x256.png" ) );

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("kiemtra", error.toString());
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.add(json);
    }
    //get du lieu cho san pham moi nhat
    private void getdulieuSpmoinhat() {
        final RequestQueue request = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest json = new JsonArrayRequest(Server.duongdansanphammoinhat,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int id = 0, gia = 0;
                String tensanpham = "";
                String motasanpham = "", hinhanhsanpham = "";
                int idsanpham = 0;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        id = object.getInt("id");
                        tensanpham = object.getString("tensanpham");
                        gia = object.getInt("gia");
                        hinhanhsanpham = object.getString("hinhanhsanpham");
                        motasanpham = object.getString("motasanpham");
                        idsanpham = object.getInt("idsanpham");
                        mangSanpham.add(new SanPham(id, tensanpham, gia, hinhanhsanpham, motasanpham, idsanpham));
                        mangSanpham2.add (new SanPham(id, tensanpham, gia, hinhanhsanpham, motasanpham, idsanpham));
                        sanphamAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("kiemtra", error.toString());
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.add(json);
    }
    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trang Chính");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
    }
//    bắt sự kiện khi click sẽ mở ra thanh menu
    private void actiontoolbar() {
        setSupportActionBar ( toolbar );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer ( GravityCompat.START );
            }
        } );
    }

    private void anhXa() {
        in = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
        out = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
        toolbar = (Toolbar) findViewById(R.id.toolbar_manhinhchinh);
        Logout = findViewById ( R.id.logout );
        viewFlipper = findViewById(R.id.viewflipper);
        recyclerViewManHinhChinh = (RecyclerView) findViewById(R.id.recyclerview);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        lvManHinhChinh = (ListView) findViewById(R.id.lv_manhinhchinh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        //Loai San Pham
        mangLoaisanpham = new ArrayList<LoaiSanPham> ();

        loaiSanPhamAdapter = new LoaiSanPhamAdapter ( mangLoaisanpham, MainActivity.this );
        lvManHinhChinh.setAdapter ( loaiSanPhamAdapter );
        mangLoaisanpham.add (0, new LoaiSanPham (0, "Trang Chính", "https://vietwebgroup.vn/admin/uploads/trang-chu-la-gi-tim-hieu-ve-trang-chu-la-gi.png"));

        //Search view
        searchView = findViewById ( R.id.searchview );

        //San pham
        mangSanpham = new ArrayList<SanPham>();
        mangSanpham2 = new ArrayList<> ();
        sanphamAdapter = new SanPhamAdapter(MainActivity.this, mangSanpham,mangSanpham2);
        recyclerViewManHinhChinh.setHasFixedSize(true);
        recyclerViewManHinhChinh.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        recyclerViewManHinhChinh.setAdapter(sanphamAdapter);
        //gio hang
        if (mangGioHang != null) {

        } else {
            mangGioHang = new ArrayList<>();
        }
    }
//bắt sự kiện viewflipper cho việc chạy quảng cáo
    private void addEEventViewFlipper() {
//        cấp phát vùng bộ nhớ
        ArrayList<String> arrQuangCao = new ArrayList<>();

        arrQuangCao.add("https://tripi.vn/cdn-cgi/image/width=640,height=640/https://www.googleapis.com/download/storage/v1/b/tourcdn/o/photos%2FRQ8EVPHB4F_%2Ftmp%2Fplaytemp571699480815918580%2FmultipartBody1511227626497251199asTemporaryFile?generation=1637913054688299&alt=media");
        arrQuangCao.add("https://tripi.vn/cdn-cgi/image/width=640,height=640/https://www.googleapis.com/download/storage/v1/b/tourcdn/o/photos%2F1QK22DLRG6_%2Ftmp%2Fplaytemp3944802264178618001%2FmultipartBody3841472090960535326asTemporaryFile?generation=1647317265414277&alt=media");
        arrQuangCao.add("https://tripi.vn/cdn-cgi/image/width=640,height=640/https://www.googleapis.com/download/storage/v1/b/tourcdn/o/photos%2FGYSY7SR5X4_%2Ftmp%2Fplaytemp3944802264178618001%2FmultipartBody975618955109982875asTemporaryFile?generation=1647406791282834&alt=media");
        arrQuangCao.add("https://tripi.vn/cdn-cgi/image/width=640,height=640/https://www.googleapis.com/download/storage/v1/b/tourcdn/o/photos%2FD5IM7SW532_%2Ftmp%2Fplaytemp6250983107787354111%2FmultipartBody2667082910754116839asTemporaryFile?generation=1653618620705845&alt=media");
        arrQuangCao.add("https://tripi.vn/cdn-cgi/image/width=640,height=640/https://www.googleapis.com/download/storage/v1/b/tourcdn/o/photos%2F2H1BQUVN2I_%2Ftmp%2Fplaytemp4332522927599923195%2FmultipartBody783253346416295469asTemporaryFile?generation=1646617475691144&alt=media");
        arrQuangCao.add("https://tripi.vn/cdn-cgi/image/width=640,height=640/https://www.googleapis.com/download/storage/v1/b/tourcdn/o/photos%2FWYT37K7XX6_%2Ftmp%2Fplaytemp4246306638376525934%2FmultipartBody2240734691898850014asTemporaryFile?generation=1651820514495833&alt=media");
        arrQuangCao.add("https://tripi.vn/cdn-cgi/image/width=640,height=640/https://www.googleapis.com/download/storage/v1/b/tourcdn/o/photos%2FYBULVFQ1D2_%2Ftmp%2Fplaytemp7564108576467774580%2FmultipartBody6764201242781047275asTemporaryFile?generation=1652437451849945&alt=media");
//       viewflipper chỉ nhận imageview -> gán đường dẫn vào imageview -> viewfliper
        for (int i = 0; i < arrQuangCao.size(); i++) {
            ImageView imageView = new ImageView(MainActivity.this);
            Picasso.get().load(arrQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);
        Animation animation_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_quangcao);
        Animation animation_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_outquangcao);
        viewFlipper.setInAnimation(animation_in);
        viewFlipper.setOutAnimation(animation_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_giohang) {
            Intent intent = new Intent(MainActivity.this, GioHangActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    public class readJson extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder=new StringBuilder();
            try {
                URL url=new URL(strings[0]);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream=httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                String line="";
                while ((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            try {
                JSONArray jsonArray=new JSONArray(s);
                int id = 0, gia = 0;
                String tensanham = "";
                String mota = "", hinhanh = "";
                int idsp = 0;
                Log.d("kiemtra",jsonArray.toString()+"chn doi");
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject object = jsonArray.getJSONObject(i);
                        id = object.getInt("id");
                        tensanham = object.getString("tensp");
                        gia = object.getInt("giasp");
                        hinhanh = object.getString("hinhanhsp");
                        mota = object.getString("motasp");
                        idsp = object.getInt("idsp");
                        mangSanpham.add(new SanPham(id, tensanham, gia, hinhanh, mota, idsp));
                        sanphamAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
