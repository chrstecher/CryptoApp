package infrastruktur;

import Exceptions.RetrieveDataException;
import Exceptions.SaveDataException;
import at.itkolleg.sample.BankAccount;
import at.itkolleg.sample.DataStore;
import at.itkolleg.sample.WalletList;

import java.io.*;

public class FileDataStore implements DataStore {


    @Override
    public void saveBankAccount(BankAccount bankAccount) throws SaveDataException {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream("account.bin"));
            objectOutputStream.writeObject(bankAccount);
            objectOutputStream.close(); //Filehandle zumachen
        } catch (IOException ioException) {
            ioException.printStackTrace();
            throw new SaveDataException("Error saving BankAccount to File: " + ioException.getMessage());
        }

    }

    @Override
    public void saveWalletList(WalletList walletList) throws SaveDataException {

    }

    @Override
    public BankAccount retrieveBankAccount() throws RetrieveDataException {
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream("account.bin"));
            /*Object o = objectInputStream.readObject();
            if (o instanceof BankAccount)*/
            BankAccount bankAccount = (BankAccount) objectInputStream.readObject();
            objectInputStream.close();
            return bankAccount;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new RetrieveDataException("Error on retrieving BankAccount Data from File!" + ex.getMessage());
        }
    }

    @Override
    public WalletList retrieveWalletList() throws RetrieveDataException {
        return null;
    }
}
