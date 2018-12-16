package activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

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
    private TextView textView;
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
        textView = findViewById(R.id.textView);
        bar = findViewById(R.id.progressBar);
    }

    @Override
    public void onResume() {

        super.onResume();

        // Set parsers for Beacon
        bar.setVisibility(View.GONE);
        beaconManager = BeaconManager.getInstanceForApplication(BeaconActivity.this);
        beaconParser = new BeaconParser();
        beaconParser.setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
        beaconManager.getBeaconParsers().add(beaconParser);
        beaconManager.setRegionStatePersistenceEnabled(false);

        // Enable check for Beacon
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Show that activity looking for beacons
                    bar.setVisibility(View.VISIBLE);
                    beaconManager.bind(BeaconActivity.this);
                    onBeaconServiceConnect();

                } else {
                    bar.setVisibility(View.GONE);
                    beaconManager.unbind(BeaconActivity.this);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        beaconManager.unbind(BeaconActivity.this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                // if there are beacons, we read their informations
                if (beacons.size() > 0) {
                    textView.setText("Force du signal :" + beacons.iterator().next().getRssi()
                    + "numero majeur : " + beacons.iterator().next().getId2()
                    + "numero mineur : " + beacons.iterator().next().getId3());

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
