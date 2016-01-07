
package clientfile;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

/**
 *
 * @author AKID(CSE'11,CUET)
 */
class ClientFiles extends JFrame implements ActionListener {

    public JButton B(JButton B) {
        B.setBackground(new Color(160, 195, 220));
        B.setFont(new Font("Courier New", 1, 14));
        B.setPreferredSize(new Dimension(100, 27));
        B.addActionListener(this);
        return B;
    }

    public JLabel L(JLabel L) {
        L.setFont(new Font("Maiandra GD", 0, 15));
        L.setPreferredSize(new Dimension(70, 20));
        return L;
    }

    public JTextField T(JTextField T) {
        T.setFont(new Font("Courier New", 0, 15));
        T.addActionListener(this);
        T.setPreferredSize(new Dimension(430, 50));
        return T;
    }
    JTextField txtFile;

    public void Interface() {

        JFrame mainFrame = new JFrame("C l i en t");
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 1, 1));
        JPanel labelPanel = new JPanel(new GridLayout(0, 1, 1, 1));
        JPanel textPanel = new JPanel(new GridLayout(0, 1, 1, 1));


        JLabel lblFile = L(new JLabel("     F i l e  l o c a t i o n:"));
        labelPanel.add(new JLabel(""), BorderLayout.SOUTH);
        labelPanel.add(lblFile, BorderLayout.NORTH);

        txtFile = T(new JTextField());
        textPanel.add(new JLabel(""), BorderLayout.NORTH);
        textPanel.add(T(txtFile), BorderLayout.CENTER);
        textPanel.add(new JLabel(""), BorderLayout.SOUTH);

        JButton btnTransfer = B(new JButton("Browse"));
        buttonPanel.add(btnTransfer);

        mainFrame.add(labelPanel, BorderLayout.NORTH);
        mainFrame.add(textPanel, BorderLayout.CENTER);
        mainFrame.add(buttonPanel, BorderLayout.SOUTH);

        mainFrame.setVisible(true);
        mainFrame.setBounds(300, 300, 700, 220);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {

        JFileChooser fileDlg = new JFileChooser();
        fileDlg.showOpenDialog(this);
        String filename = fileDlg.getSelectedFile().getAbsolutePath();
        txtFile.setText("  " + filename);

        try {

            Socket sk = new Socket("localhost", 1234);
            OutputStream output = sk.getOutputStream();

            /* Send filename to server */
            OutputStreamWriter outputStream = new OutputStreamWriter(sk.getOutputStream());
            outputStream.write(fileDlg.getSelectedFile().getName() + "\n");
            outputStream.flush();

            /* Get reponse from server */
            BufferedReader inReader = new BufferedReader(new InputStreamReader(sk.getInputStream()));

            /* Read the first line */
            String serverStatus = inReader.readLine();

            /* If server is ready, send the file */
            if (serverStatus.equals("done")) {

                FileInputStream file = new FileInputStream(filename);
                byte[] buffer = new byte[sk.getSendBufferSize()];
                int bytesRead = 0;

                while ((bytesRead = file.read(buffer)) > 0) {
                    output.write(buffer, 0, bytesRead);
                }

                output.close();
                file.close();
                sk.close();

                JOptionPane.showMessageDialog(this, "T r a n s f e r  c o m p l e t e");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}

public class ClientFile {

    public static void main(String[] args) {
        ClientFiles r = new ClientFiles();
        r.Interface();
    }
}
