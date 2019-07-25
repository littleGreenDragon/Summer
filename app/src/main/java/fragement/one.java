package fragement;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;

import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.example.summer.BaseFragment;
import com.example.summer.MylistView;
import com.example.summer.R;

import java.lang.annotation.Target;
import java.util.List;
import info.hoang8f.android.segmented.SegmentedGroup;
import model.SearchResultAdapter;
import android.graphics.Point;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.RegeocodeQuery;
import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

public class one extends BaseFragment implements LocationSource,
        AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener, PoiSearch.OnPoiSearchListener{
    private AMap aMap;
    private MylistView listView;
    private SegmentedGroup mSegmentedGroup;
    private AutoCompleteTextView searchText;
    private MapView mapView;
    private LocationSource.OnLocationChangedListener mListener;
    //声明AMapLocationClient类对象
    private AMapLocationClient mlocationClient;
//    AMapLocationClientOption对象用来设置发起定位的模式和相关参数。
    private AMapLocationClientOption mLocationOption;
    private String[] items = {"住宅区", "学校", "楼宇", "商场" };
    private Marker locationMarker;
    private ProgressDialog progDialog = null;
    private GeocodeSearch geocoderSearch;
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;
    private List<PoiItem> poiItems;// poi数据
    private String searchType = items[0];
    private String searchKey = "";
    private LatLonPoint searchLatlonPoint;
    private List<PoiItem> resultData;
    private SearchResultAdapter searchResultAdapter;
    private boolean isItemClickAction;
    private List<Tip> autoTips;
    private boolean isfirstinput = true;
    private PoiItem firstItem;
    private FloatingActionButton floatingActionButton;
    private static LatLng latLngone,latLngtwo;
    private DistanceSearch distanceSearch=new DistanceSearch(getContext());
    private BottomSheetBehavior behavior;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapView = (MapView)getView().findViewById(R.id.map);
        floatingActionButton=getView().findViewById(R.id.sunlignt);
        mapView.onCreate(savedInstanceState);
        init();
        initView();
        View nest=getView().findViewById(R.id.nest);
        behavior=BottomSheetBehavior.from(nest);
        behavior.setPeekHeight(450);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float v) {
//                behavior.setState(BottomSheetBehavior.STATE_DRAGGING);
            }
        });

        resultData = new ArrayList<>();
      distanceSearch.setDistanceSearchListener(new DistanceSearch.OnDistanceSearchListener() {
          @Override
          public void onDistanceSearched(DistanceResult distanceResult, int i) {

          }
      });

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        return view;
    }

    private void initView() {

        listView = (MylistView)getView().findViewById(R.id.listview);
        searchResultAdapter = new SearchResultAdapter(getContext());
        listView.setAdapter(searchResultAdapter);

        listView.setOnItemClickListener(onItemClickListener);

        mSegmentedGroup = (SegmentedGroup)getView().findViewById(R.id.segmented_group);
        mSegmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                searchType = items[0];
                switch (checkedId) {
                    case R.id.radio0 :
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        searchType = items[0];
                        break;
                    case R.id.radio1 :
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        searchType = items[1];
                        break;
                    case R.id.radio2 :
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        searchType = items[2];
                        break;
                    case R.id.radio3 :
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        searchType = items[3];
                        break;
                }
                geoAddress();
            }
        });
        //查找地点，keyword是那个查找框
        searchText = (AutoCompleteTextView)getView().findViewById(R.id.keyWord);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();

                if (newText.length() > 0) {
                    InputtipsQuery inputquery = new InputtipsQuery(newText, "哈尔滨");
                    Inputtips inputTips = new Inputtips(getContext(), inputquery);
                    inputquery.setCityLimit(true);
                    inputTips.setInputtipsListener(inputtipsListener);
                    inputTips.requestInputtipsAsyn();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MY", "setOnItemClickListener");
                if (autoTips != null && autoTips.size() > position) {
                    Tip tip = autoTips.get(position);
                    searchPoi(tip);

                }
            }
        });

        geocoderSearch = new GeocodeSearch(getContext());
        geocoderSearch.setOnGeocodeSearchListener(this);
        progDialog = new ProgressDialog(getContext());

        hideSoftKey(searchText);
    }

    /**
     * 初始化地图
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (!isItemClickAction && !isInputKeySearch) {
                    geoAddress();
                    startJumpAnimation();
                }
                searchLatlonPoint = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
                isInputKeySearch = false;
                isItemClickAction = false;
            }
        });

        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                Log.d("qaz","Load");
                addMarkerInScreenCenter(null);
            }
        });
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        //无加减号
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        MyLocationStyle myLocationStyle= new MyLocationStyle();
        myLocationStyle.strokeColor(R.color.white);//设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.radiusFillColor(R.color.tinypink);//设置定位蓝点精度圆圈的填充颜色的方法。
        //不展示我的位置
        myLocationStyle.showMyLocation(false);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示

    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
       mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    public void updateUI() {
        super.updateUI();
       Log.d("qaz", "updateUI: ");
        init();
        initView();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);

    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);
                LatLng curLatlng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                latLngone=curLatlng;
                searchLatlonPoint = new LatLonPoint(curLatlng.latitude, curLatlng.longitude);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));
                isInputKeySearch = false;
                searchText.setText("");

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    /**
     * 激活定位
     * 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
     * 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
     * 在定位结束后，在合适的生命周期调用onDestroy()方法
     * 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getContext());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听，给定位客户端对象设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.setLocationListener(this);
//            //设置为单次定位
//            mLocationOption.setOnceLocation(true);
//            设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
            mLocationOption.setInterval(1000);
            //设置定位模式为AMapLocationMode.Hight_Accuracy
            // 高精度模式。会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);

            //启动定位
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    /**
     * 响应逆地理编码
     */
    public void geoAddress() {
        showDialog();
        searchText.setText("");
        if (searchLatlonPoint != null){
            // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            RegeocodeQuery query = new RegeocodeQuery(searchLatlonPoint, 1000, GeocodeSearch.AMAP);
            geocoderSearch.getFromLocationAsyn(query);
        }
    }

    /**
     * 开始进行poi搜索
     */
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        currentPage = 0;
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(searchKey, searchType, "");
        query.setCityLimit(true);
        query.setPageSize(40);
        query.setPageNum(currentPage);

        if (searchLatlonPoint != null) {
            poiSearch = new PoiSearch(getContext(), query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(searchLatlonPoint, 1000, true));//
            poiSearch.searchPOIAsyn();
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                String address = result.getRegeocodeAddress().getProvince() + result.getRegeocodeAddress().getCity() + result.getRegeocodeAddress().getDistrict() + result.getRegeocodeAddress().getTownship();
                firstItem = new PoiItem("regeo", searchLatlonPoint, address, address);
                doSearchQuery();
            }
        } else {
//            Toast.makeText(MainActivity.this, "error code is " + rCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * POI搜索结果回调
     * 用户搜索了之后，对搜索的结果进行处理
     * @param poiResult 搜索结果
     * @param resultCode 错误码
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {
        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {
                if (poiResult.getQuery().equals(query)) {
                    poiItems = poiResult.getPois();
                    if (poiItems != null && poiItems.size() > 0) {
                        updateListview(poiItems);
                    } else {
                        Toast.makeText(getActivity(), "无搜索结果", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getActivity(), "无搜索结果", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 更新列表中的item
     * @param poiItems
     */
    private void updateListview(List<PoiItem> poiItems) {
        resultData.clear();
        searchResultAdapter.setSelectedPosition(0);
        resultData.add(firstItem);
        resultData.addAll(poiItems);
        searchResultAdapter.setData(resultData);
        searchResultAdapter.notifyDataSetChanged();
    }


    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position != searchResultAdapter.getSelectedPosition()) {
                PoiItem poiItem = (PoiItem) searchResultAdapter.getItem(position);
                LatLng curLatlng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
                addMaker(curLatlng);
                latLngtwo=curLatlng;
               float distance = AMapUtils.calculateLineDistance(latLngone,latLngtwo);
                //展示一下距离
//                Toast.makeText(getActivity(), distance+"", Toast.LENGTH_LONG).show();
                View root = getLayoutInflater().inflate(R.layout.root, null);
                final PopupWindow popupWindow=new PopupWindow(root,700,500);
                    Button button = root.findViewById(R.id.igetit);
                    if(!popupWindow.isShowing()) {
                        popupWindow.setFocusable(true);
                        popupWindow.setOutsideTouchable(false);
                        popupWindow.setFocusable(false);
                        TextView textView = root.findViewById(R.id.distan);
                        textView.setText("距离现在位置为"+distance+"千米");
                        popupWindow.setFocusable(true);
                        popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER_HORIZONTAL, 0, 0);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow.dismiss();
                            }
                        });

                    }

                isItemClickAction = true;
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));
                searchResultAdapter.setSelectedPosition(position);
                searchResultAdapter.notifyDataSetChanged();
            }
        }
    };

    public void showDialog() {
        floatingActionButton.show();
        ObjectAnimator alphaAni = ObjectAnimator.ofFloat(floatingActionButton, "alpha", 1, 0, 1);
        //旋转动画
        ObjectAnimator rotationAni = ObjectAnimator.ofFloat(floatingActionButton, "rotation", 0, 360, 0,360);
        AnimatorSet set = new AnimatorSet();
        //在进行平移之后，旋转和透明度变化同时进行
        set.play(rotationAni).with(alphaAni);
        set.setDuration(5*1000);
        set.start();
    }

    public void dismissDialog() {
        floatingActionButton.hide();
    }

    private void addMaker(LatLng latLng)
    {
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        locationMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.1f,0.1f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.settingtwo)));
        //设置Marker在屏幕上,不跟随地图移动
        locationMarker.setPositionByPixels(screenPosition.x,screenPosition.y);
        locationMarker.setZIndex(1);
        Animation markerAnimation = new ScaleAnimation(0, 1, 0, 1); //初始化生长效果动画
        markerAnimation.setDuration(1000);  //设置动画时间 单位毫秒
        locationMarker.setAnimation(markerAnimation);
        locationMarker.startAnimation();
    }
    private void addMarkerInScreenCenter(LatLng locationLatLng) {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        locationMarker = aMap.addMarker(new MarkerOptions()
               .anchor(0.1f,0.1f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.setting)));
        //设置Marker在屏幕上,不跟随地图移动
        locationMarker.setPositionByPixels(screenPosition.x,screenPosition.y);
        locationMarker.setZIndex(1);
        Animation markerAnimation = new ScaleAnimation(0, 1, 0, 1); //初始化生长效果动画
        markerAnimation.setDuration(1000);  //设置动画时间 单位毫秒
        locationMarker.setAnimation(markerAnimation);
        locationMarker.startAnimation();
    }

    /**
     * 屏幕中心marker 跳动
     */
    public void startJumpAnimation() {

        if (locationMarker != null ) {
            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = locationMarker.getPosition();
            Point point =  aMap.getProjection().toScreenLocation(latLng);
            point.y -= dip2px(getContext(),125);
            LatLng target = aMap.getProjection()
                    .fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if(input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f)*(1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(600);
            //设置动画
            locationMarker.setAnimation(animation);
            //开始动画
            locationMarker.startAnimation();
        } else {
            Log.e("ama","screenMarker is null");
        }
    }

    //dip和px转换
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    Inputtips.InputtipsListener inputtipsListener = new Inputtips.InputtipsListener() {
        @Override
        public void onGetInputtips(List<Tip> list, int rCode) {
            if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
                autoTips = list;
                List<String> listString = new ArrayList<String>();
                for (int i = 0; i < list.size(); i++) {
                    listString.add(list.get(i).getName());
                }
                ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                        getContext(),
                        R.layout.route_inputs, listString);
                searchText.setAdapter(aAdapter);
                aAdapter.notifyDataSetChanged();
                if (isfirstinput) {
                    isfirstinput = false;
                    searchText.showDropDown();
                }
            } else {
               Toast.makeText(getActivity(), "erroCode " + rCode , Toast.LENGTH_SHORT).show();
            }
        }
    };

    private boolean isInputKeySearch;
    private String inputSearchKey;
    private void searchPoi(Tip result) {
        isInputKeySearch = true;
        //getAddress(); // + result.getRegeocodeAddress().getCity() + result.getRegeocodeAddress().getDistrict() + result.getRegeocodeAddress().getTownship();
        inputSearchKey = result.getName();
        searchLatlonPoint = result.getPoint();
        firstItem = new PoiItem("tip", searchLatlonPoint, inputSearchKey, result.getAddress());
        firstItem.setCityName(result.getDistrict());
        firstItem.setAdName("");
        resultData.clear();
        //搜索得到的地方，设置maker
        LatLng latLng=new LatLng(searchLatlonPoint.getLatitude(),searchLatlonPoint.getLongitude());
        addMaker(latLng);
        searchResultAdapter.setSelectedPosition(0);

        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(searchLatlonPoint.getLatitude(), searchLatlonPoint.getLongitude()), 16f));

        hideSoftKey(searchText);
        doSearchQuery();
    }

    private void hideSoftKey(View view) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
