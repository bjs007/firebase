import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GroupHandler {

CountDownLatch cl = new CountDownLatch(1);

    private static GroupHandler groupHandler = null;

    public static GroupHandler getGroupHandler() {
        if (groupHandler == null) {

            groupHandler = new GroupHandler();
        }

        return groupHandler;
    }
//
//        public void onCallBack(List<ChildNodeWithDBReference> list, final int level) {
//            final String parentId = list.get(level).getCurrentNodeDbRef();
//            final String childId = list.get(level+1).getCurrentNodeDbRef();
//            final String path = "level - " + level;
//
//            firebaseDatabase.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//                {
//                    if(dataSnapshot.exists())
//                    {
//                        Iterator<DataSnapshot> dataSnapshotIterator = dataSnapshot.getChildren().iterator();
//                        while (dataSnapshotIterator.hasNext())
//                        {
//                            DataSnapshot ds = dataSnapshotIterator.next();
//
//                            Group group = ds.getValue(Group.class);
//                            if(group.getId().equals(parentId))
//                            {
//                                if(group.getChildrenIds() == null)
//                                {
//                                    group.setChildrenIds(new ArrayList<String>());
//                                }
//                                boolean childExist = false;
//
//                                for(String childId : group.getChildrenIds())
//                                {
//                                    if(childId.equals(childId))
//                                    {
//                                        childExist = true;
//                                        break;
//                                    }
//                                }
//
//                                if(!childExist)
//                                {
//                                    Map<String, Object> updatedNodeAtCurrentLevelDetail = new HashMap<>();
//
//                                    updatedNodeAtCurrentLevelDetail.put(path +"/"+ group.getId() + "/", group);
//
//                                    firebaseDatabase.updateChildren(updatedNodeAtCurrentLevelDetail);
//                                }
//
//                            }
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError)
//                {
//
//                }
//            });
//        }

    public void GroupCreationRequest(List<GroupCreationRequest> groupRequest) {

    }

    public void createGroup(List<GroupCreationRequest> groupRequest) throws InterruptedException {
//        String[] tokens = groupPathWithName.split("/");
//
//        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Groups");
        final List<ChildNodeWithDBReference> list = new ArrayList();
//
//        int levels = -1;
//
//        List<String> pathNodes = getPathNodes(tokens);
//
//        if(pathNodes.size() == 0)
//        {
//            return;
//        }


        for (final GroupCreationRequest dbNode : groupRequest) {

            list.add(new ChildNodeWithDBReference(dbNode, null, null));
        }

        Thread t1 = new Thread(new ParentCreation(list, cl));

        t1.start();

        t1.join();

        cl.await();

        Thread.sleep(9000);

//        t1.join();

//        Thread.sleep(9000);
//        Thread t2 = new Thread(new ChildCreation(list));
//        t2.start();
    }


    private List<String> getPathNodes(String[] tokens) {
        List<String> nodes = new ArrayList<>();
        for (String token : tokens) {
            if (!token.equals("/")) {
                nodes.add(token);
            }
        }

        return nodes;
    }
}


