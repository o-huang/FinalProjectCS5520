package edu.northeastern.finalprojectcs5520;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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

//                int[] milestones = {1, 3, 7, 14, 30, 50, 100, 200, 300, 365, 500};
                int[] milestones = {1,2, 3,4,5,6, 7, 14, 30, 50, 100, 200, 300, 365, 500};

                ArrayList<Integer> achieved = new ArrayList<>();
                for (int i = 0; i < milestones.length; i++) {
                    if (records.size() >= milestones[i]-20) {
                        achieved.add(milestones[i]);
                    }
                }
                System.err.println("*********** <arrayList> achieved");
                System.err.println(achieved);


                int badgesPerRow = 4;
                int numBadges = achieved.size();
                // Version I -- work well!
                for (int i = 0; i < achieved.size(); i++) {
                    LinearLayout row = new LinearLayout(getApplicationContext());
                    row.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));



                    int milestone = achieved.get(i);
                    ImageView badge = new ImageView(getApplicationContext());
                    badge.setImageResource(R.drawable.badge);
                    badge.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    badge.setContentDescription(milestone + " records");

                    TextView badgeText = new TextView(getApplicationContext());
                    badgeText.setText(String.valueOf(milestone));
                    badgeText.setTextColor(Color.WHITE);
                    badgeText.setGravity(Gravity.CENTER);

                    RelativeLayout badgeContainer = new RelativeLayout(getApplicationContext());
                    badgeContainer.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    badgeContainer.addView(badge);
                    badgeContainer.addView(badgeText);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) badgeText.getLayoutParams();
                    params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    badgeText.setLayoutParams(params);

                    LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
//                    badgeParams.weight = 1;

                    row.addView(badgeContainer, badgeParams);


//                     Add empty views to fill the row if necessary
                    int remainingBadges = numBadges - i;

                    if (remainingBadges < badgesPerRow) {
                        for (int k = 0; k < badgesPerRow - remainingBadges; k++) {
                            View emptyView = new View(getApplicationContext());
                            emptyView.setLayoutParams(new LinearLayout.LayoutParams(
                                    0,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            emptyView.setVisibility(View.INVISIBLE);
                            row.addView(emptyView);
                        }
                    }
                    mBadgeLayout.addView(row);


//                    mBadgeLayout.addView(badgeContainer);
                }

                // Solution with the badge_item and badgeAdapter
//                BadgeAdapter adapter = new BadgeAdapter(getApplicationContext(), achieved);
//                mGridView.setAdapter(adapter);



                // chatGPT 4.0
//                int badgesPerRow = 4;
//                int numBadges = achieved.size();
//
//                for (int i = 0; i < numBadges; i += badgesPerRow) {
//                    LinearLayout row = new LinearLayout(getApplicationContext());
//                    row.setLayoutParams(new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.MATCH_PARENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT));
//
//                    for (int j = i; j < i + badgesPerRow && j < numBadges; j++) {
//                        int milestone = achieved.get(j);
//                        ImageView badge = new ImageView(getApplicationContext());
//                        badge.setImageResource(R.drawable.badge);
//                        badge.setLayoutParams(new LinearLayout.LayoutParams(
//                                LinearLayout.LayoutParams.WRAP_CONTENT,
//                                LinearLayout.LayoutParams.WRAP_CONTENT));
//                        badge.setContentDescription(milestone + " records");
//
//                        TextView badgeText = new TextView(getApplicationContext());
//                        badgeText.setText(String.valueOf(milestone));
//                        badgeText.setTextColor(Color.WHITE);
//                        badgeText.setGravity(Gravity.CENTER);
//
//                        RelativeLayout badgeContainer = new RelativeLayout(getApplicationContext());
//                        badgeContainer.setLayoutParams(new LinearLayout.LayoutParams(
//                                LinearLayout.LayoutParams.WRAP_CONTENT,
//                                LinearLayout.LayoutParams.WRAP_CONTENT));
//                        badgeContainer.addView(badge);
//                        badgeContainer.addView(badgeText);
//
//                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) badgeText.getLayoutParams();
//                        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//                        badgeText.setLayoutParams(params);
//
//                        LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(
//                                LinearLayout.LayoutParams.WRAP_CONTENT,
//                                LinearLayout.LayoutParams.WRAP_CONTENT);
//                        badgeParams.weight = 1;
//
//                        row.addView(badgeContainer, badgeParams);
//                    }
//
//                    mBadgeLayout.addView(row);
//                }

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
