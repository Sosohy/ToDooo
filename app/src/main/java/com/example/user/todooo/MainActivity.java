package com.example.user.todooo;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

        private ArrayList<String> items = null;
        private ArrayAdapter<String> adapter = null;

        public  SharedPreferences.Editor edit;
        public  SharedPreferences pref;

        private AlarmManager al;

        String TODOS;

    @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            //로딩화면
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {

            }


            al = (AlarmManager)getSystemService(Context.ALARM_SERVICE);


            // ListView의 데이터를 저장할 Adapter 생성
            ListView lv = (ListView)findViewById(R.id.lv_data);
            items = new ArrayList<String>();
            adapter = new ArrayAdapter<String>(this, R.layout.list_item , items);
            lv.setAdapter(adapter);


           lv.setOnItemClickListener( new ListViewItemClickListener() );
            lv.setOnItemLongClickListener( new ListViewItemLongClickListener() );


            //저장했던 정보 불러오기
            SharedPreferences pref = getSharedPreferences("Add",0);
            TODOS = pref.getString("TODO" , " " );
            items.add(TODOS);


        }



        /////알림설정

    public void onStart(View v){
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + 3);
      //  cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 23, 52, 0);

        long interval = 5000;

        Toast.makeText(getApplicationContext() , "3시간마다 알려드릴게요!.!" , Toast.LENGTH_LONG).show();
        al.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), interval, pi);

    }

    public void onStop(View v){
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 1, i, 0);

        al.cancel(pi);
    }

    ////////////////////


    //  버튼이 클릭되었을 경우, 내용 추가
    public void onBtnAddClick( View v )
    {
        EditText ed = (EditText)findViewById(R.id.ed_input);
        String value = ed.getText().toString();
        items.add(value);

        //정보저장
        pref = getSharedPreferences("Add" , 0);
        edit = pref.edit();
        edit.putString("TODO", value);
        edit.commit();

        // 호출하지 않을 경우, ListView에 추가된 문자열이 보이지 않는다.
        adapter.notifyDataSetChanged();
        ed.setText("");

    }


    //수정하는 창 띄우기
   private class ListViewItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);

            ad.setMessage("수정할 내용을 입력하세요.");   // 내용 설정

            // EditText
            final EditText et = new EditText(MainActivity.this);
            ad.setView(et);

            // 확인 버튼
            ad.setPositiveButton("수정하기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String value = et.getText().toString();
                     selectedPos = position;

            items.set(selectedPos, value);

                    //정보저장
                    pref = getSharedPreferences("Add" , 0);
                    edit = pref.edit();
                    edit.putString("TODO", value);
                    edit.commit();

            // listview 갱신
            adapter.notifyDataSetChanged();

                    dialog.dismiss(); //닫기!
                }
            });

          // 취소 버튼 설정
            ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            //창띄우기
            ad.show();

        }
    }


    int selectedPos = -1;

   // 길게 클릭하면 아이템 삭제
    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {
            selectedPos = position;
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setTitle(R.string.alert_title_question);

            // 확인 버튼 클릭
            alertDlg.setPositiveButton( R.string.button_yes, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int which )
                {
                    items.remove(selectedPos);

                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });

            // 아니오 버튼
            alertDlg.setNegativeButton( R.string.button_no, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int which ) {
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
            });

            alertDlg.setMessage( String.format( getString(R.string.alert_msg_delete), items.get(position)) );
            //String.format( getString(R.string.alert_msg_delete) 찾아보기
            alertDlg.show();
            return false;
        }

    }

}
