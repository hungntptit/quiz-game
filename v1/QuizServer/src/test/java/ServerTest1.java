
import com.ltm.model.ObjectWrapper;
import com.ltm.model.User;
import com.ltm.quizserver.dao.UserDAO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest1 {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(5056);

        while (true) {
            Socket s = null;

            try {
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(s, ois, oos);

                // Invoking the start() method
                t.start();
//                oos.close();
            } catch (Exception e) {
                s.close();
                e.printStackTrace();
            }
        }
    }
}

class ClientHandler extends Thread {

    final ObjectInputStream ois;
    final ObjectOutputStream oos;
    final Socket s;

    // Constructor
    public ClientHandler(Socket s, ObjectInputStream ois, ObjectOutputStream oos) {
        this.s = s;
        this.ois = ois;
        this.oos = oos;
    }

    @Override
    public void run() {
        System.out.println("thread started");
        ObjectWrapper received;
        ObjectWrapper toreturn;
//        while (true) {
        try {
            // receive the request from client
            received = (ObjectWrapper) ois.readObject();
            System.out.println(received);
            User u = null;
            // write on output stream based on the
            // request from the client
            switch (received.getPerformative()) {

                case ObjectWrapper.LOGIN_USER:
                    u = (User) received.getData();
                    System.out.println(u);
                    toreturn = new ObjectWrapper(ObjectWrapper.REPLY_LOGIN_USER, new UserDAO().checkLogin(u));
//                        dos.writeUTF(toreturn);
                    oos.writeObject(toreturn);
                    break;

                case ObjectWrapper.REGISTER_USER:
                    u = (User) received.getData();
                    System.out.println(u);
                    toreturn = new ObjectWrapper(ObjectWrapper.REPLY_REGISTER_USER, new UserDAO().saveUser(u));
//                        dos.writeUTF(toreturn);
                    oos.writeObject(toreturn);
                    break;

                default:
                    oos.writeObject(new ObjectWrapper(-1000, "Invalid input"));
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        }

        try {
            // closing resources
            this.ois.close();
            this.oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
