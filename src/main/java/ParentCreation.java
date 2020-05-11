
import androidx.annotation.NonNull;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ParentCreation implements Runnable {
    private DatabaseReference firebaseDatabase;
    List<ChildNodeWithDBReference> childNodeWithDBReferences;
    CountDownLatch cl;

    public ParentCreation(List<ChildNodeWithDBReference> childNodeWithDBReferences, CountDownLatch cl) {
        firebaseDatabase = getFirebaseAccess();
        this.childNodeWithDBReferences = childNodeWithDBReferences;
        this.cl = cl;
    }

    @Override
    public void run() {
        createGroup(childNodeWithDBReferences, 0);
    }

    private void createGroup(final List<ChildNodeWithDBReference> groupPathWithName, final int level) {


        final String currentLevel = "level - " + level;
        boolean isFunctionCallAtLeafNode = false;

        if (level == groupPathWithName.size() - 1) {
            isFunctionCallAtLeafNode = true;
        }

        final boolean leafLevel = isFunctionCallAtLeafNode;


        firebaseDatabase.child(currentLevel).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                Group requiredNodeAtCurrentLevel = null;

                while (iterator.hasNext()) {
                    Group group = iterator.next().getValue(Group.class);
                    String newGroupName = groupPathWithName.get(level).getRequest().getGroupNameInEng().trim();

                    if (group != null && group.getEngName().equalsIgnoreCase(newGroupName)) {
                        requiredNodeAtCurrentLevel = group;
                        break;
                    }
                }

                if (requiredNodeAtCurrentLevel == null) {
                    DatabaseReference newGroupDBRefId = firebaseDatabase.child(currentLevel).push();
                    final String groupId = newGroupDBRefId.getKey();
                    requiredNodeAtCurrentLevel = createGroup(groupPathWithName.get(level).getRequest(), groupId, level, leafLevel);


                    if (level == 0) {
                        requiredNodeAtCurrentLevel.setParentId("root");
                    } else {
                        requiredNodeAtCurrentLevel.setParentId(groupPathWithName.get(level - 1).getCurrentNodeDbRef());
                    }
                }

                groupPathWithName.get(level).setCurrentNodeDbRef(requiredNodeAtCurrentLevel.getId());

                final Map<String, Object> updatedNodeAtCurrentLevelDetail = new HashMap<>();

                if(level == 0)
                {
                    updatedNodeAtCurrentLevelDetail.put(currentLevel + "/" + requiredNodeAtCurrentLevel.getId() + "/",
                            requiredNodeAtCurrentLevel);
                }
                else
                {
                    updatedNodeAtCurrentLevelDetail.put(currentLevel + "/"
                            + groupPathWithName.get(level - 1).getCurrentNodeDbRef() + "/"
                            + requiredNodeAtCurrentLevel.getId(),
                            requiredNodeAtCurrentLevel);
                }

                final Group finalNodeHere = requiredNodeAtCurrentLevel;

                firebaseDatabase.updateChildrenAsync(updatedNodeAtCurrentLevelDetail);

                if (!leafLevel)
                {
                    createGroup(groupPathWithName, level + 1);
                }
                else
                {
                    Map<String, Object> updateLeafNodes = new HashMap<>();
                    updateLeafNodes.put("leafs" + "/" + finalNodeHere.getId() + "/", finalNodeHere);
                    firebaseDatabase.updateChildren(updateLeafNodes, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            System.out.println("Error occurred : " + error);
                            cl.countDown();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Group createGroup(GroupCreationRequest request, String id, int level, boolean isLeaf) {

        String currentUser = "admin";
        Group grp = new Group(request.getGroupNameInEng(), id, currentUser, getCurrentDate(), getCurrentTime(), isLeaf, level, new ArrayList<String>());
        grp.setEngDesc(request.getGroupDescInEng());
        grp.setHinName(request.getGroupNameInHin());
        grp.setHinDesc(request.getGroupDescInHin());
        return grp;
    }


    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        return currentDate.format(calendar.getTime());
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        return currentTime.format(calendar.getTime());
    }

    private DatabaseReference getFirebaseAccess() {
        DatabaseReference ref = null;
        try {
            FileInputStream serviceAccount = new FileInputStream("/Users/bijaysharma/Desktop/frienders-a1fdb-firebase-adminsdk.json");

// Initialize the app with a service account, granting admin privileges
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://frienders-a1fdb.firebaseio.com/")
                    .build();
            FirebaseApp.initializeApp(options);


            System.out.println("Hello world");

// As an admin, the app has access to read and write all data, regardless of Security Rules
             ref = FirebaseDatabase.getInstance()
                    .getReference("Groups");
//
        } catch (Exception ex) {
        }

        return ref;
    }

}
