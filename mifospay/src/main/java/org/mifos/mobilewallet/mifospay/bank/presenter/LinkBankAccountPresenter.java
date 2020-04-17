package org.mifos.mobilewallet.mifospay.bank.presenter;

import org.mifos.mobilewallet.core.domain.model.BankAccountDetails;
import org.mifos.mobilewallet.mifospay.bank.BankContract;
import org.mifos.mobilewallet.mifospay.base.BaseView;
import org.mifos.mobilewallet.mifospay.data.local.PreferencesHelper;

import java.util.Random;

import javax.inject.Inject;

/**
 * Created by ankur on 09/July/2018
 */

public class LinkBankAccountPresenter implements BankContract.LinkBankAccountPresenter {

    private static final Random mRandom = new Random();
    private final PreferencesHelper mPreferencesHelper;
    BankContract.LinkBankAccountView mLinkBankAccountView;

    @Inject
    public LinkBankAccountPresenter(PreferencesHelper preferencesHelper) {
        this.mPreferencesHelper = preferencesHelper;
    }

    @Override
    public void attachView(BaseView baseView) {
        mLinkBankAccountView = (BankContract.LinkBankAccountView) baseView;
        mLinkBankAccountView.setPresenter(this);
    }

    @Override
    public void fetchBankAccountDetails(final String bankName) {
        // TODO:: UPI API implement
        mLinkBankAccountView.addBankAccount(
                new BankAccountDetails(bankName, "Ankur Sharma", "New Delhi",
                        mRandom.nextInt() + " ", "Savings"));
    }

    @Override
    public void getMobileNumber() {
        String mobileNumber = mPreferencesHelper.getMobile();
        mLinkBankAccountView.fetchMobileNumber(mobileNumber);
    }
}
