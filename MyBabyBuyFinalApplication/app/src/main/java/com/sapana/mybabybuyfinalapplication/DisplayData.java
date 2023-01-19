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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class DisplayData extends AppCompatActivity {
    dBHelper dBmain;
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<String> ArrayList;
    FloatingActionButton floating;
    BottomNavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        getSupportActionBar().hide();
//        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple)));
        nav = findViewById(R.id.bottomNavigationView);
        dBmain=new dBHelper(this);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        Intent intent0 = new Intent(DisplayData.this,HomeActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.List:
                        Intent intent = new Intent(DisplayData.this,DisplayData.class);
                        startActivity(intent);
                        break;
                    case R.id.feedback:
                        Intent intent3 = new Intent(DisplayData.this,Feedback.class);
                        startActivity(intent3);
                        break;
                    case R.id.logout:
                        Intent intent4 = new Intent(DisplayData.this,LoginActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.placeholder:
                        Intent intent5 = new Intent(DisplayData.this,AddActivity.class);
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
        floating_btn();

        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

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
                    Toast.makeText(DisplayData.this, "Swiped to the right", Toast.LENGTH_SHORT).show();
                    //Mark purchased
                    Markpurchased((long) viewHolder.itemView.getTag());
                    displayData();
                }

            }
            @Override
            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(DisplayData.this, R.color.purple_500))
                        .addSwipeLeftActionIcon(R.drawable.ic_twotone_delete_24)
                        .addSwipeLeftLabel("Delete")
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(DisplayData.this, R.color.colorRed))
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_shopping_cart_checkout_24)
                        .addSwipeRightLabel("Mark as purchased")
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(DisplayData.this,R.color.purple_200))

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
        myAdapter = new MyAdapter(this, R.layout.singledata, models, sqLiteDatabase);
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
        myAdapter=new MyAdapter(this,R.layout.singledata,models,sqLiteDatabase);
        recyclerView.setAdapter(myAdapter);
    }
    private void floating_btn(){
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayData.this,AddActivity.class);
                startActivity(intent);

            }
        });
    }



    private void findId() {
        recyclerView=findViewById(R.id.rv);
        floating = (FloatingActionButton) findViewById(R.id.floatingActionButton);
    }
}