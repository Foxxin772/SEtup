import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
    private ArrayList<Peuple> sockets = new ArrayList<>();
    private ServerSocket serverSocket;
    public static void main(String[] srt) throws Exception
    {
        new Main().labuda();
    }

    private void labuda() throws Exception
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int port = Integer.parseInt(reader.readLine());
        serverSocket = new ServerSocket(port);
        Client client = new Client();
        client.run();
    }

    private class Peuple extends Thread
    {
        protected Socket socket;
        protected Client client;
        public BufferedReader reader;
        public BufferedWriter writer;

        public Peuple(Socket socket, Client client) {
            this.socket = socket;
            this.client = client;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            }catch (Exception e)
            {
                System.out.println("Бляяяяяяя!!!!!!!!!! Сервер сдох!!!!!!!!");
            }

        }

        public Socket getSocket() {
            return socket;
        }

        @Override
        public void run() {
            try {
                wite();
            }catch (Exception e)
            {
                System.out.println("Бляяяяяяя!!!!!!!!!! Сервер сдох!!!!!!!!");
            }
        }


        private void wite() throws Exception
        {
            while (true)
            {
                client.transit(reader.readLine());
            }
        }
    }
    private class Client extends Thread {

        @Override
        public void run() {
            try {
                startServer();
            }catch (Exception e)
            {
                System.out.println("Бляяяяяяя!!!!!!!!!! Сервер сдох!!!!!!!!");
            }
        }

        private void startServer() throws Exception {
            while (true) {
                Peuple peuple = new Peuple(serverSocket.accept(), this);
                peuple.run();
                for (Peuple sock : sockets) {
                    sock.writer.write("new client!!!!" + peuple.getSocket().getLocalSocketAddress().toString());
                }
                sockets.add(peuple);
            }
        }

        public void transit(String string) throws Exception
        {
            for (Peuple sock : sockets) {
                sock.writer.write(string);
                sock.writer.flush();
            }
        }
    }
}
