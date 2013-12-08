package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rachel
 * Date: 12/8/13
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class Client extends JFrame{
    public static final int PORT = 9999;
    private String serverResponse;
    private int numGuesses;


    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    JFrame ui = new Client();
                    ui.pack();
                    ui.setMinimumSize(ui.getSize());
                    ui.setLocationRelativeTo(null);
                    ui.setVisible(true);
                    ui.setDefaultCloseOperation(EXIT_ON_CLOSE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

           });

    }

    private Socket connection;
    private JButton send;
    private JLabel guessLabel;
    private JTextField guess, response, guesses;
    private JPanel content;
    private PrintWriter write;

    ActionListener tx = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String text = Client.this.guess.getText();
            Client.this.write.println(text);
            numGuesses ++;
            String guessNum = Integer.toString(numGuesses);
            Client.this.guesses.setText("Guesses: "+guessNum);
        }
    };

    public Client () throws IOException {
        super("Client");
        this.content = new JPanel(new GridLayout(0, 1));
        this.add(content);
        this.connection = new Socket("localhost", Server.PORT);
        this.write = new PrintWriter(this.connection.getOutputStream(), true);
        this.guessLabel = new JLabel("Enter your guess");
        this.guess = new JTextField(20);
        this.guesses = new JTextField(20);
        this.response = new JTextField(20);
        this.send = new JButton("Send");

        this.guess.addActionListener(this.tx);
        this.send.addActionListener(this.tx);
        this.content.add(this.guessLabel);
        this.guessLabel.setVisible(true);
        this.content.add(this.guess);
        this.guess.setVisible(true);
        this.content.add(this.response);
        this.response.setVisible(true);
        this.content.add(guesses);
        this.guesses.setVisible(true);
        this.content.add(this.send, BorderLayout.SOUTH);

        final SwingWorker<Void, String> reader = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (BufferedReader read = new BufferedReader(new InputStreamReader(Client.this.connection.getInputStream()));) {
                    while (!this.isCancelled()) {
                        String guessResponse = read.readLine();
                        if (guessResponse == null) this.cancel(true);
                        else {

                            serverResponse = guessResponse;
                            Client.this.response.setText(serverResponse);
                            if(serverResponse == "correct"){
                                System.exit(1);
                            }
                        }
                    }
                } catch (Exception e) {
                    //nothing to do
                }
                System.out.println("Done");
                Client.this.guess.setEnabled(false);
                Client.this.send.setEnabled(false);
                setTitle("Closed");
                return null;
            }
            @Override
            protected void process(List<String> chunks) {
                Client.this.guess.setText(chunks.get(0));
            }

        };

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                reader.cancel(true);
                try {
                    Client.this.connection.close();
                } catch (IOException e1) {
                    //nothing to do
                }
            }
        });
        reader.execute();
    }

    }

