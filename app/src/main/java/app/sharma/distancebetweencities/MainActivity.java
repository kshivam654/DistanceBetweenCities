package app.sharma.distancebetweencities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchField;
    private EditText mSearchField2;

    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private String srcLati, srcLang, destLati, destLang;

    ArrayList<Places> arrayList = new ArrayList<>();

    Button caculate;

    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mSearchField = (EditText) findViewById(R.id.src_search);
        mSearchField2 = (EditText) findViewById(R.id.dest_search);

        caculate = (Button) findViewById(R.id.calculate);

        result = (TextView) findViewById(R.id.result);

        mRecyclerView = (RecyclerView) findViewById(R.id.src_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerView2 = (RecyclerView) findViewById(R.id.dest_recyclerview);
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView2.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //for search text change
        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()){
                    setAdapter(s.toString());
                }
            }
        });
        mSearchField2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (!s.toString().isEmpty()){
                            setAdapter2(s.toString());
                        }
                    }
                });

        caculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double val = distance(Double.parseDouble(srcLati),Double.parseDouble(srcLang),Double.parseDouble(destLati),Double.parseDouble(destLang));
                result.setText(String.valueOf(val));
            }
        });
    }
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void setAdapter(final String searchString) {
        databaseReference.child("Coordinates").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //for every new search
                arrayList.clear();
                mRecyclerView.removeAllViews();

                int counter = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String name = snapshot.child("name").getValue(String.class);
                    srcLati = snapshot.child("latitude").getValue(String.class);
                    srcLang = snapshot.child("longitude").getValue(String.class);
                    if (name.startsWith(searchString)){
                        arrayList.add(new Places(name, srcLati, srcLang));
                        counter++;
                    }

                    //for the top 15 results
                    if (counter  == 15){
                        break;
                    }

                }
                SearchAdapter searchAdapter;

                searchAdapter = new SearchAdapter(MainActivity.this, arrayList, mSearchField, mRecyclerView);
                mRecyclerView.setAdapter(searchAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setAdapter2(final String searchString) {
        databaseReference.child("Coordinates").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //for every new search
                arrayList.clear();
                mRecyclerView2.removeAllViews();

                int counter = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String name = snapshot.child("name").getValue(String.class);
                    destLati = snapshot.child("latitude").getValue(String.class);
                    destLang = snapshot.child("longitude").getValue(String.class);
                    if (name.startsWith(searchString)){
                        arrayList.add(new Places(name, destLati, destLang));
                        counter++;
                    }

                    //for the top 15 results
                    if (counter  == 15){
                        break;
                    }

                }

                SearchAdapter searchAdapter;

                searchAdapter = new SearchAdapter(MainActivity.this, arrayList, mSearchField2, mRecyclerView2);
                mRecyclerView2.setAdapter(searchAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}