package activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;


public class TimeToLive extends AppCompatActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    private Long time;
    private NfcAdapter mNfcAdapter;
    private Intent intent;
    private String idNFC;
    private String authorizationAccess;
    private String authorizationAccessOK = getResources().getString(R.string.authorize);
    private String authorizationAccessKO = getResources().getString(R.string.lost);
    private String authorizationLevel;
    private Context content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_to_live);

        content = this;
        intent = getIntent();
        idNFC = intent.getStringExtra("idNFC");

        time = System.currentTimeMillis();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Button authorizationMax = findViewById(R.id.authorizationMax);
        Button authorizationMedium = findViewById(R.id.authorizationMedium);
        Button authorizationMin = findViewById(R.id.authorizationMin);

        authorizationMax.setOnClickListener(onClickListener);
        authorizationMedium.setOnClickListener(onClickListener);
        authorizationMin.setOnClickListener(onClickListener);

        handleIntent(intent);
    }

    // somewhere else in your code
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.authorizationMax: {
                    if ( System.currentTimeMillis() <= time + 10000){
                        authorizationAccess = authorizationAccessOK;
                    }
                    else {
                        authorizationAccess = authorizationAccessKO;
                    }
                    authorizationLevel = getResources().getString(R.string.authorizationMax);
                    break;
                }

                case R.id.authorizationMedium: {
                    if ( System.currentTimeMillis() <= time + 20000){
                        authorizationAccess = authorizationAccessOK;
                    }
                    else {
                        authorizationAccess = authorizationAccessKO;
                    }
                    authorizationLevel = getResources().getString(R.string.authorizationMedium);
                    break;
                }

                case R.id.authorizationMin: {
                    if ( System.currentTimeMillis() <= time + 30000){
                        authorizationAccess = authorizationAccessOK;
                    }
                    else {
                        authorizationAccess = authorizationAccessKO;
                    }
                    authorizationLevel = getResources().getString(R.string.authorizationMin);
                    break;
                }
            }
            Toast.makeText( content, authorizationAccess + authorizationLevel, Toast.LENGTH_LONG).show();
        }
    };
    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mNfcAdapter);
        Intent intent = new Intent(activity.TimeToLive.this, NFC.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
        Intent intent = new Intent(activity.TimeToLive.this, NFC.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }
    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new TimeToLive.NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new TimeToLive.NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * @param activity The corresponding {@link } requesting to stop the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch( final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }


    // private class manage the read of the NFC
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
            /*
             * See NFC forum specification for "Text Record Type Definition" at 3.2.1
             *
             * http://www.nfc-forum.org/specs/
             *
             * bit_7 defines encoding
             * bit_6 reserved for future use, must be 0
             * bit_5..0 length of IANA language code
             */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.equals(idNFC)) {
                    time = System.currentTimeMillis();
                }
            }
        }

    }

}