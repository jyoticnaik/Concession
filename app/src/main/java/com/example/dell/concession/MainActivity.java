package com.example.dell.concession;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.TRANSPARENT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean test=false;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore con_db;
    private Button next_button,scan_button;
    private String uid=" Not Available ",name=" Not Available ",gender=" Not Available ",yearOfBirth=" Not Available ";
    private String address,pincode,uid_string;
    private boolean doesExist;

    private DatabaseHelper myDB=new DatabaseHelper(MainActivity.this);
    private DocumentReference confirm_db;

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();
        con_db = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser()==null){
            //Checking if user has registered. If not then the scanning page will not be displayed.
            finish();
            Toast.makeText(this, "Please Login First!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginPage.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

        }


        scan_button=findViewById(R.id.scan_aadhaar);
        next_button=findViewById(R.id.next_button);

        scan_button.setOnClickListener(this);
        next_button.setOnClickListener(this);
        if(!test) {
            next_button.setEnabled(test);
            next_button.setAlpha((float) 0.25);
        }
    }


    @Override
    public void onClick(View v) {
        if(v==scan_button){
            scan_aadhaar_click();
        }

        if (v==next_button){
            Cursor uid_cursor = myDB.getUID();

            if(uid_cursor.getCount() == 0){
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
            }
            while (uid_cursor.moveToNext()){
                uid_string = uid_cursor.getString(0);
            }

            confirm_db = con_db.collection("Students").document(uid_string).collection("ConfirmationDetails").document("Confirmation");

            confirm_db.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        Toast.makeText(MainActivity.this, "You Have Already Confirmed Your Concession!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        startActivity(new Intent(MainActivity.this,ConcessionFillActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    startActivity(new Intent(MainActivity.this,ConcessionFillActivity.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
            });
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

        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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
                }
                else if(eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {
                    // extract data from tag
                    //uid
                    uid = parser.getAttributeValue(null,DataAttributes.AADHAR_UID_ATTR);
                    //name
                    name = parser.getAttributeValue(null,DataAttributes.AADHAR_NAME_ATTR);
                    //gender
                    gender = parser.getAttributeValue(null,DataAttributes.AADHAR_GENDER_ATTR);
                    // year of birth
                    yearOfBirth = parser.getAttributeValue(null,DataAttributes.AADHAR_YOB_ATTR);

                    address = parser.getAttributeValue(null,DataAttributes.AADHAR_HOUSE_ATTR) +", "
                            + parser.getAttributeValue(null,DataAttributes.AADHAR_STREET_ATTR)
                            + parser.getAttributeValue(null,DataAttributes.AADHAR_VTC_ATTR) +", District: "
                            + parser.getAttributeValue(null,DataAttributes.AADHAR_DIST_ATTR) +", SubDistrict: "
                            + parser.getAttributeValue(null,DataAttributes.AADHAR_SUBDIST_ATTR) +", State: "
                            + parser.getAttributeValue(null,DataAttributes.AADHAR_STATE_ATTR);

                    pincode = parser.getAttributeValue(null,DataAttributes.AADHAR_PC_ATTR);

                    Log.d("MainActivity",address);

                    addDataToDatabase(uid,name,gender,yearOfBirth,address,pincode);

                    finish();
                    startActivity(new Intent(MainActivity.this,FormActivity.class));

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

    public void addDataToDatabase(String s_uid,String s_name,String s_gender,String s_yearOfBirth,String s_address,String s_pincode){
        boolean insertionSucessful = myDB.insertData(s_uid,s_name,s_gender,s_yearOfBirth,s_address,s_pincode);
        if(insertionSucessful)
            Log.d("addDataToDatabase()","INSERTION SUCCESSFUL");
        else
            Log.d("addDataToDatabase()","INSERTION UN-SUCCESSFUL");
    }
    /*public void saveData(String uid,String name,String gender,String yearOfBirth){
        UserInformation userInformation=new UserInformation(uid,name,gender,yearOfBirth);
        rootref.child(user.getUid()).setValue(userInformation);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.form_menu){
           if(test)
               startActivity(new Intent(this,FormActivity.class));
           else
               Toast.makeText(this, "First Scan the aadhar card!", Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.logout_menu){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this,LoginPage.class));
        }

        return true;
    }
}
