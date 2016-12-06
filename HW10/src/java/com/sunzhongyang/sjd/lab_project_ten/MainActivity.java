package com.sunzhongyang.sjd.lab_project_ten;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

public class MainActivity extends AppCompatActivity
{
    //全局地图View
    MapView mMapView = null;

    //全局传感器和传感器管理器
    SensorManager mSensorManager;
    Sensor mMagneticSensor;
    Sensor mAccelerometerSensor;

    //位置管理器
    LocationManager mLocationManager;

    //位置数据来源
    String provider;

    //箭头指向
    float newRotationDegree = 0;

    //加速度数据和磁力传感数据
    float[] accValues = new float[3];
    float[] magValues = new float[3];

    ToggleButton mToggleButton;

    //用户所在地图位置有关信息
    MyLocationData.Builder data;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);

        //获取传感器管理器和传感器
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mToggleButton = (ToggleButton) findViewById(R.id.tb_center);

        //获得位置管理器
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //设置定位传感器来源为网络
        provider = mLocationManager.NETWORK_PROVIDER;

        //初始化用户方向箭头
        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pointer), 100, 100, true);
        BitmapDescriptor bitmapD = BitmapDescriptorFactory.fromBitmap(bitmap);

        //根据定位传感器获得当前位置
        Location loc = mLocationManager.getLastKnownLocation(provider);

        //将位置坐标转换为百度位置坐标
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(new LatLng(loc.getLatitude(), loc.getLongitude()));
        LatLng desLatLng = converter.convert();

        mMapView.getMap().setMyLocationEnabled(true);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, bitmapD);
        mMapView.getMap().setMyLocationConfigeration(config);

        //设置百度地图位置
        data = new MyLocationData.Builder();
        data.latitude(desLatLng.latitude);
        data.longitude(desLatLng.longitude);
        data.direction(0);

        mMapView.getMap().setMyLocationData(data.build());

        //将用户位置设置为窗口中心
        MapStatus mMapstatus = new MapStatus.Builder().target(desLatLng).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapstatus);
        mMapView.getMap().setMapStatus(mMapStatusUpdate);

        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Location loc = mLocationManager.getLastKnownLocation(provider);

                //将位置坐标转换为百度位置坐标
                CoordinateConverter converter  = new CoordinateConverter();
                converter.from(CoordinateConverter.CoordType.GPS);
                converter.coord(new LatLng(loc.getLatitude(), loc.getLongitude()));
                LatLng desLatLng = converter.convert();

                data = new MyLocationData.Builder();
                data.latitude(desLatLng.latitude);
                data.longitude(desLatLng.longitude);
                data.direction(newRotationDegree);

                mMapView.getMap().setMyLocationData(data.build());

                //将用户位置设置为窗口中心
                MapStatus mMapstatus = new MapStatus.Builder().target(desLatLng).build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapstatus);
                mMapView.getMap().setMapStatus(mMapStatusUpdate);
            }

        });

        mMapView.getMap().setOnMapTouchListener(new BaiduMap.OnMapTouchListener()
        {
            @Override
            public void onTouch(MotionEvent motionEvent)
            {
                switch(motionEvent.getAction())
                {
                    case MotionEvent.ACTION_MOVE:
                        mToggleButton.setChecked(false);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();

        //注册磁力变化和加速度变化到监听事件
        mSensorManager.registerListener(mSensorEventListener, mMagneticSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mSensorEventListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);

        //注册位置变化到监听事件
        mLocationManager.requestLocationUpdates(provider, 0, 0, mLocationListener);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();

        //取消对磁力变化和加速度变化的监听
        mSensorManager.unregisterListener(mSensorEventListener);

        //取消对位置变化的监听
        mLocationManager.removeUpdates(mLocationListener);
    }

    private SensorEventListener mSensorEventListener = new SensorEventListener()
    {
        long lastShakeTime = 0;

        @Override
        public void onSensorChanged(SensorEvent event)
        {
            //根据变化的传感器类型更新不同的数据
            switch(event.sensor.getType())
            {
                case Sensor.TYPE_ACCELEROMETER:
                    accValues = event.values;
                    if((event.values[0] + event.values[1] + event.values[2]) > 30)
                    {
                        Log.i("acceler", String.valueOf(event.values[0]) + " " + String.valueOf(event.values[1]) + " " + String.valueOf(event.values[2]));
                        Location loc = mLocationManager.getLastKnownLocation(provider);
                        mToggleButton.setChecked(true);

                        //将位置坐标转换为百度位置坐标
                        CoordinateConverter converter  = new CoordinateConverter();
                        converter.from(CoordinateConverter.CoordType.GPS);
                        converter.coord(new LatLng(loc.getLatitude(), loc.getLongitude()));
                        LatLng desLatLng = converter.convert();

                        //将用户位置设置为窗口中心
                        MapStatus mMapstatus = new MapStatus.Builder().target(desLatLng).build();
                        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapstatus);
                        mMapView.getMap().setMapStatus(mMapStatusUpdate);
                    }
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    magValues = event.values;
                    break;
                default:
                    break;
            }

            float[] R = new float[9];
            float[] values = new float[3];

            //根据磁力变化和加速度变化计算新的朝向
            SensorManager.getRotationMatrix(R, null, accValues, magValues);
            SensorManager.getOrientation(R, values);
            newRotationDegree = (float) Math.toDegrees(values[0]);

            data.direction(newRotationDegree);

            //更新朝向
            mMapView.getMap().setMyLocationData(data.build());
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {

        }
    };

    private LocationListener mLocationListener = new LocationListener()
    {
        public void onLocationChanged(Location location)
        {
            //初始化用户方向箭头
            Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pointer), 100, 100, true);
            BitmapDescriptor bitmapD = BitmapDescriptorFactory.fromBitmap(bitmap);

            //将位置坐标转换为百度位置坐标
            CoordinateConverter converter  = new CoordinateConverter();
            converter.from(CoordinateConverter.CoordType.GPS);
            converter.coord(new LatLng(location.getLatitude(), location.getLongitude()));
            LatLng desLatLng = converter.convert();

            mMapView.getMap().setMyLocationEnabled(true);
            MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, bitmapD);
            mMapView.getMap().setMyLocationConfigeration(config);

            //设置百度地图位置
            data = new MyLocationData.Builder();
            data.latitude(desLatLng.latitude);
            data.longitude(desLatLng.longitude);
            data.direction(newRotationDegree);

            mMapView.getMap().setMyLocationData(data.build());

            if(mToggleButton.isChecked())
            {
                //将用户位置设置为窗口中心
                MapStatus mMapstatus = new MapStatus.Builder().target(desLatLng).build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapstatus);
                mMapView.getMap().setMapStatus(mMapStatusUpdate);
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }
        public void onProviderEnabled(String provider)
        {

        }

        public void onProviderDisabled(String provider)
        {

        }
    };
}
