package ui;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import Exceptions.InsufficientAmountException;
import Exceptions.InsufficientBalanceException;
import at.itkolleg.sample.WalletApp;
import domain.Wallet;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

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

        this.lblBankaccountBalance.textProperty().setValue(getBankAccount().getBalance().toString());
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
        System.out.println("OPEN WALLET");
    }

    public void newWallet()
    {
        System.out.println("NEW WALLET");
    }
}
