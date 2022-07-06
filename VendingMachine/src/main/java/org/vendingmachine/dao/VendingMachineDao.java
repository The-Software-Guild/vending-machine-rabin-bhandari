package org.vendingmachine.dao;

import org.vendingmachine.dto.Item;

import java.util.List;

public interface VendingMachineDao {
    Item addItem(String name, Item item) throws VendingMachinePersistenceException, NoItemInventoryException;

    List<Item> getAllItems() throws VendingMachinePersistenceException;

    Item getItem(String name) throws VendingMachinePersistenceException;

    Item reduceItem(String name) throws VendingMachinePersistenceException;

}
