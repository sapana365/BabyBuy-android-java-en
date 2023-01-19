package com.sapana.mybabybuyfinalapplication;

import static com.sapana.mybabybuyfinalapplication.dBHelper.TABLENAME;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class AddActivity extends AppCompatActivity {
    dBHelper dBmain;
    SQLiteDatabase sqLiteDatabase;
    ImageView avatar;
    EditText name,price,location,details,isPurchased;
    Button submit,display,edit,textAdd;
    TextView textGone,textAdd2;
    BottomNavigationView nav;
    FloatingActionButton fab;
    int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().hide();
        nav = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        textGone = findViewById(R.id.textGone);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });

//bottom navigation bar
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        Intent intent0 = new Intent(AddActivity.this,HomeActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.List:
                        Intent intent = new Intent(AddActivity.this,DisplayData.class);
                        startActivity(intent);
                        break;
                    case R.id.feedback:
                        Intent intent3 = new Intent(AddActivity.this,Feedback.class);
                        startActivity(intent3);
                        break;
                    case R.id.logout:
                        Intent intent4 = new Intent(AddActivity.this,LoginActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.placeholder:
                        Intent intent5 = new Intent(AddActivity.this,AddActivity.class);
                        startActivity(intent5);
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });
         dBmain=new dBHelper(this);
        findid();
        insertDataItem();
        imagePick();
        editData();

        //shaking device to clear entry
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensorshake = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float floatTotal = Math.abs(x)+ Math.abs(y)+ Math.abs(z);
                if (floatTotal> 30)
                {

                    Toast.makeText(AddActivity.this, "Shaken to clear entry", Toast.LENGTH_SHORT).show();
                    avatar.setImageResource(R.drawable.ic_twotone_photo_camera_24);
                    name.setText("");
                    price.setText("");
                    location.setText("");
                    details.setText("");
                    isPurchased.setText("");

                }
                else{

                }
//
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(sensorEventListener, sensorshake, SensorManager.SENSOR_DELAY_NORMAL);

    }

    private void imagePick() {
        textAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AddActivity.this)
                        .cropSquare()
                        .compress(764)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(480, 480)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });
    }
    private void editData() {
        if (getIntent().getBundleExtra("userdata")!=null){
            Bundle bundle=getIntent().getBundleExtra("userdata");
            id=bundle.getInt("id");

            //for set name
            name.setText(bundle.getString("name"));
            price.setText(bundle.getString("price"));
            location.setText(bundle.getString("location"));
            details.setText(bundle.getString("details"));
            isPurchased.setText(bundle.getString("isPurchased"));
            //for image
            byte[]bytes=bundle.getByteArray("avatar");
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            avatar.setImageBitmap(bitmap);
            //visible edit button and hide submit button
            submit.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            textAdd2.setVisibility(View.GONE);
            textGone.setVisibility(View.VISIBLE);

        }
    }
    //for inserting items

    private void insertDataItem() {
        submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ContentValues cv=new ContentValues();
            cv.put("name",name.getText().toString());
            cv.put("price",price.getText().toString());
            cv.put("location",location.getText().toString());
            cv.put("details",details.getText().toString());
            cv.put("avatar",ImageViewToByte(avatar));
            cv.put("isPurchased",isPurchased.getText().toString());
            sqLiteDatabase=dBmain.getWritableDatabase();
            Long recinsert=sqLiteDatabase.insert(TABLENAME,null,cv);
            if (recinsert!=null){
                Toast.makeText(AddActivity.this, "inserted successfully", Toast.LENGTH_SHORT).show();
                //clear when click on submit button
                avatar.setImageResource(R.drawable.ic_twotone_photo_camera_24);
                name.setText("");
                price.setText("");
                location.setText("");
                details.setText("");
                isPurchased.setText("");

            }
        }
    });

        display.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(AddActivity.this,DisplayData.class));
        }
    });
    //for storing new data or update data
        edit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ContentValues cv=new ContentValues();

            cv.put("name",name.getText().toString());
            cv.put("price",price.getText().toString());
            cv.put("location",location.getText().toString());
            cv.put("details",details.getText().toString());
            cv.put("isPurchased",isPurchased.getText().toString());
            cv.put("avatar",ImageViewToByte(avatar));
            sqLiteDatabase=dBmain.getWritableDatabase();
            long recedit=sqLiteDatabase.update(TABLENAME,cv,"id="+id,null);

            if (recedit!=-1){
                Toast.makeText(AddActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                //clear data after submit
                name.setText("");
                price.setText("");
                location.setText("");
                details.setText("");
                isPurchased.setText("");
                avatar.setImageResource(R.drawable.ic_twotone_photo_camera_24);
                //edit hide and submit visible
                edit.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                textAdd2.setVisibility(View.GONE);
                textGone.setVisibility(View.VISIBLE);
            }
        }
    });

}

    private byte[] ImageViewToByte(ImageView avatar) {
        Bitmap bitmap=((BitmapDrawable)avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
        byte[]bytes=stream.toByteArray();
        return bytes;
    }
private void empty(){
    if(name.equals("") || price.equals("") || location.equals("") || details.equals("") || isPurchased.equals("") || avatar.equals(""))
        Toast.makeText(AddActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();

}
    private void findid() {
        avatar=(ImageView)findViewById(R.id.avatar);
        name=(EditText)findViewById(R.id.edit_name);
        price=(EditText)findViewById(R.id.edit_price);
        location=(EditText)findViewById(R.id.edit_location);
        details=(EditText)findViewById(R.id.edit_details);
        isPurchased=(EditText)findViewById(R.id.edit_isPurchased);
        submit=(Button)findViewById(R.id.btn_submit);
        display=(Button)findViewById(R.id.btn_display);
        edit=(Button)findViewById(R.id.btn_edit);
        textAdd = (Button) findViewById(R.id.textAdd);
        textAdd2 = (TextView) findViewById(R.id.textAdd2);
        textGone = (TextView) findViewById(R.id.textGone);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Image pickup success", Toast.LENGTH_SHORT).show();
            Uri Imageuri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(Imageuri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                avatar.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

    }
}