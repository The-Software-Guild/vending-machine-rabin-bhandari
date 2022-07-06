package org.vendingmachine.servicelayer;

public class VendingMachineDuplicateNameException extends Exception {
    public VendingMachineDuplicateNameException(String message) {
        super(message);
    }

    public VendingMachineDuplicateNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
