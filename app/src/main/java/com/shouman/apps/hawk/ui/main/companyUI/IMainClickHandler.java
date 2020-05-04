package com.shouman.apps.hawk.ui.main.companyUI;

import com.shouman.apps.hawk.ui.main.OnCustomerItemClickHandler;

public interface IMainClickHandler extends OnCustomerItemClickHandler {
    void onBranchItemClickHandler(String branchUID, String branchName);

    void onSalesItemClickHandler(String salesUID, String salesName);

    @Override
    void onCustomerItemClickHandler(String customerUID);


    //sales list item menu
    void onActionMove(String salesUID, String salesName, boolean status, String branchDetails);

    void onActionDisable(String salesUID, String branchDetails);

    void onActionEnable(String salesUID, String branchDetails);

    void onActionDelete(String salesUID, String branchDetails);
}
