import com.google.api.client.json.Json;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class Jsonhandler
{
    private static Jsonhandler jsonhandler = null;
    private List<GroupCreationRequest> groupCreationRequests = new ArrayList<>();

    public static Jsonhandler getJsonhandler() {
        if(jsonhandler == null)
        {
            jsonhandler = new Jsonhandler();
        }

        return jsonhandler;
    }

    public List<GroupCreationRequest> getGrups(String filepath)
    {
        String line = "";
        String splitBy = ",";
        try
        {

            Workbook workbook = WorkbookFactory.create(new File(filepath));


            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            System.out.println("Retrieving Sheets using Iterator");
            while (sheetIterator.hasNext())
            {
                Sheet sheet = sheetIterator.next();
                System.out.println("=> " + sheet.getSheetName());
            }

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
            Iterator<Row> rowIterator = sheet.rowIterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Now let's iterate over the columns of the current row
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String cellValue = dataFormatter.formatCellValue(cell);
                    System.out.print(cellValue + "\t");
                }
                System.out.println();
            }


////parsing a CSV file into BufferedReader class constructor
//            BufferedReader br = new BufferedReader(new FileReader(filepath));
//            int row = 0;
//            while ((line = br.readLine()) != null)   //returns a Boolean value
//            {
//                if(row == 0 )
//                {
//                    row++;
//                    continue;
//                }
//                String[] group = line.split(splitBy);    // use comma as separator
//                System.out.println("Employee [Eng Name=" + group[1] + ", Eng Desc=" + group[2] + ", Hindi Name=" + group[3]
//                +", Hindi Desc=" + group[4]);
//
//                GroupCreationRequest groupCreationRequest = new GroupCreationRequest(group[1] , group[2] , group[3], group[4]);
//                groupCreationRequests.add(groupCreationRequest);
//            }
        }
        catch (IOException | InvalidFormatException e)
        {
            e.printStackTrace();
        }

        return groupCreationRequests;
    }

}
