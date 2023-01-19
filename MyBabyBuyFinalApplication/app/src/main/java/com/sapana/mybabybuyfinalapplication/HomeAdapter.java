package com.sapana.mybabybuyfinalapplication;

import static android.content.Context.MODE_PRIVATE;
import static com.sapana.mybabybuyfinalapplication.dBHelper.TABLENAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<com.sapana.mybabybuyfinalapplication.MyAdapter.ViewHolder> {
        Context context;
        int singledata;
        ArrayList<Model> modelArrayList;
        SQLiteDatabase sqLiteDatabase;
        String MymessageString;
        //generate constructor
        public HomeAdapter(Context context, int singledata, ArrayList<Model> modelArrayList, SQLiteDatabase sqLiteDatabase) {
            this.context = context;
            this.singledata = singledata;
            this.modelArrayList = modelArrayList;
            this.sqLiteDatabase = sqLiteDatabase;
        }
        @NonNull
        @Override
        public com.sapana.mybabybuyfinalapplication.MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.singledata,null);
            return new com.sapana.mybabybuyfinalapplication.MyAdapter.ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull com.sapana.mybabybuyfinalapplication.MyAdapter.ViewHolder holder, int position) {
            final Model model = modelArrayList.get(position);
            byte[] image = model.getProavatar();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.imageavatar.setImageBitmap(bitmap);
            holder.txtname.setText(model.getUsername());
            holder.txtprice.setText(model.getPrice());
            holder.txtisPurchased.setText(model.getIsPurchased());
            holder.txtlocation.setText(model.getLocation());
            holder.txtdetails.setText(model.getDetails());

            long id = model.getId();
            holder.itemView.setTag(id);


//flow menu
            holder.flowmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.flowmenu);
                    popupMenu.inflate(R.menu.flow_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit_menu:
                                    ///////
                                    //edit operation
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("id", model.getId());

                                    bundle.putString("name", model.getUsername());
                                    bundle.putString("price", model.getPrice());
                                    bundle.putString("location", model.getLocation());
                                    bundle.putString("details", model.getDetails());
                                    bundle.putString("isPurchased", model.getIsPurchased());
                                    bundle.putByteArray("avatar", model.getProavatar());
                                    Intent intent = new Intent(context, AddActivity.class);
                                    intent.putExtra("userdata", bundle);
                                    context.startActivity(intent);
                                    break;
                                case R.id.delete_menu:
                                    ///delete operation
                                    dBHelper dBmain = new dBHelper(context);
                                    sqLiteDatabase = dBmain.getReadableDatabase();
                                    long recdelete = sqLiteDatabase.delete(TABLENAME, "id=" + model.getId(), null);
                                    if (recdelete != -1) {
                                        Toast.makeText(context, "data deleted", Toast.LENGTH_SHORT).show();
                                        //remove position after deleted
                                        modelArrayList.remove(holder.getBindingAdapterPosition());
                                        //update data
                                        notifyDataSetChanged();
                                    }
                                    break;
                                case R.id.share_menu:
                                    //sharing the item details as a sms text
                                    dBHelper dbmain2 = new dBHelper(context);
                                    sqLiteDatabase = dbmain2.getReadableDatabase();
                                    Cursor cursor = sqLiteDatabase.rawQuery("SELECT * from Items where id="+ id, null);
                                    //ArrayList<ItemModel> models = new ArrayList<>();
                                    if(cursor.getCount() > 0) {
                                        cursor.moveToFirst();
                                        String name = cursor.getString(1);
                                        String price = cursor.getString(2);
                                        String location = cursor.getString(3);
                                        String Details = cursor.getString(4);
                                        SharedPreferences pref = v.getContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
                                        String username = pref.getString("username","BabyBuyUser");
                                        // SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
                                        MymessageString =username + " has sent this Babybuy item: \n" + "Product name: " + name +"\nPrice: " + price + "\nStore location: "+ location +"\nProduct Details: "+ Details;
                                    }
                                    //opening sms section
                                    Intent intent1 = new Intent();
                                    intent1.setAction(Intent.ACTION_SEND);
                                    intent1.putExtra(Intent.EXTRA_TEXT,MymessageString);
                                    intent1.setType("text/plain");
                                    intent1 = Intent.createChooser(intent1,"babyBuy items details");
                                    context.startActivity(intent1);

                                    break;
                                default:
                                    return false;
                            }
                            return false;
                        }
                    });
                    //display menu
                    popupMenu.show();
                }

            });
        }

        @Override
        public int getItemCount() {
            return modelArrayList.size();

        }
        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imageavatar;
            TextView txtname,txtprice,txtlocation,txtisPurchased,txtdetails;
            ImageButton flowmenu;

            RelativeLayout mainLayout;



            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageavatar=(ImageView)itemView.findViewById(R.id.viewavatar);
                txtname=(TextView)itemView.findViewById(R.id.txt_name);
                txtprice=(TextView)itemView.findViewById(R.id.txt_price);
                txtlocation=(TextView)itemView.findViewById(R.id.text_location);
                txtisPurchased=(TextView)itemView.findViewById(R.id.txt_purchased);
                txtdetails=(TextView)itemView.findViewById(R.id.text_details);
                flowmenu=(ImageButton)itemView.findViewById(R.id.flowmenu);
                mainLayout = itemView.findViewById(R.id.mainLayout);
            }
        }
    }


