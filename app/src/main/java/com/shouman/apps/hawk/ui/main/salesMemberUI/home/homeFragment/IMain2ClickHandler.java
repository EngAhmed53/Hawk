package com.shouman.apps.hawk.ui.main.salesMemberUI.home.homeFragment;

import com.shouman.apps.hawk.ui.main.OnCustomerItemClickHandler;

public interface IMain2ClickHandler extends OnCustomerItemClickHandler {
    @Override
    void onCustomerItemClickHandler(String customerUID, String customerName);
}
