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
        if (bankAccount != null) {
            ObjectOutputStream objectOutputStream = null;
            try {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream("account.bin"));
                objectOutputStream.writeObject(bankAccount);
                objectOutputStream.close(); //Filehandle zumachen
            } catch (IOException ioException) {
                ioException.printStackTrace();
                throw new SaveDataException("Error saving BankAccount to file: " + ioException.getMessage());
            }

        }
    }

    @Override
    public void saveWalletList(WalletList walletList) throws SaveDataException {

        if (walletList != null) {
            ObjectOutputStream objectOutputStream = null;
            try {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream("walletlist.bin"));
                objectOutputStream.writeObject(walletList);
                objectOutputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                throw new SaveDataException("Error saving WalletList to file: " + ioException.getMessage());
            }
        }
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
            throw new RetrieveDataException("Error on retrieving BankAccount data from File!" + ex.getMessage());
        }
    }

    @Override
    public WalletList retrieveWalletList() throws RetrieveDataException {
        /*Object o = objectInputStream.readObject();
            if (o instanceof WalletList)*/
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream("walletlist.bin"));
            WalletList walletList = (WalletList) objectInputStream.readObject();
            objectInputStream.close();
            return walletList;
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
            throw new RetrieveDataException("Error on retrieving WalletList data from file! " + exception.getMessage());
        }

    }
}
