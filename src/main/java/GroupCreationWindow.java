
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import java.sql.*;
public class GroupCreationWindow
{
    private TextField textField;
    private Frame frame;
    private Button submt;
    GroupCreationWindow()
    {
       Initialize();
    }

    private void Initialize()
    {
        frame = new Frame();
        frame.setBounds(100, 100, 1000, 620);

        textField = new TextField();
        textField.setBounds(50,100, 200,30);
        submt = new Button("Submit");

        textField.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

            }
        });

        submt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

            }
        });
    }


}
