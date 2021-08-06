package com.example.ioc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.ioc.impl.ContentView;
import com.example.ioc.impl.OnClick;
import com.example.ioc.impl.ViewInject;

@ContentView(R.layout.demo_activity)
public class DemoActivity extends Activity {


    @ViewInject(R.id.text1)
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectUtils.inject(this);

        textView.setText("注解了解一下");
    }

    @OnClick(R.id.text1)
    public void click(View view){
        Toast.makeText(this,  textView.getText().toString(),Toast.LENGTH_LONG).show();
    }
}
