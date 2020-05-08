package com.shouman.apps.hawk.ui.main.companyUI.navDrawer.home;

public interface OnSalesMemberLongClickActionsHandler {

    void onActionMove(String salesUID, String salesName, boolean status, String branchDetails);

    void onActionDisable(String salesUID, String branchDetails);

    void onActionEnable(String salesUID, String branchDetails);

    void onActionDelete(String salesUID, String branchDetails);
}
