package org.vendingmachine.servicelayer;

public class VendingMachineDataValidationException extends Exception {
    public VendingMachineDataValidationException(String message) {
        super(message);
    }

    public VendingMachineDataValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
