package com.ukom.kasapengguna.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SPHelper {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context context;

    public SPHelper(Context context) {
        this.context = context;
        this.sp = context.getSharedPreferences("apiku", context.MODE_PRIVATE);
        this.editor = sp.edit();
    }

    public String getValue(String key){
        return sp.getString(key, "");
    }

    public void setValue(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

    public String getToken(){
        return getValue("token");
    }

    public void setToken(String token){
        editor.putString("token", token);
        editor.commit();
    }

    public void setUsername(String username){
        editor.putString("username", username);
        editor.commit();
    }

    public String getUsername(){
        return getValue("username");
    }

    public void setIdPengguna(int idpengguna){
        editor.putInt("idpengguna", idpengguna);
        editor.commit();
    }

    public int getIdPengguna(){
        return sp.getInt("idpengguna", 0);
    }

    public void setEmail(String email){
        editor.putString("email", email);
        editor.commit();
    }

    public void clearData(){
        editor.clear();
        editor.commit();
    }


    public String getEmail(){
        return getValue("email");
    }
}
