package com.meituan.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.meituan.robust.patch.RobustModify;
//import com.meituan.robust.patch.annotaion.Add;
//import com.meituan.robust.patch.annotaion.Modify;

import com.meituan.robust.patch.RobustModify;
import com.meituan.robust.patch.annotaion.Add;
import com.meituan.robust.patch.annotaion.Modify;

import java.lang.reflect.Field;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    public static int test2 = 2;
    protected static String name = "SecondActivity";
    private ListView listView;
    private String[] multiArr = {"列表1", "列表2", "列表3", "列表4"};

    @Modify
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView = (ListView) findViewById(R.id.listview);
        TextView textView = (TextView) findViewById(R.id.secondtext);
        Button button = (Button) findViewById(R.id.button2);
//        //change text on the  SecondActivity
//        button.setOnClickListener(new BtnClickListener());
        textView.setText(getTextInfo());
    }

    //    @Modify
    private String getTextInfo() {
//        RobustModify.modify();
//        return getArray1()[0] ;
        Log.d("chlog", "444");
//        String dsf=other(4);
//        String sdf=MainActivity.mainStatic();
        return "222";
    }

    public static void tsetRunnable(String info, Runnable runnable) {
        Log.d("chlog", "runnable=" + info);
        runnable.run();
    }


    //    @Add
    public String[] getArray() {
        return new String[]{"hello", "world"};
    }

    //    @Add
    public String[] getArray1() {
        return new String[]{"hello1", "world1"};
    }


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(SecondActivity.this, "from implements onclick ", Toast.LENGTH_SHORT).show();

    }

    public static Field getReflectField(String name, Object instance) throws NoSuchFieldException {
        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(name);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
                // ignore and search next
            }
        }
        throw new NoSuchFieldException("Field " + name + " not found in " + instance.getClass());
    }

    public static Object getFieldValue(String name, Object instance) {
        try {
            return getReflectField(name, instance).get(instance);
        } catch (Exception e) {
            Log.d("robust", "getField error " + name + "   target   " + instance);
            e.printStackTrace();
        }
        return null;
    }

    private void printLog(@NonNull String tag, @NonNull String[][] args) {
        int i = 0;
        int j = 0;
        for (String[] array : args) {
            for (String arg : array) {
                Log.d(tag, "args[" + i + "][" + j + "] is: " + arg);
                j++;
            }
            i++;
        }
    }
}