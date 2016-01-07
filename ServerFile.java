
package serverfile;

import java.io.*;
import java.net.*;

/**
 *
 * @author AKID(CSE'11,CUET)
 */

public class ServerFile {

    public static void main(String args[]) throws Exception {
        System.out.println("Server running...");
        ServerSocket server = new ServerSocket(1234);

        Socket sk = server.accept();

        System.out.println("Server accepted client");
        InputStream input = sk.getInputStream();
        BufferedReader inReader = new BufferedReader(new InputStreamReader(sk.getInputStream()));
        BufferedWriter outReader = new BufferedWriter(new OutputStreamWriter(sk.getOutputStream()));

        /* Read the filename */
        String filename = inReader.readLine();

        if (!filename.equals("")) {
            /* Reply back to client with READY status */
            outReader.write("done\n");
            outReader.flush();
        }

        /* Create a new file in the tmp directory using the filename */
        FileOutputStream wr = new FileOutputStream(new File("C://Users//Abnormal//Desktop//Download//" + filename));

        byte[] buffer = new byte[sk.getReceiveBufferSize()];
        int bytesReceived = 0;

        while ((bytesReceived = input.read(buffer)) > 0) {
            /* Write to the file */
            wr.write(buffer, 0, bytesReceived);
        }
    }
}
