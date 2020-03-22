package com.shouman.apps.hawk.ui.main.companyUi;

import com.shouman.apps.hawk.ui.main.OnCustomerItemClickHandler;

public interface IMainClickHandler extends OnCustomerItemClickHandler {
    void onBranchItemClickHandler(String branchUID, String branchName);
    void onSalesItemClickHandler(String salesUID, String salesName);
    @Override
    void onCustomerItemClickHandler(String customerUID, String customerName);
}
