package at.itkolleg.sample;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ResourceBundle;

import Exceptions.*;
import infrastruktur.CurrentCurrencyPrices;
import infrastruktur.FileDataStore;
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
        ba.deposit(new BigDecimal("1000"));
        System.out.println(ba);

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
            wallet = new Wallet("My BTC Wallet", CryptoCurrency.BTC, new BigDecimal("1"));
        } catch (InvalidFeeException e) {
            System.out.println(e.getMessage()); //Ausgabe der Nachricht der Exception
            e.printStackTrace(); //Ausgabe wie diese Exception zustande kam
        }
        System.out.println(wallet);

        try {
            wallet.buy(new BigDecimal("10"), new BigDecimal("5"), ba);
        } catch (InvalidAmountException e) {
            e.printStackTrace();
        } catch (InsufficientBalanceException insufficientBalanceException) {
            insufficientBalanceException.printStackTrace();
        }

        System.out.println(ba);
        System.out.println(wallet);
        try {
            wallet.sell(new BigDecimal("10"), new BigDecimal("5"), ba);
        } catch (InsufficientAmountException e) {
            e.printStackTrace();
        } catch (InvalidAmountException e) {
            e.printStackTrace();
        }

        System.out.println(ba);
        System.out.println(wallet);

        WalletList walletList = new WalletList();
        walletList.addWallet(wallet);

        System.out.println(walletList);

        CurrentPriceForCurrency currentPrices = new CurrentCurrencyPrices();
        try {
            BigDecimal result = currentPrices.getCurrentPrice(CryptoCurrency.BTC);
            System.out.println(result);
        } catch (GetCurrentPriceException e) {
            e.printStackTrace();
        }

        DataStore dataStore = new FileDataStore();
        try {
            dataStore.saveBankAccount(ba);
        } catch (SaveDataException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }

        try {
            BankAccount bankAccount2 = dataStore.retrieveBankAccount();
            System.out.println(bankAccount2);
        } catch (RetrieveDataException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }

        launch(args);
    }
}
