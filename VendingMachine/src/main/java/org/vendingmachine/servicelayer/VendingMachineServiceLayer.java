package org.vendingmachine.servicelayer;

import org.vendingmachine.dao.NoItemInventoryException;
import org.vendingmachine.dao.VendingMachinePersistenceException;
import org.vendingmachine.dto.Item;

import java.math.BigDecimal;
import java.util.List;

public interface VendingMachineServiceLayer {
        void addItem(Item item) throws
                VendingMachinePersistenceException,
                VendingMachineDuplicateNameException,
                VendingMachineDataValidationException, NoItemInventoryException;

        List<Item> getAllItems() throws
                VendingMachinePersistenceException;

        Item getItem(String name, BigDecimal funds) throws
                VendingMachineInsufficientFundsException,
                VendingMachinePersistenceException, VendingMachineOutOfStockException;

        Item reduceItem(String name) throws
                VendingMachinePersistenceException;

        List<BigDecimal> returnChange(BigDecimal payment, BigDecimal price);

        boolean enoughFunds(BigDecimal price, BigDecimal funds) throws
                VendingMachineInsufficientFundsException;

        boolean inStock(Item item) throws
                VendingMachineOutOfStockException;

        Item reduceQuantity(Item item) throws
                VendingMachinePersistenceException;

}
