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
import java.util.logging.Level;
import java.util.logging.Logger;

public class BeaconActivity extends AppCompatActivity implements BeaconConsumer {

    private Switch switcher;
    private ListView listView;
    private ProgressBar bar;
    private BeaconParser beaconParser;
    private BeaconManager beaconManager;
    private Logger logger;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.beacon_activity);

        logger = Logger.getLogger(BeaconActivity.class.getName());

        // Set buttons
        switcher = findViewById(R.id.switcher);
        listView = findViewById(R.id.listView);
    }

    @Override
    public void onResume() {

        super.onResume();

        // Set parsers for Beacon
        beaconManager = BeaconManager.getInstanceForApplication(BeaconActivity.this);
        beaconParser = new BeaconParser();
        beaconParser.setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
        beaconManager.getBeaconParsers().add(beaconParser);
        beaconManager.setRegionStatePersistenceEnabled(false);

        // Enable check for Beacon
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    beaconManager.bind(BeaconActivity.this);
                    onBeaconServiceConnect();

                } else {
                    bar = findViewById(R.id.progressBar);
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
            logger.log(Level.WARNING, "Error while connecting to beacons");
        }
    }

}
