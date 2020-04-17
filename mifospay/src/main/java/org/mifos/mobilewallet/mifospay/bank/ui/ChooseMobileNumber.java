package org.mifos.mobilewallet.mifospay.bank.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ankur on 09/July/2018
 */

public class ChooseMobileNumber extends BottomSheetDialogFragment {

    @BindView(R.id.et_mobile_number)
    TextInputEditText editTextMobileNumber;

    private BottomSheetBehavior mBottomSheetBehavior;
    private String mobileNumber;


    public static ChooseMobileNumber newInstance(String mobileNumber) {
        ChooseMobileNumber fragment = new ChooseMobileNumber();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MOBILE_NUMBER, mobileNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.dialog_choose_mobile_number, null);
        dialog.setContentView(view);
        mBottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        if (getArguments() != null) {
            mobileNumber = getArguments().getString(Constants.MOBILE_NUMBER);
        }
        ButterKnife.bind(this, view);
        editTextMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mobileNumber = editable.toString();
            }
        });
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        editTextMobileNumber.setText(mobileNumber);
    }

    @OnClick(R.id.tv_change_mobile_number)
    public void changeMobileNumber() {
        editTextMobileNumber.setEnabled(true);
    }

    @OnClick(R.id.btn_confirm)
    public void onConfirmClicked() {
        dismiss();
        if (getActivity() instanceof LinkBankAccountActivity) {
            ((LinkBankAccountActivity) getActivity()).linkBankAccount(mobileNumber);
        }
    }
}
