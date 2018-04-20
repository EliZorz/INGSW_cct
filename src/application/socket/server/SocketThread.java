package application.socket.server;

import application.details.DishesDbDetails;
import application.rmi.server.ServerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

//quello corretto
public class SocketThread extends Thread {

    private String line = null;
    private BufferedReader in = null;  //uso questo e non Scanner perché è synchronized
    private PrintWriter out = null;
    private Socket s = null;
    private ServerImpl impl;  //mi serve per poter chiamare le funzioni  ESSENZIALE!!!!!

    //devo considerare che per chiamare  i metodi devo mandare dei messaggi

    public SocketThread(Socket s, ServerImpl impl) {
        this.impl = impl;
        this.s = s;
    }

    @Override
    public void run() {
        try {
            //creo in e out per la comunicazione con il client
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            out = new PrintWriter(s.getOutputStream());

        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }

        try {

            while (true) {
                System.out.println("Ready to recieve a message");

                line = in.readLine();   //attende fino a quando arriva un messaggio

                System.out.println("recieved " + line);

                String responce = this.doAction(line);  //passo il messaggio al doAction che decide cosa fare

                System.out.println("sending back : " + responce);

                out.println(responce);  //manda al socket client la risposta

                out.flush();
            }


        } catch (IOException e) {

            line = this.getName();  //salva in line il nome del thread che sta eseguendo
            System.out.println("IO Error/ Client " + line + " terminated abruptly");
        } catch (NullPointerException e) {
            line = this.getName();
            System.out.println("Client " + line + " Closed");  //stampa nel server che tale client è chiuso
        } finally {
            try {
                System.out.println("Connection Closing..");
                if (in != null) {
                    in.close();
                    System.out.println(" Socket Input Stream Closed");
                }

                if (out != null) {
                    out.close();
                    System.out.println("Socket Out Closed");
                }
                if (s != null) {
                    s.close();  //fa terminare la accept di quel solo client
                    //System.out.println(s.isClosed()); //serve per controllare che la socket sia chiusa
                    System.out.println("Socket Closed");
                }

            } catch (IOException ie) {
                System.out.println("Socket Close Error");
            }
        }
    }

    private String doAction(String line) throws IOException {

        String[] credentials = line.split("\\s+");
        String[] what = line.split("\\*");
        String ret = null;

        if (credentials[0].equals("login")){

            System.out.println("Username: " + credentials[1]);
            System.out.println("Password: " + credentials[2]);

            if (impl.funzLog(credentials[1], credentials[2])) {

                return "ok";
            } else {
                System.out.println("non va bene");
                return "no";
            }
        }


        else if(credentials[0].equals("loadmenu")){
            System.out.println("Richiedo l'apertura del menu");
            if(impl.loadMenu() != null)
                ret = impl.loadMenu().get(0).getNumber()+" "+impl.loadMenu().get(0).getEntree()+" "+impl.loadMenu().get(0).getMainCourse()+ " "+impl.loadMenu().get(0).getDessert()+" "+ impl.loadMenu().get(0).getSideDish()+" "+impl.loadMenu().get(0).getDrink()+" "+impl.loadMenu().get(0).getDay();
                return ret;
        }
            else if(credentials[0].equals("addMenu")){
                    System.out.println("Invio il nuovo menu al database");
                    System.out.println(credentials[7]);
                    LocalDate d = LocalDate.parse(credentials[7],DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    System.out.println(d);

                    if(impl.addMenu(credentials[1],credentials[2],credentials[3],credentials[4],credentials[5],credentials[6],d))
                        return "Ok";
                    else if(credentials[0].equals("loadChild")){
                System.out.println("sto caricando i bambini");
                if (impl.loadData() != null) {
                    ret = impl.loadData().get(0).getName() + "*" + impl.loadData().get(0).getSurname() + "*" + impl.loadData().get(0).getCf() + "*" + impl.loadData().get(0).getBornOn() + "*" + impl.loadData().get(0).getBornWhere() + "*" + impl.loadData().get(0).getResidence() + "*" + impl.loadData().get(0).getAddress() + "*" + impl.loadData().get(0).getCap() + "*" + impl.loadData().get(0).getProvince();
                    return ret;
                }
            }
            else if(what[0].equals("addData")){
                        //devo finirla per capire se fa o meno errori per via delle stringhe
                    }
                    else if(credentials[0].equals("loadIngr")){
                            System.out.println("invio richiesta a server impl");
                            if(impl.loadIngr() != null){
                                ret = impl.loadIngr().get(0).getIngr();
                                return ret;
                            }
                    }

        }
        return "no";
    }
}
