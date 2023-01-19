package com.sapana.mybabybuyfinalapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME ="Session";
    String SESSION_KEY ="session_user";
    public  Session(Context context)
    {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


    }
    public void saveSession(UserModel user){
        //save session of user whenever user logged in
        String email = user.getEmail();
        editor.putString(SESSION_KEY,email ).commit();
        editor.apply();

    }
    public int getSession(){
        //return user whose session is saved
        return sharedPreferences.getInt(SESSION_KEY,-1);
    }
    public  void removeSession(){
        editor.putInt(SESSION_KEY, -1).commit();
    }


}
