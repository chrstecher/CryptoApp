package at.itkolleg.sample;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ResourceBundle;

import Exceptions.InsufficientBalanceException;
import Exceptions.InvalidFeeException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        AnchorPane root = FXMLLoader.load(Main.class.getResource("main.fxml"),
                ResourceBundle.getBundle("at.itkolleg.sample.main"));

        Scene scene = new Scene(root, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        System.out.println("Hallo Welt!");
        BankAccount ba = new BankAccount();
        ba.deposit(new BigDecimal("100"));
        System.out.println(ba);

        try {
            ba.withdraw(new BigDecimal("50"));
            ba.withdraw(new BigDecimal("51"));
            System.out.println(ba);
        } catch (InsufficientBalanceException insufficientBalanceException)
        {
            System.out.println("Zu wenig Geld am Konto!");
            //System.out.println(insufficientBalanceException.getMessage());
        }

        CryptoCurrency crypto = CryptoCurrency.BTC;
        System.out.println(crypto.getCurrencyName());
        System.out.println(crypto.getCode());
        System.out.println(CryptoCurrency.valueOf("BTC").getCurrencyName());

        Transaction transaction = new Transaction(CryptoCurrency.ETH,
                new BigDecimal("1.23"),
                new BigDecimal("1567.8")
        );

        System.out.println(transaction);

        Wallet wallet = null;
        try {
            wallet = new Wallet("My BTC Wallet", CryptoCurrency.BTC, new BigDecimal("-0.01"));
        } catch (InvalidFeeException e) {
            System.out.println(e.getMessage()); //Ausgabe der Nachricht der Exception
            e.printStackTrace(); //Ausgabe wie diese Exception zustande kam
        }
        System.out.println(wallet);

        launch(args);
    }
}
