package org.vendingmachine.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class VendingMachineAuditDaoFileImpl implements VendingMachineAuditDao{

    public static final String AUDIT_FILE = "audit.txt";

    @Override
    public void writeAuditEntry(String entry) throws VendingMachinePersistenceException {

        PrintWriter out;

        try{

            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));

        } catch (IOException e) {

            throw new VendingMachinePersistenceException(
                    "Could not write to audit file!"
            );

        }

        LocalDateTime timeStamp = LocalDateTime.now();
        out.println(timeStamp.toString() + ": " + entry);
        out.flush();

    }
}
