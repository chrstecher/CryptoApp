package ui;

import Exceptions.InsufficientBalanceException;
import Exceptions.InvalidFeeException;
import at.itkolleg.sample.WalletApp;
import domain.CryptoCurrency;
import domain.Wallet;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.util.Optional;

public class MainController extends BaseControllerState {

    @FXML
    private Button btnClose;

    @FXML
    private ComboBox cmbWalletCurrency;

    @FXML
    private Label lblBankaccountBalance;

    @FXML
    private TableView<Wallet> tableView;

    public void initialize() {

        this.cmbWalletCurrency.getItems().addAll(CryptoCurrency.getCodes());
        this.lblBankaccountBalance.textProperty().setValue(getBankAccount().getBalance().toString());

        //Spaltendefinition
        TableColumn<Wallet, String> symbol = new TableColumn<>("SYMBOL");
        //Festlegung des Wertes
        symbol.setCellValueFactory(new PropertyValueFactory<>("cryptoCurrency"));
        //Holt den Wert aus der Getter-Methode (Aufruf der get-Methode)

        //Spaltendefinition
        TableColumn<Wallet, String> currencyName = new TableColumn<>("CURRENCY NAME");
        //Festlegung des Wertes
        currencyName.setCellValueFactory(new PropertyValueFactory<>("currencyName"));
        //Holt den Wert aus der Getter-Methode (Aufruf der get-Methode)

        //Spaltendefinition
        TableColumn<Wallet, String> name = new TableColumn<>("WALLET NAME");
        //Festlegung des Wertes
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        //Holt den Wert aus der Getter-Methode (Aufruf der get-Methode)

        //Spaltendefinition
        TableColumn<Wallet, String> amount = new TableColumn<>("AMOUNT");
        //Festlegung des Wertes
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        //Holt den Wert aus der Getter-Methode (Aufruf der get-Methode)

        tableView.getColumns().clear(); //Löschen aller Spalten
        tableView.getColumns().add(name);
        tableView.getColumns().add(symbol);
        tableView.getColumns().add(currencyName);
        tableView.getColumns().add(amount);

        tableView.getItems().setAll(getWalletList().getWalletsAsObservableList());

    }

    public void deposit()
    {
        TextInputDialog dialog = new TextInputDialog("Insert amount to deposit ...");
        dialog.setTitle("Deposit to bankaccount");
        dialog.setHeaderText("How much money do you want to deposit?");
        dialog.setContentText("Amount: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                BigDecimal amount = new BigDecimal(result.get());
                this.getBankAccount().deposit(amount);
                this.lblBankaccountBalance.textProperty().set(this.getBankAccount().getBalance().toString());
            }
            catch (NumberFormatException numberFormatException) {
                WalletApp.showErrorDialog("Please insert a number!");
            }
        }

    }

    public void withdraw()
    {
        TextInputDialog dialog = new TextInputDialog("Insert amount to withdraw ...");
        dialog.setTitle("Withdraw from bankaccount");
        dialog.setHeaderText("How much money do you want to withdraw?");
        dialog.setContentText("Amount: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                BigDecimal amount = new BigDecimal(result.get());
                this.getBankAccount().withdraw(amount);
                this.lblBankaccountBalance.textProperty().set(this.getBankAccount().getBalance().toString());
            }
            catch (NumberFormatException numberFormatException) {
                WalletApp.showErrorDialog("Please insert a number!");
            } catch (InsufficientBalanceException insufficientBalanceException)
            {
                WalletApp.showErrorDialog(insufficientBalanceException.getMessage());
            }
        }

    }

    public void openWallet()
    {
        Wallet wallet = this.tableView.getSelectionModel().getSelectedItem();
        if (wallet != null)
        {
            GlobalContext.getGlobalContext().putStateFor(WalletApp.GLOBAL_SELECTED_WALLET, wallet);
            WalletApp.switchScene("wallet.fxml", "at.itkolleg.sample.wallet");
        } else
        {
            WalletApp.showErrorDialog("You have to select a wallet first!");
        }
    }

    public void newWallet() throws InvalidFeeException {
        Object selectedItem = this.cmbWalletCurrency.getSelectionModel().getSelectedItem();
        if (selectedItem == null)
        {
            WalletApp.showErrorDialog("Choose currency!");
            return;
        }
        CryptoCurrency selectedCryptoCurrency = CryptoCurrency.valueOf(this.cmbWalletCurrency.getSelectionModel().getSelectedItem().toString());
        //String wird aus der Combobox geholt und von CryptoCurrency (Enum) mit valueOf auf Basis dieses Strings die Cryptocurrency generieren.
        this.getWalletList().addWallet(new Wallet(selectedCryptoCurrency.currencyName, selectedCryptoCurrency, new BigDecimal("1")));
        tableView.getItems().setAll(this.getWalletList().getWalletsAsObservableList());
    }
}
