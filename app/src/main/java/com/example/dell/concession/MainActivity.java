package com.example.dell.concession;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private Button next_button,scan_button;
    private String uid=" Not Available ",name=" Not Available ",gender=" Not Available ",yearOfBirth=" Not Available ";

    DatabaseReference rootref= FirebaseDatabase.getInstance().getReference();

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser()==null){
            //Checking if user has registered. If not then the scanning page will not be displayed.
            finish();
            Toast.makeText(this, "Please Login First!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginPage.class));

        }

        user=firebaseAuth.getCurrentUser();

        scan_button=findViewById(R.id.scan_aadhaar);
        next_button=findViewById(R.id.next_button);


        scan_button.setOnClickListener(this);
        next_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==scan_button){
            scan_aadhaar_click();
            Intent i=new Intent(this,FormActivity.class)
                    .putExtra("u_id",uid)
                    .putExtra("name",name)
                    .putExtra("gender",gender)
                    .putExtra("yob",yearOfBirth);

            startActivity(i);
        }

        if (v==next_button){
            startActivity(new Intent(this,ConcessionFillActivity.class));
        }

    }


    public void scan_aadhaar_click(){

        Log.d("MainActivity: ","Inside scan_aadhar_click()");
        // we need to check if the user has granted the camera permissions
        // otherwise scanner will not work
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            return;
        }

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan a Aadharcard QR Code");
        integrator.setResultDisplayDuration(500);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            //String scanFormat = scanningResult.getFormatName();

            // process received data
            if(scanContent != null && !scanContent.isEmpty()){
                processScannedData(scanContent);
            }else{
                Toast toast = Toast.makeText(getApplicationContext(),"Scan Cancelled", Toast.LENGTH_SHORT);
                toast.show();
            }

        }else{
            Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    protected void processScannedData(String scanData){
        Log.d("MainActivity",scanData);
        XmlPullParserFactory pullParserFactory;

        try {
            // init the parserfactory
            pullParserFactory = XmlPullParserFactory.newInstance();
            // get the parser
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(scanData));

            // parse the XML
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("Rajdeol","Start document");
                } else if(eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {
                    // extract data from tag
                    //uid
                    uid = parser.getAttributeValue(null,DataAttributes.AADHAR_UID_ATTR);
                    //name
                    name = parser.getAttributeValue(null,DataAttributes.AADHAR_NAME_ATTR);
                    //gender
                    gender = parser.getAttributeValue(null,DataAttributes.AADHAR_GENDER_ATTR);
                    // year of birth
                    yearOfBirth = parser.getAttributeValue(null,DataAttributes.AADHAR_YOB_ATTR);

                    saveData(uid,name,gender,yearOfBirth);

                    Intent i=new Intent(this,FormActivity.class)
                            .putExtra("u_id",uid)
                            .putExtra("name",name)
                            .putExtra("gender",gender)
                            .putExtra("yob",yearOfBirth);

                    startActivity(i);


                } else if(eventType == XmlPullParser.END_TAG) {
                    Log.d("MainActivity","End tag "+parser.getName());

                } else if(eventType == XmlPullParser.TEXT) {
                    Log.d("MainActivity","Text "+parser.getText());

                }
                // update eventType
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData(String uid,String name,String gender,String yearOfBirth){
        UserInformation userInformation=new UserInformation(uid,name,gender,yearOfBirth);
        rootref.child(user.getUid()).setValue(userInformation);
    }
}
