package com.eric.timicaller31;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.eric.timicaller31.MyEvent.AlarmFragment;
import com.eric.timicaller31.MyTimer.TimerFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, ValueEventListener {
private static final int ACTIVITY_NUM = 1;
private static final int REQUEST_LOGIN = 100;
private static final int RC_SIGN_IN = 101;
private Context mContext = MainActivity.this;
boolean logon = false;
private FirebaseAuth auth;
private FirebaseUser user;
private Member member;


//    @Override
//    protected void onStart() {
//        super.onStart();
//        auth.addAuthStateListener(this);
//
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        auth.removeAuthStateListener(this);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        setBoNaView();
        setMyPager();


    }
    private void setBoNaView(){

        BottomNavigationViewEx b = (BottomNavigationViewEx)findViewById(R.id.bar);
        BoNaViewHelper.setBoNaView(b);
        BoNaViewHelper.enableNavigation(mContext,b);
        Menu menu = b.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    private void setMyPager(){
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AlarmFragment());
        adapter.addFragment(new TimerFragment());

        ViewPager viewPager = (ViewPager)findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_event_alarm);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_timer);

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user = firebaseAuth.getCurrentUser();String userid= user.getUid();
        if(user !=null){
            FirebaseDatabase.getInstance().getReference("users")
                    .child(user.getUid())
                    .child("uid")
                    .setValue(user.getUid());
            FirebaseDatabase.getInstance().getReference("users")
                    .child(user.getUid())
                    .child("uid")
                    .addValueEventListener(this);


        }
        else {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(Arrays.asList(
                    //new AuthUI.IdpConfig.FacebookBuilder().build(),
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build()
            )).setIsSmartLockEnabled(false).build(),RC_SIGN_IN);
        }
        getSharedPreferences("Timi",MODE_PRIVATE)
                .edit()
                .putString("USERID",userid)
                .apply();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        member = dataSnapshot.getValue(Member.class);
        if(member.getNickname()==null){

        }
        else {
            FirebaseDatabase.getInstance().getReference("users")
                    .child(user.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            member = dataSnapshot.getValue(Member.class);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==REQUEST_LOGIN){
//            if(resultCode!=RESULT_OK){
//                finish();
//            }
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
