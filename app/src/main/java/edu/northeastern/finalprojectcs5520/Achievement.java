package edu.northeastern.finalprojectcs5520;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Arrays;

public class Achievement extends AppCompatActivity {

    TextView currentUserName;
    FirebaseUser currentUser;
    FirebaseAuth auth;

    FirebaseDatabase database;
    DatabaseReference reference;
    String username;

    private LinearLayout mBadgeLayout;
    private GridView mGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        auth = FirebaseAuth.getInstance();
        currentUserName = findViewById(R.id.currentUser);
        currentUser = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference();

        mBadgeLayout = findViewById(R.id.badge_layout);
        mGridView = findViewById(R.id.grid_view);


        //Check if there is a user. If not goes to login page.
        if (currentUser == null) {
            openLoginPage();
        } else {
            //Getting username from firebase and setting the textview
            String email = currentUser.getEmail();
            username = email.split("@")[0];
            currentUserName.setText("💪🏻You made it, "+username+"!");
        }

        reference.child("users").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DataSnapshot> records = new ArrayList<>();
                for(DataSnapshot record : snapshot.child("recordWeights").getChildren()) {
                    records.add(record);
                }
                System.err.println("***********current records num:");
                System.err.println(records.size());

                int[] milestones = {1, 3, 7, 14, 30, 50, 100, 200, 300, 365, 500};

                ArrayList<Integer> achieved = new ArrayList<>();
                for (int i = 0; i < milestones.length; i++) {
                    if (records.size() >= milestones[i]) {
                        achieved.add(milestones[i]);
                    }
                }
                System.err.println("*********** <arrayList> achieved");
                System.err.println(achieved);


                int badgesPerRow = 4;
                int numBadges = achieved.size();
                mBadgeLayout.removeAllViews();

                mBadgeLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout row = new LinearLayout(getApplicationContext());
                row.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                for (int i = 0; i < achieved.size(); i++) {
                    if(i % badgesPerRow == 0) {
                        mBadgeLayout.addView(row);
                        row = new LinearLayout(getApplicationContext());
                        row.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                    }
                    int milestone = achieved.get(i);
                    ImageView badge = new ImageView(getApplicationContext());
                    badge.setImageResource(R.drawable.medal_pic_small);


                    badge.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    badge.setContentDescription(milestone + " records");


                    TextView badgeText = new TextView(getApplicationContext());
                    badgeText.setText(String.valueOf(milestone));
                    badgeText.setTextColor(Color.BLUE);
                    badgeText.setPadding(0,0,0,50);
                    badgeText.setTextSize(17);

                    RelativeLayout badgeContainer = new RelativeLayout(getApplicationContext());
                    badgeContainer.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    badgeContainer.addView(badge);
                    badgeContainer.addView(badgeText);
                    badgeContainer.setPadding(40,15,40,15);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) badgeText.getLayoutParams();
                    params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    badgeText.setLayoutParams(params);
                    row.addView(badgeContainer);

                }

                mBadgeLayout.addView(row);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void openLoginPage() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
