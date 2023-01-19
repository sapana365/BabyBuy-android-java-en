package com.sapana.mybabybuyfinalapplication;

import static com.sapana.mybabybuyfinalapplication.dBHelper.TABLENAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.media.session.MediaSessionManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView nav;
    FloatingActionButton fab;
    dBHelper dBmain;
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    HomeAdapter myAdapter;
    java.util.ArrayList<String> ArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        dBmain = new dBHelper(this);
        nav = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.floatingActionButton);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });



        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:

                        Intent i = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(i);
                        break;
                    case R.id.List:
                        Intent intent = new Intent(HomeActivity.this, DisplayData.class);
                        startActivity(intent);
                        break;
                    case R.id.feedback:
                        Intent intent3 = new Intent(HomeActivity.this, Feedback.class);
                        startActivity(intent3);
                        break;
                    case R.id.logout:
                        Toast.makeText(HomeActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(HomeActivity.this, LoginActivity.class);
                        finish();
                        startActivity(in);
                        break;
                    case R.id.placeholder:
                        Intent intent5 = new Intent(HomeActivity.this, AddActivity.class);
                        startActivity(intent5);
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });
        findId();
        displayData();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    RemoveItem((long) viewHolder.itemView.getTag());
                } else if (direction == ItemTouchHelper.RIGHT) {
                    Toast.makeText(HomeActivity.this, "Swiped to the right", Toast.LENGTH_SHORT).show();
                    //Mark purchased
                    Markpurchased((long) viewHolder.itemView.getTag());
                    displayData();
                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.purple_500))
                        .addSwipeLeftActionIcon(R.drawable.ic_twotone_delete_24)
                        .addSwipeLeftLabel("Delete")
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.colorRed))
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_shopping_cart_checkout_24)
                        .addSwipeRightLabel("Mark as purchased")
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.purple_200))

                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        }).attachToRecyclerView(recyclerView);
    }
        private void Markpurchased(long id) {
            sqLiteDatabase = dBmain.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("isPurchased", "✅ Marked As Purchased");
            long recUpdate = sqLiteDatabase.update(TABLENAME,cv,"id ="+ id, null);
            if(recUpdate != -1){
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                displayData();
            }
            else{
                Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show();

            }
        }

        private void RemoveItem(long id) {
            sqLiteDatabase = dBmain.getWritableDatabase();
            sqLiteDatabase.delete(TABLENAME, "id =" + id, null);
            Toast.makeText(this, "Item Removed", Toast.LENGTH_SHORT).show();
            sqLiteDatabase = dBmain.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * from " + TABLENAME, null);
            ArrayList<Model> models = new ArrayList<>();
            while (cursor.moveToNext()) {
                int Id=cursor.getInt(0);
                String name=cursor.getString(1);
                String price=cursor.getString(2);
                String location=cursor.getString(3);
                String details=cursor.getString(4);
                String isPurchased=cursor.getString(5);
                byte[]avatar=cursor.getBlob(6);
                models.add(new Model(Id,name,price,location,details,isPurchased,avatar));
            }
            cursor.close();
            myAdapter = new HomeAdapter(this, R.layout.singledata, models, sqLiteDatabase);
            recyclerView.setAdapter(myAdapter);
        }


        private void displayData()
        {
            sqLiteDatabase=dBmain.getReadableDatabase();
            Cursor cursor=sqLiteDatabase.rawQuery("select *from "+TABLENAME+"",null);
            ArrayList<Model> models=new ArrayList<>();
            while (cursor.moveToNext())
            {
                int id=cursor.getInt(0);
                String name=cursor.getString(1);
                String price=cursor.getString(2);
                String location=cursor.getString(3);
                String details=cursor.getString(4);
                String isPurchased=cursor.getString(5);
                byte[]avatar=cursor.getBlob(6);
                models.add(new Model(id,name,price,location,details,isPurchased,avatar));
            }
            cursor.close();
            myAdapter=new HomeAdapter(this,R.layout.singledata,models,sqLiteDatabase);
            recyclerView.setAdapter(myAdapter);
        }



        private void findId() {
            recyclerView=findViewById(R.id.rv);

        }






}