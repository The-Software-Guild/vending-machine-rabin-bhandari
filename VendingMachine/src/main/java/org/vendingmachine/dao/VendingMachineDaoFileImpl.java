package org.vendingmachine.dao;

import org.vendingmachine.dto.Item;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class VendingMachineDaoFileImpl implements VendingMachineDao{

    public final String VENDINGMACHINE_FILE;
    public static final String DELIMETER = "::";
    private Map<String, Item> items = new HashMap<>();

    public VendingMachineDaoFileImpl(){
        this.VENDINGMACHINE_FILE = "vendingMachine.txt";
    }

    public VendingMachineDaoFileImpl(String fileName) {
        this.VENDINGMACHINE_FILE = fileName;
    }


    @Override
    public Item addItem(String name, Item item) throws VendingMachinePersistenceException {
        loadVendingMachine();
        Item newItem = items.put(name, item);
        writeVendingMachine();
        return newItem;
    }

    @Override
    public List<Item> getAllItems() throws VendingMachinePersistenceException {
        loadVendingMachine();
        return new ArrayList(items.values());
    }

    @Override
    public Item getItem(String name) throws VendingMachinePersistenceException {
        loadVendingMachine();
        return items.get(name);
    }

    @Override
    public Item reduceItem(String name) throws VendingMachinePersistenceException {
        loadVendingMachine();
        Item item = items.remove(name);
        item.setQuantity(item.getQuantity()-1);
        items.put(name, item);
        writeVendingMachine();

        return item;
    }

    private void loadVendingMachine() throws VendingMachinePersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(VENDINGMACHINE_FILE)));
        } catch (FileNotFoundException e) {
            throw new VendingMachinePersistenceException(
                    "-_- Could not load data into memory.", e);
        }

        String currentLine;
        Item currentItem;

        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currentItem = unmarshallItem(currentLine);
            items.put(currentItem.getName(),currentItem);
        }
        scanner.close();
    }

    private Item unmarshallItem(String itemAsText){
        String[] itemTokens = itemAsText.split(DELIMETER);

        String name = itemTokens[0];

        Item item = new Item(name);
        item.setPrice(new BigDecimal(itemTokens[1]));
        item.setQuantity(Integer.parseInt(itemTokens[2]));

        return item;
    }

    private void writeVendingMachine() throws VendingMachinePersistenceException {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(VENDINGMACHINE_FILE));
        } catch (IOException e) {
            throw new VendingMachinePersistenceException("Could not save item Data",e);
        }

        String itemAsText;
        List<Item> itemList = this.getAllItems();
        for (Item currentItem : itemList) {
            itemAsText = marshallItem(currentItem);
            out.println(itemAsText);
            out.flush();
        }
        out.close();
    }

    private String marshallItem(Item item) {
        String itemAsText = item.getName()+ DELIMETER;
        itemAsText += item.getPrice()+DELIMETER;
        itemAsText += item.getQuantity()+DELIMETER;
        return itemAsText;
    }

}
