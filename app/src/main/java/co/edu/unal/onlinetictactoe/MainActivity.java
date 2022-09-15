package co.edu.unal.onlinetictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LinearLayout J1Layout,J2Layout;
    private ImageView image1,image2,image3,image4,image5,image6,image7,image8,image9;
    private TextView J1,J2;
    private final List<int[]> combinationsList = new ArrayList<>();
    private String playeruniqueId="0";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gmvstriqui-default-rtdb.firebaseio.com/");
    private boolean opponentFound=false;
    private String opponentuniqueId="0";
    private String status="matching";
    private String playerTurn="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        J1Layout = findViewById(R.id.J1Layout);
        J2Layout = findViewById(R.id.J2Layout);
        J1 = findViewById(R.id.J1);
        J2 = findViewById(R.id.J2);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);
        image6 = findViewById(R.id.image6);
        image7 = findViewById(R.id.image7);
        image8 = findViewById(R.id.image8);
        image9 = findViewById(R.id.image9);
        final String getPlayerName = getIntent().getStringExtra("playerName");
        combinationsList.add(new int[]{0,1,2});
        combinationsList.add(new int[]{3,4,5});
        combinationsList.add(new int[]{6,7,8});
        combinationsList.add(new int[]{0,3,6});
        combinationsList.add(new int[]{1,4,7});
        combinationsList.add(new int[]{2,5,8});
        combinationsList.add(new int[]{2,4,6});
        combinationsList.add(new int[]{0,4,8});

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Esperando oponente");
        progressDialog.show();

        playeruniqueId=String.valueOf(System.currentTimeMillis());
        J1.setText(getPlayerName);
        databaseReference.child("connections").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(opponentFound){
                    if(snapshot.hasChildren()){
                        for(DataSnapshot connections: snapshot.getChildren()){

                            Long conID = Long.parseLong(connections.getKey());
                            int getPlayersCount = (int)connections.getChildrenCount();
                            if(status.equals("waiting")){
                                if(getPlayersCount==2){
                                    playerTurn=playeruniqueId;
                                    applyPlayerTurn(playerTurn);

                                }
                            }
                        }
                    }
                }
                else {
                    String connectionUniqueID = String.valueOf(System.currentTimeMillis());
                    snapshot.child(connectionUniqueID).child(playeruniqueId).child("player_name").getRef().setValue(getPlayerName);
                    status="waiting";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void applyPlayerTurn(String playeruniqueId2){
        if(playeruniqueId2.equals(playeruniqueId))
        {
            J1Layout.setBackgroundResource(R.drawable.round_back_dark_blue);
            J2Layout.setBackgroundResource(R.drawable.round_back_dark_gray_20);
        }
        else{
            J2Layout.setBackgroundResource(R.drawable.round_back_dark_blue);
            J1Layout.setBackgroundResource(R.drawable.round_back_dark_gray_20);
        }
    }
}