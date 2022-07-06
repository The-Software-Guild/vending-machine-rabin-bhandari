package org.vendingmachine.ui;

import org.vendingmachine.dto.Item;

import java.math.BigDecimal;
import java.util.List;

public class VendingMachineView {

    private final UserIO io;

    public VendingMachineView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection() {
        // Display Items
        io.print("Main Menu");
        io.print("1. Add an Item");
        io.print("2. Purchase Item");
        io.print("3. List Item's");
        io.print("4. Exit");

        return io.readInt("Please select from the above choices.", 1, 4);
    }

    public Item getNewItemInfo() {
        String name = io.readString("Please enter item name.");
        BigDecimal price = io.readBigDecimal("Please enter the price per unit.");
        int quantity = io.readInt("Please enter " + name + "'s quantity.");

        Item item = new Item(name);
        item.setPrice(price);
        item.setQuantity(quantity);
        return item;
    }

    public void displayCreateItemBanner(){
        io.print("**********************CREATE ITEM**********************");
    }

    public void displayCreateSuccessBanner(){
        io.print("**********************SUCCESS*************************");
        io.readString("Please hit ENTER to proceed.");
    }

    public void displayItems(List<Item> items){
        for(Item i: items){
            String itemInfo = String.format("%s : costs $%s.",
                    i.getName(),
                    i.getPrice());
            io.print(itemInfo);
        }
    }

    public void displayAllItemsBanner(){
        io.print("**********************ALL ITEMS**********************");
    }

    public void showItemBanner(){
        io.print("**********************DISPLAY ITEM**********************");
    }

    public String getItemChoice(){
        return io.readString("Please enter item name to fetch.");
    }

    public BigDecimal getInputFunds() {
        return io.readBigDecimal("How much money would you like to input?");
    }

    public void displayItem(Item item){
        if (item != null){
            io.print( "Successfully purchased " + item.getName() + ".");
            io.print("Price: " + item.getPrice().toString() + ".");
        } else {
            io.print("No such item found.");
        }
    }

    public void removeItemBanner(){
        io.print("**********************REMOVE ITEM**********************");
    }

    public void removeItem(Item item){
        if (item != null){
            io.print("item successfully removed.");
        } else {
            io.print("No such item found.");
        }

        io.readString("Please hit ENTER to proceed.");
    }

    public void displayExitBanner(){
        io.print("Goodbye!");
    }

    public void displayUnknownCommandBanner(){
        io.print("Unknown command!");
    }

    public void displayError(String error){
        io.print("**********************ERROR************************");
        io.print(error);
    }

    public void returnChangeInfo(List<BigDecimal> change, BigDecimal price, BigDecimal money) {
        BigDecimal changeToGive = change.get(change.size()-1);

        io.print("$"+money.toString() + " - $" + price.toString());
        io.print("Change given: $"+String.valueOf(changeToGive));
        io.print("-----------------------------");
        io.print(change.get(0) + " Quarters");
        io.print(change.get(1) + " Dimes");
        io.print(change.get(2) + " Nickels");
        io.print(change.get(3) + " Pennies");
        io.print("-----------------------------");
    }
}
