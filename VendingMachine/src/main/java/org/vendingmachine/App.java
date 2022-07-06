package org.vendingmachine;

import org.vendingmachine.controller.VendingMachineController;
import org.vendingmachine.dao.VendingMachineAuditDao;
import org.vendingmachine.dao.VendingMachineAuditDaoFileImpl;
import org.vendingmachine.dao.VendingMachineDao;
import org.vendingmachine.dao.VendingMachineDaoFileImpl;
import org.vendingmachine.servicelayer.VendingMachineServiceLayer;
import org.vendingmachine.servicelayer.VendingMachineServiceLayerImpl;
import org.vendingmachine.ui.UserIO;
import org.vendingmachine.ui.UserIOConsoleImpl;
import org.vendingmachine.ui.VendingMachineView;

public class App {
    public static void main(String[] args) {
        UserIO myIo = new UserIOConsoleImpl();
        VendingMachineView myView = new VendingMachineView(myIo);
        VendingMachineDao myDao = new VendingMachineDaoFileImpl();
        VendingMachineAuditDao myAuditDao = new VendingMachineAuditDaoFileImpl();
        VendingMachineServiceLayer service = new VendingMachineServiceLayerImpl(myDao, myAuditDao);
        VendingMachineController controller = new VendingMachineController(myView, service);
        controller.run();
    }
}