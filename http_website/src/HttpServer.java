import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.StringTokenizer;

public class HttpServer implements Runnable, Serializable{

    private Socket socket;
    private int port;
    private BufferedReader in;
    private PrintStream out;
    private BufferedOutputStream dataOut;

    public HttpServer(Socket socket, int port) throws  IOException{
        this.socket = socket;
        this.port = port;

        //ignore servers, else error
        if(port != 0) {
            //input and output streams
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintStream(socket.getOutputStream());
            this.dataOut = new BufferedOutputStream(socket.getOutputStream());
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
    	//start server
        new Thread(new HttpServer(null,0)).start();
    }


    @Override
    public void run() {

        try {

            //server for port 8080
            if (port==0){
                launchServer(8080);
            }

            //if clients asks for port 8080
            else if (port==8080){
                String request = waitForRequest();

                switch (request) {
                    case " " -> answerRequest("dbs_website.html", "html");
                    default -> answerRequest(getQuery(request), "string");
                }

            }

            //server thread will never get here, only client thread
            in.close();
            out.close();
            dataOut.close();
            System.out.println("client on port:"+ port +" closed");

        } catch (IOException | SQLException e) { e.printStackTrace(); }

    }


    public void launchServer(int port) throws IOException {

        //create socket
        ServerSocket server = new ServerSocket(port);

        while(true){
            //listen for connection
            Socket socket = server.accept();
            System.out.println("\nclient on port:"+ port +" connected");

            //start new thread
            new Thread(new HttpServer(socket,port)).start();
        }

    }

    public String waitForRequest() throws IOException {

        //first line is the request.. like GET / HTTP/1.1
        String request = in.readLine();
//System.out.println(request);
        //wait till there is a new request
        while ( request==null )
            request = in.readLine();

        //remove GET and HTTP/1.1 ...
        String type = request.replace("GET /","").replaceAll("HTTP/[\\d.]*","");

        //empty the in buffer
        while ( !request.equals("") ){
            request = in.readLine();
//System.out.println(request);
}
        return type;
    }




    public void answerRequest(String msg, String type) throws IOException, SQLException {

        //allocate values
        byte[] msg_bytes = null;
        int msg_length = 0;
        String msg_type = "";

        switch (type) {
            case "string" -> {
                msg_bytes = msg.getBytes();
                msg_length = msg.length();
                msg_type = "text/plain";
            }
            case "html" -> {
                File file = new File(msg);         
                msg_bytes = Files.readAllBytes(file.toPath());
                msg_length = (int) file.length();
                msg_type = "text/html";
            }
        }

        // send HTTP Headers
        out.println("HTTP/1.1 200 OK");
        //out.println("Server: Java HTTP Server from Thore : 1.0");
        //out.println("Date: " + new Date());
        out.println("Content-type: " + msg_type);
        out.println("Content-length: " + msg_length);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        //sending the website
        dataOut.write(msg_bytes);
        dataOut.flush();

        System.out.println("send msg : "+ msg);

    }


    public String getQuery (String sql) throws SQLException {

        //prepare query... remove the "%20" from query and swap %E3 and %C3 to
        sql = sql.replaceAll("%20"," ");
        sql = sql.replaceAll("%3E",">");
        sql = sql.replaceAll("%3C","<");
        System.out.println("send following sql query to database: "+sql);

        //connect to database
        String JdbcURL = "jdbc:postgresql://localhost/datenbanksysteme";
        String Username = "postgres";
        String password = "postgres";
        Connection con = null;
        Statement stmt = null;
        con = DriverManager.getConnection(JdbcURL, Username, password);
        stmt = con.createStatement();

        //perform query, check for syntax error
        ResultSet rs;
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {return "error";}

        //get column metadata
        ResultSetMetaData metaData = rs.getMetaData();
        int columns = metaData.getColumnCount();

        //get column names
        String column_names ="";
        for (int i = 1; i<=columns; i++)
            column_names = column_names + metaData.getColumnName(i) + ":";

        //get column content
        String column_content ="";
        while (rs.next()) {
            for (int i = 1; i<=columns; i++)
                column_content = column_content + rs.getString(i) + ":";
        }

        //System.out.println(column_names);
        //System.out.println(column_content);

        //close database connection
        stmt.close();
        con.close();

        //push number of columns to the start and remove the last ','
        return columns + ":" +column_names + column_content.substring(0,column_content.length()-1);
    }


}
