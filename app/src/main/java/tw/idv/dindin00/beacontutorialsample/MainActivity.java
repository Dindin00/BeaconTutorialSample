package tw.idv.dindin00.beacontutorialsample;

import android.content.DialogInterface;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.recognition.packets.EstimoteLocation;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    //宣告Beacon管理器變數
    BeaconManager beaconManager;
    //宣告Beacon地區變數
    BeaconRegion myBeaconRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //創造新的Beacon管理器
        beaconManager = new BeaconManager(MainActivity.this);

        //創造新的Beacon地區
        //Here input your ID處輸入識別地區的ID(自行決定中英皆可)
        //Here input your UUID處輸入UUID(請以小寫且字串型態輸入)
        //Here input your Major處輸入Major碼(以int型態輸入)
        //Here input your Minor處輸入Minor碼(以int型態輸入)
        myBeaconRegion = new BeaconRegion("Here input your ID", UUID.fromString("Here input your UUID"), "Here input your Major", "Here input your Minor");

        //開始跟Beacon進行連結並設定要掃描的Beacon地區
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                //此處設定要掃描的Beacon訊號
                beaconManager.startMonitoring(myBeaconRegion);
            }
        });

        //設定掃描到Beacon後的監聽器
        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion beaconRegion, List<Beacon> beacons) {
                //此處用以設定進入Beacon偵測範圍後的動作
                //將每個掃描到的Beacon用來檢查是否符合監聽的Beacon，是則創造提示訊息並顯示
                //Here input your UUID處輸入UUID(請以小寫且字串型態輸入)
                //Here input your Major處輸入Major碼(以int型態輸入)
                //Here input your Minor處輸入Minor碼(以int型態輸入)
                for(Beacon beacon : beacons){
                    if (beacon.getProximityUUID().toString().equals("Here input your UUID") && beacon.getMajor() == "Here input your Major" && beacon.getMinor() == "Here input your Minor") {
                        new AlertDialog.Builder(MainActivity.this)
                                //設定標題
                                .setTitle("你靠近了Beacon！")
                                //設定內容
                                //beaconRegion是Beacon存放詳細資料的物件
                                .setMessage("Beacon UUID：" + beacon.getProximityUUID()
                                        + "\nBeacon Major：" + beacon.getMajor()
                                        + "\nBeacon Minor：" + beacon.getMinor())
                                //設定正向按鈕
                                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                    }
                }
            }

            @Override
            public void onExitedRegion(BeaconRegion beaconRegion) {
                //此處用以設定離開Beacon偵測範圍後的動作
                //符合監聽的Beacon即創造提示訊息並顯示
                //Here input your UUID處輸入UUID(請以小寫且字串型態輸入)
                //Here input your Major處輸入Major碼(以int型態輸入)
                //Here input your Minor處輸入Minor碼(以int型態輸入)
                if (beaconRegion.getProximityUUID().toString().equals("Here input your UUID") && beaconRegion.getMajor() == "Here input your Major" && beaconRegion.getMinor() == "Here input your Minor") {
                    new AlertDialog.Builder(MainActivity.this)
                            //設定標題
                            .setTitle("你遠離了Beacon！")
                            //設定內容
                            //beaconRegion是Beacon存放詳細資料的物件
                            .setMessage("Beacon UUID：" + beaconRegion.getProximityUUID()
                                    + "\nBeacon Major：" + beaconRegion.getMajor()
                                    + "\nBeacon Minor：" + beaconRegion.getMinor())
                            //設定正向按鈕
                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //動態要求並打開定位及藍芽權限
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //中斷Beacon掃描
        //Here input your ID處輸入識別地區的ID(前方建立的BeaconRegion的ID)
        beaconManager.stopMonitoring("Beacon Tag Id");
        //中斷Beacon連結
        beaconManager.disconnect();
    }
}
