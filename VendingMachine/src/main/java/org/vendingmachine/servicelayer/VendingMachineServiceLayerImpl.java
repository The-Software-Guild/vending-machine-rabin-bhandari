package org.vendingmachine.servicelayer;

import org.vendingmachine.dao.NoItemInventoryException;
import org.vendingmachine.dao.VendingMachineAuditDao;
import org.vendingmachine.dao.VendingMachineDao;
import org.vendingmachine.dao.VendingMachinePersistenceException;
import org.vendingmachine.dto.Coin;
import org.vendingmachine.dto.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.vendingmachine.dto.Coin.*;

public class VendingMachineServiceLayerImpl implements VendingMachineServiceLayer{

    private VendingMachineDao dao;
    private VendingMachineAuditDao auditDao;

    public VendingMachineServiceLayerImpl(VendingMachineDao dao, VendingMachineAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
    }

    private void validateItemData(Item item) throws VendingMachineDataValidationException {
        if (item.getName() == null
                || item.getName().trim().length() == 0
                || item.getName().equals("")
                || item.getPrice() == null
                || item.getPrice().compareTo(new BigDecimal("0")) == 0
                || item.getQuantity() == 0) {
            throw new VendingMachineDataValidationException (
                    "ERROR: No fields can be set to 0/null"
            );
        }

    }

    @Override
    public void addItem(Item item) throws VendingMachinePersistenceException, VendingMachineDuplicateNameException, VendingMachineDataValidationException, NoItemInventoryException {
        if (dao.getItem(item.getName()) !=null) {
            throw new VendingMachineDuplicateNameException(
                    "ERROR: Item already exists!"
            );
        }
        validateItemData(item);
        dao.addItem(item.getName(),item);
        auditDao.writeAuditEntry("ITEM ADDED: "+item.getName());

    }

    @Override
    public List<Item> getAllItems() throws VendingMachinePersistenceException {
        try {
            List<Item> items = dao.getAllItems();

            List<Item> itemsInStock = items.stream()
                    .filter((i) -> i.getQuantity() > 0)
                    .collect(Collectors.toList());


            return itemsInStock;
        } catch (VendingMachinePersistenceException e) {
            throw new VendingMachinePersistenceException("Can't retrieve any items.");
        }
    }

    @Override
    public Item getItem(String name, BigDecimal funds) throws VendingMachineInsufficientFundsException, VendingMachinePersistenceException, VendingMachineOutOfStockException {
        Item item = null;

        try {

            item = dao.getItem(name);

            if (item == null) {

                return null;

            }

            this.enoughFunds(item.getPrice(), funds);
            this.inStock(item);

        } catch (VendingMachinePersistenceException e) {
            throw new VendingMachinePersistenceException("No such item.");
        } catch (VendingMachineInsufficientFundsException e) {
            throw new VendingMachineInsufficientFundsException("Insufficient funds!");
        } catch (VendingMachineOutOfStockException e) {
            throw new VendingMachineOutOfStockException(item.getName() + " is out of stock!");
        }


        return item;
    }

    @Override
    public Item reduceItem(String name) throws VendingMachinePersistenceException {
        Item removedItem = dao.reduceItem(name);

        if (removedItem == null) {

            auditDao.writeAuditEntry("NO SUCH ITEM");
            return null;

        } else {

            auditDao.writeAuditEntry("ITEM REMOVED: " + removedItem.getName());

        }

        return removedItem;
    }

    @Override
    public List<BigDecimal> returnChange(BigDecimal money, BigDecimal price) {
        List<BigDecimal> changeInfo = new ArrayList<>();
        BigDecimal change = money.subtract(price);
        BigDecimal temp = change;
        BigDecimal zero = new BigDecimal("0");

        int quarters = 0;
        int dimes = 0;
        int nickels =0;
        int pennies = 0;

        while (temp.compareTo(zero)!=0) {
            if (temp.compareTo(QUARTER.getValue())!=-1) {
                quarters++;
                temp = temp.subtract(QUARTER.getValue());
            } else if (temp.compareTo(DIME.getValue())!=-1) {
                dimes++;
                temp = temp.subtract(DIME.getValue());
            } else if (temp.compareTo(NICKEL.getValue())!=-1) {
                nickels++;
                temp = temp.subtract(NICKEL.getValue());
            } else {
                pennies++;
                temp = temp.subtract(PENNY.getValue());
            }
        }

        changeInfo.add(BigDecimal.valueOf(quarters));
        changeInfo.add(BigDecimal.valueOf(dimes));
        changeInfo.add(BigDecimal.valueOf(nickels));
        changeInfo.add(BigDecimal.valueOf(pennies));
        changeInfo.add(change);

        return changeInfo;
    }

    @Override
    public boolean enoughFunds(BigDecimal price, BigDecimal funds) throws VendingMachineInsufficientFundsException {
        if (price.compareTo(funds) <0) {
            return true;
        } else {
            throw new VendingMachineInsufficientFundsException("Insufficient funds!");
        }
    }

    @Override
    public boolean inStock(Item item) throws VendingMachineOutOfStockException {
        if(item.getQuantity() < 1) {
            throw new VendingMachineOutOfStockException(item.getName() + " is out of stock!");
        }

        return true;
    }

    @Override
    public Item reduceQuantity(Item item) throws VendingMachinePersistenceException {
        item = dao.reduceItem(item.getName());
        return item;
    }
}
