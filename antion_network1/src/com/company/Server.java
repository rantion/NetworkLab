package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Server  extends JFrame implements ActionListener{
    public static final int PORT = 9999;
    private Game game = new Game();

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame ui = new Server();
                ui.pack();
                ui.setMinimumSize(ui.getSize());
                ui.setLocationRelativeTo(null);
                ui.setVisible(true);
                ui.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });
    }

    class Acceptor extends SwingWorker<Void, Integer> {

        @Override
        protected Void doInBackground() throws Exception {
            try (ServerSocket listen = new ServerSocket(PORT)) {
                listen.setSoTimeout(10 * 1000);
                while (!this.isCancelled()) {
                    try {
                        @SuppressWarnings("resource")
                        Socket client = listen.accept();
                        Connection next = new Connection(client);
                        synchronized (Server.this.connections) {
                            Server.this.connections.add(next);
                            publish(Server.this.connections.size());
                        }
                        next.start();
                    } catch (SocketTimeoutException e) {
                        System.out.println("Checking if cancelled");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                //nothing to do
            }
            return null;
        }

        @Override
        protected void process(List<Integer> chunks) {
            Server.this.clients.setText(chunks.get(0).toString());
        }

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if("Enter".equals(e.getActionCommand())){
            try{
                String enteredNumber = number.getText();
                game.reset(Integer.parseInt(enteredNumber));
            }
            catch(Exception p){
                System.out.println("Something has gone wrong, please enter a number.");
            }
        }

    }

    class Connection extends Thread {
        Socket client;
        boolean running = true;
        private PrintWriter writer;

        public Connection(Socket client) throws IOException {
            this.client = client;
            this.writer = new PrintWriter(client.getOutputStream(), true);
        }

        @Override
        public void run() {
            try {
                PrintWriter out =
                        new PrintWriter(client.getOutputStream(), true);
                BufferedReader read = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));

                String inputLine, outputLine;

                while (this.running){

                        String guessString = read.readLine();
                        int guess = Integer.parseInt(guessString);

                        outputLine = game.checkGuess(guess);
                        out.println(outputLine);

                        if (outputLine.equals("correct"))
                            break;
                    }


            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port "
                        + PORT + " or listening for a connection");
                System.out.println(e.getMessage());
            }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Server.this.clients.setText(String.valueOf(Server.this.connections.size()));
                    }
                });


        }
    }

    private Acceptor acceptor;
    private JTextField clients, number;
    private JPanel content;
    private JLabel enterNumber;
    private JButton enter;
    private List<Connection> connections = new ArrayList<>();


    public Server(){
        super("Server");
        this.content = new JPanel(new GridLayout(4, 4));
        this.clients = new JTextField(String.valueOf(0));
        this.content.add(new JLabel("Clients Connected:", SwingConstants.RIGHT));
        this.content.add(this.clients);

        this.setContentPane(this.content);
        enterNumber= new JLabel("Enter the Number you would like the other player to Guess");
        enterNumber.setHorizontalTextPosition(SwingConstants.CENTER);
        enterNumber.setVerticalTextPosition(SwingConstants.CENTER);
        this.add(enterNumber);

        number = new JTextField();
        this.add(number);



        enter = new JButton("Enter Number");
        enter.setVerticalTextPosition(AbstractButton.CENTER);
        enter.setHorizontalTextPosition(AbstractButton.LEADING);
        enter.setMnemonic(KeyEvent.VK_D);
        enter.setActionCommand("Enter");
        enter.addActionListener(this);
        this.add(enter);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                synchronized (Server.this.connections) {
                    for (Connection c : Server.this.connections) {
                        System.out.println("IDK what this is supposed to do. cool");
                    }
                }
                Server.this.acceptor.cancel(true);
            }
        });
        this.acceptor = new Acceptor();
        this.acceptor.execute();
    }
}




