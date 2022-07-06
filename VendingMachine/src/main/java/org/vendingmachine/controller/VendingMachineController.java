package org.vendingmachine.controller;

import org.vendingmachine.dao.NoItemInventoryException;
import org.vendingmachine.dao.VendingMachinePersistenceException;
import org.vendingmachine.dto.Item;
import org.vendingmachine.servicelayer.*;
import org.vendingmachine.ui.VendingMachineView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VendingMachineController {
    private final VendingMachineView view;
    private final VendingMachineServiceLayer serviceLayer;

    public VendingMachineController(VendingMachineView view, VendingMachineServiceLayer serviceLayer) {
        this.view = view;
        this.serviceLayer = serviceLayer;
    }

    public void run(){
        boolean keepGoing = true;
        int menuSelection = 0;

        try {
            listItems();
            while (keepGoing) {

                menuSelection = getMainMenuSelection();

                switch (menuSelection) {
                    case 1:
                        addItem();
                        break;
                    case 2:
                        purchaseItem();
                        break;
                    case 3:
                        listItems();
                        break;
                    case 4:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                        break;
                }
            }
            exitMessage();
        } catch (VendingMachinePersistenceException | VendingMachineOutOfStockException | NoItemInventoryException |
                 VendingMachineDataValidationException | VendingMachineDuplicateNameException |
                 VendingMachineInsufficientFundsException e ) {
            view.displayError(e.getMessage());
        }
    }

    private void purchaseItem() throws VendingMachineInsufficientFundsException, VendingMachinePersistenceException, VendingMachineOutOfStockException {
        BigDecimal money = view.getInputFunds();
        String name = view.getItemChoice();

        Item item = serviceLayer.getItem(name,money);
        if (item == null) {
            view.displayError("Could not find " +name + ".");
        } else {
            try {
                BigDecimal price = item.getPrice();

                List<BigDecimal> change = new ArrayList<>();

                boolean stocked = serviceLayer.inStock(item);
                boolean enoughFunds = serviceLayer.enoughFunds(price, money);

                if (enoughFunds && stocked) {
                    change = serviceLayer.returnChange(money,price);
                    serviceLayer.reduceQuantity(item);
                    view.displayItem(item);
                    view.returnChangeInfo(change,price,money);
                }
            } catch (VendingMachineOutOfStockException | VendingMachineInsufficientFundsException e) {
                view.displayError(e.getMessage());
            }
        }
    }

    private void addItem() throws NoItemInventoryException, VendingMachinePersistenceException, VendingMachineDataValidationException, VendingMachineDuplicateNameException {
        view.displayCreateItemBanner();
        Item newItem = view.getNewItemInfo();
        serviceLayer.addItem(newItem);
        view.displayCreateSuccessBanner();
    }

    private void listItems() throws VendingMachinePersistenceException,VendingMachineOutOfStockException {
        view.displayAllItemsBanner();
        List<Item> items = serviceLayer.getAllItems();
        view.displayItems(items);
    }

    private void exitMessage() {
        view.displayExitBanner();
    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private int getMainMenuSelection() {
        return view.printMenuAndGetSelection();
    }
}
