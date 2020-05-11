import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class FirebaseGroupUploader
{
    public static void main(String args[]) throws InterruptedException {
//        Scanner in = new Scanner(System.in);
//        String fileName = in.nextLine();
//
//        if(fileName == null || fileName.equals(""))
//        {
//            System.exit(1);
//        }

        Jsonhandler jsonhandler = Jsonhandler.getJsonhandler();
        List<GroupCreationRequest>  groupCreationRequests = jsonhandler.getGrups("/Users/bijaysharma/Desktop/group.csv");
        if(groupCreationRequests == null || groupCreationRequests.size() == 0)
        {
            System.out.println("There is no group in the csv file");
        }

//        createGroup(groupCreationRequests);

    }

    private static void createGroup(List<GroupCreationRequest> groupRequest) throws InterruptedException {

        final List<ChildNodeWithDBReference> list = new ArrayList();

        for (final GroupCreationRequest dbNode : groupRequest) {

            list.add(new ChildNodeWithDBReference(dbNode, null, null));
        }

        CountDownLatch cl = new CountDownLatch(1);

        Thread t1 = new Thread(new ParentCreation(list, cl));
        t1.start();
        cl.await();

    }


}
