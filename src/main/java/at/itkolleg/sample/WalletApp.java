package at.itkolleg.sample;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ResourceBundle;

import Exceptions.*;
import domain.*;
import infrastruktur.CurrentCurrencyPrices;
import infrastruktur.FileDataStore;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ui.GlobalContext;

public class WalletApp extends Application {

    //UI Parts
    private static Stage mainStage;
    public static final String GLOBAL_WALLET_LIST = "walletlist";
    public static final String GLOBAL_BANK_ACCOUNT = "bankaccount";
    public static final String GLOBAL_CURRENT_CURRENCY_PRICES = "currencyprices";

    public static void switchScene(String fxmlFile, String resourceBundle) {
        try {
            Parent root = FXMLLoader.load(WalletApp.class.getResource(fxmlFile), ResourceBundle.getBundle(resourceBundle));
            Scene scene = new Scene(root);
            mainStage.setScene(scene); //Man bekommt diese MainStage über die start Methode
            mainStage.show();
        } /*catch (NullPointerException nullPointerException)
        {
            WalletApp.showErrorDialog("Could not load new scene!");
            nullPointerException.printStackTrace();
        }*/
        catch (Exception ioException) {
            WalletApp.showErrorDialog("Could not load new scene!");
            ioException.printStackTrace();
        }
    }
    {

    }

    public static void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("An exception occured: " + message);
        alert.showAndWait(); //Benutzer muss den Dialog wegklicken um weiterzumachen -> Programmausführung bleibt stehen.
    }

    //File-Handling-Parts

    private BankAccount loadBankAccountFromFile() throws RetrieveDataException {
        DataStore dataStore = new FileDataStore();
        BankAccount bankAccount = dataStore.retrieveBankAccount();
        System.out.println("Bankaccount loaded!");
        return bankAccount;
    }

    private WalletList loadWalletListFromFile() throws RetrieveDataException {
        DataStore dataStore = new FileDataStore();
        WalletList walletList = dataStore.retrieveWalletList();
        System.out.println("WalletList loaded!");
        return walletList;
    }

    private void storeBankAccountToFile(BankAccount bankAccount) throws SaveDataException
    {
        DataStore dataStore = new FileDataStore();
        dataStore.saveBankAccount(bankAccount);
    }

    private void storeWalletListToFile(WalletList walletList) throws SaveDataException
    {
        DataStore dataStore = new FileDataStore();
        dataStore.saveWalletList(walletList);
    }

    @Override
    public void start(Stage stage) throws IOException {

        mainStage = stage;

        BankAccount bankAccount = new BankAccount();
        WalletList walletList = new WalletList();

        try {
            bankAccount = loadBankAccountFromFile();
        } catch (RetrieveDataException e) {
            WalletApp.showErrorDialog("Error on loading BankAccount data. Using new empty account!");
            e.printStackTrace();
        }

        try {
            walletList = loadWalletListFromFile();
        } catch (RetrieveDataException e) {
            WalletApp.showErrorDialog("Error on loading WalletList data. Using new empty WalletList!");
            e.printStackTrace();
        }

        //Fill GlobalContext
        GlobalContext.getGlobalContext().putStateFor(WalletApp.GLOBAL_WALLET_LIST, walletList); //gibt den aktuellen globalContext zurück oder erstellt einen neuen globalContext
        //wenn noch Keiner gesetzt wurde.
        //GlobalerContext = Globaler Speicher
        GlobalContext.getGlobalContext().putStateFor(WalletApp.GLOBAL_BANK_ACCOUNT, bankAccount);
        GlobalContext.getGlobalContext().putStateFor(WalletApp.GLOBAL_CURRENT_CURRENCY_PRICES, new CurrentCurrencyPrices()); //nimm bei Währungsumrechnungen das CurrentCurrencyPrices Objekt, welches bereitgestellt wurde.


        WalletApp.switchScene("main.fxml", "at.itkolleg.sample.main");
    }

    @Override
    public void stop()
    {
        WalletList walletList = (WalletList)GlobalContext.getGlobalContext().getStateFor(WalletApp.GLOBAL_WALLET_LIST);
        BankAccount bankAccount = (BankAccount) GlobalContext.getGlobalContext().getStateFor(WalletApp.GLOBAL_BANK_ACCOUNT);

        try {
            storeBankAccountToFile(bankAccount);
            System.out.println("BankAccount details stored to file!");
        } catch (SaveDataException e) {
            WalletApp.showErrorDialog("Could not store bankaccount details!");
            e.printStackTrace();
        }

        try {
            storeWalletListToFile(walletList);
            System.out.println("WalletList stored to file!");
        } catch (SaveDataException e) {
            WalletApp.showErrorDialog("Could not store wallet details!");
            e.printStackTrace();
        }
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

        try {
            dataStore.saveWalletList(walletList);
        } catch (SaveDataException e) {
            e.printStackTrace();
        }
        try {
            WalletList walletList2 = dataStore.retrieveWalletList();
            System.out.println(walletList2);
        } catch (RetrieveDataException e) {
            e.printStackTrace();
        }

        launch(args);
    }
}
