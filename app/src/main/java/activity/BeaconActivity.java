package activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class BeaconActivity extends AppCompatActivity implements BeaconConsumer {

    private Switch switcher;
    private ListView listView;
    private ProgressBar bar;
    private BeaconParser beaconParser;
    private BeaconManager beaconManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.beacon_activity);

        // Set buttons
        switcher = findViewById(R.id.switcher);
        listView = findViewById(R.id.listView);
        bar = findViewById(R.id.progressBar);

        // Set parsers for Beacon
        beaconParser = new BeaconParser();
        beaconParser.setBeaconLayout("m-2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");

        // Enable check for Beacon
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bar.setIndeterminate(true);
                    beaconManager = BeaconManager.getInstanceForApplication(BeaconActivity.this);
                    beaconManager.getBeaconParsers().add(beaconParser);
                    onBeaconServiceConnect();

                } else {
                    bar.setIndeterminate(false);
                }
            }
        });

    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    System.out.println("The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
