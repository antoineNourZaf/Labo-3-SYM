package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        Button mClickButton1 = findViewById(R.id.NFC);
        Button mClickButton2 = findViewById(R.id.QRCode);
        Button mClickButton3 = findViewById(R.id.iBeacon);
        Button mClickButton4 = findViewById(R.id.capteurs);


        mClickButton1.setOnClickListener(onClickListener);
        mClickButton2.setOnClickListener(onClickListener);
        mClickButton3.setOnClickListener(onClickListener);
        mClickButton4.setOnClickListener(onClickListener);
    }

    // somewhere else in your code
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.NFC: {
                    Intent intent = new Intent(MainActivity.this, activity.NFC.class);
                    startActivity(intent);
                    break;
                }

                case R.id.QRCode: {
                    // do something for button 2 click
                    Intent intent = new Intent(MainActivity.this, activity.QRcode.class);
                    startActivity(intent);
                    break;
                }

                case R.id.iBeacon: {
                    // do something for button 1 click
                    break;
                }

                case R.id.capteurs: {
                    Intent intent = new Intent(MainActivity.this, ch.heigvd.iict.sym.a3dcompassapp.CompassActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    };
}
