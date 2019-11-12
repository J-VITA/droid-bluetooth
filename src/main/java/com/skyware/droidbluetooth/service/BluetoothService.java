package com.skyware.droidbluetooth.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import com.skyware.droidbluetooth.utils.Logging;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothService {

    private static final String TAG = BluetoothService.class.getSimpleName();

    private BluetoothAdapter btAdapter;
    BluetoothDevice mRemoteDevie;

    private Activity mActivity;
    private Handler mHamdler;
    int mPariedDeviceCount = 0;
    Set<BluetoothDevice> mDevices;

    BluetoothSocket mSocket = null;
    OutputStream mOutputStream = null;
    InputStream mInputStream = null;
    String mStrDelimiter = "\n";
    char mCharDelimiter =  '\n';

    byte[] readBuffer;
    int readBufferPosition;
    Thread mWorkerThread = null;

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;


    public BluetoothService(Activity act, Handler handler){
        mActivity = act;
        mHamdler = handler;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            btAdapter = BluetoothAdapter.getDefaultAdapter();
        }
    }

    BluetoothDevice getDeviceFromBondedList(String name) {
        BluetoothDevice selectedDevice = null;

        for(BluetoothDevice deivce : mDevices) {
            // Device Bluetooth Adapter Name return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                if(name.equals(deivce.getName())) {
                    selectedDevice = deivce;
                    break;
                }
            }
        }
        return selectedDevice;
    }

    public boolean getDeviceState(){
        Logging.d(TAG, "check the bluetooth support");
        if (btAdapter == null){
            Logging.d(TAG, "Bluetooth is not available");
            return  false;
        }else{
            Logging.d(TAG, "Bluetooth is available");
            return  true;
        }
    }

    public void enableBluetooth(){
        Logging.d(TAG, "check the enable bluetooth");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            if(btAdapter.isEnabled()) {
                //
                Logging.d(TAG, "블루투스가 활성화 되어 있음.");
            } else {
                //
                Logging.d(TAG, "블루투스가 비활성화 되어 있음.");
                Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); mActivity.startActivityForResult(i, REQUEST_ENABLE_BT);
            }
        }
    }

    public void selectDevice(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            mDevices = btAdapter.getBondedDevices();
        }
        mPariedDeviceCount = mDevices.size();

        if(mPariedDeviceCount == 0 ) { // 페어링된 장치가 없는 경우.
            Toast.makeText(context.getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            //finish(); // App 종료.
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("블루투스 장치 선택");

        // 각 디바이스는 이름과(서로 다른) 주소를 가진다. 페어링 된 디바이스들을 표시한다.
        List<String> listItems = new ArrayList<String>();
        for(BluetoothDevice device : mDevices) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                listItems.add(device.getName());
            }
        }
        listItems.add("취소");  // 취소 항목 추가.

        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
        // toArray 함수를 이용해서 size만큼 배열이 생성 되었다.
        listItems.toArray(new CharSequence[listItems.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                // TODO Auto-generated method stub
                if(item == mPariedDeviceCount) { // 연결할 장치를 선택하지 않고 '취소' 를 누른 경우.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        Toast.makeText(builder.getContext().getApplicationContext(), "연결할 장치를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
                    }
                    //finish();
                }
                else { // 연결할 장치를 선택한 경우, 선택한 장치와 연결을 시도함.
                    connectToSelectedDevice(items[item].toString(), context);
                }
            }

        });

        builder.setCancelable(false);  // 뒤로 가기 버튼 사용 금지.
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void connectToSelectedDevice(String selectedDeviceName, Context context) {
        mRemoteDevie = getDeviceFromBondedList(selectedDeviceName);
        // 자바에서 중복되지 않는 Unique 키 생성.
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                mSocket = mRemoteDevie.createRfcommSocketToServiceRecord(uuid);
                mSocket.connect();

                mOutputStream = mSocket.getOutputStream();
                mInputStream = mSocket.getInputStream();
            }
            // 데이터 수신 준비.
            beginListenForData(context);

        }catch(Exception e) { // 블루투스 연결 중 오류 발생
            Toast.makeText(context.getApplicationContext(),
                    "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            //finish();  // App 종료
            finishListenForData();
        }
    }

    // 데이터 수신(쓰레드 사용 수신된 메시지를 계속 검사함)
    public void beginListenForData(final Context context) {
        final Handler handler = mHamdler;//new Handler();

        readBufferPosition = 0;                 // 버퍼 내 수신 문자 저장 위치.
        readBuffer = new byte[1024];            // 수신 버퍼.

        // 문자열 수신 쓰레드.
        mWorkerThread = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()) {
                    try {
                        // InputStream.available() : 다른 스레드에서 blocking 하기 전까지 읽은 수 있는 문자열 개수를 반환함.
                        int byteAvailable = mInputStream.available();   // 수신 데이터 확인
                        if(byteAvailable > 0) {                        // 데이터가 수신된 경우.
                            byte[] packetBytes = new byte[byteAvailable];
                            // read(buf[]) : 입력스트림에서 buf[] 크기만큼 읽어서 저장 없을 경우에 -1 리턴.
                            mInputStream.read(packetBytes);
                            for(int i=0; i<byteAvailable; i++) {
                                byte b = packetBytes[i];
                                if(b == mCharDelimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];

                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);

                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable(){
                                        // 수신된 문자열 데이터에 대한 처리.
                                        @Override
                                        public void run() {
                                            // mStrDelimiter = '\n';
                                            Logging.v(data+ mStrDelimiter);
                                        }

                                    });
                                }
                                else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }

                    } catch (Exception e) {    // 데이터 수신 중 오류 발생.
                        Toast.makeText(context.getApplicationContext(), "데이터 수신 중 오류가 발생 했습니다.", Toast.LENGTH_LONG).show();
                        finishListenForData();
                        //finish();            // App 종료.
                    }
                }
            }

        });
    }

    void finishListenForData(){
        try{
            if (mWorkerThread != null) mWorkerThread.interrupt();
            if (mInputStream != null) mInputStream.close();
            if (mSocket != null) mSocket.close();
        }catch (Exception e){

        }
    }

}
