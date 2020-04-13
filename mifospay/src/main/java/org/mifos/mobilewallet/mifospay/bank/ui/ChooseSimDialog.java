package org.mifos.mobilewallet.mifospay.bank.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.utils.SimCardUtil;
import org.mifos.mobilewallet.mifospay.utils.Toaster;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ankur on 09/July/2018
 */

public class ChooseSimDialog extends BottomSheetDialogFragment {

    @BindView(R.id.tv_sim1)
    TextView mTvSim1;
    @BindView(R.id.tv_sim2)
    TextView mTvSim2;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.ll_sim1)
    LinearLayout linearLayoutSim1;
    @BindView(R.id.ll_sim2)
    LinearLayout linearLayoutSim2;
    @BindView(R.id.tv_or)
    TextView tvOr;
    @BindView(R.id.tv_sim1_name)
    TextView tvSim1Details;
    @BindView(R.id.tv_sim2_name)
    TextView tvSim2Details;

    private static final int REQUEST_READ_PHONE_STATE = 100;
    private BottomSheetBehavior mBottomSheetBehavior;
    private int selectedSim = 0;
    private int numberOfSimCards = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.dialog_choose_sim_dialog, null);

        dialog.setContentView(view);
        mBottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());

        ButterKnife.bind(this, view);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkPermission();
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void getSimCardInfo() {
        numberOfSimCards = SimCardUtil.getSimCardNumber(getContext());
        if (numberOfSimCards == 1) {
            linearLayoutSim2.setVisibility(View.GONE);
            tvOr.setVisibility(View.GONE);
            tvSim1Details.setText(SimCardUtil.getNetworkOperatorName(getContext(), 0));
        } else {
            tvSim1Details.setText(SimCardUtil.getNetworkOperatorName(getContext(), 0));
            tvSim2Details.setText(SimCardUtil.getNetworkOperatorName(getContext(), 1));
        }
    }

    @OnClick({R.id.tv_sim1, R.id.tv_sim2})
    public void onSimSelected(View view) {
        switch (view.getId()) {
            case R.id.tv_sim1:
                selectedSim = 1;
                mTvSim1.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable(R.drawable.sim_card_selected), null, null);
                mTvSim2.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable(R.drawable.sim_card_unselected), null, null);
                Toaster.showToast(getActivity(), getString(R.string.sim_1_selected));
                break;
            case R.id.tv_sim2:
                selectedSim = 2;
                mTvSim2.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable(R.drawable.sim_card_selected), null, null);
                mTvSim1.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable(R.drawable.sim_card_unselected), null, null);
                Toaster.showToast(getActivity(), getString(R.string.sim_2_selected));
                break;
        }
    }

    @OnClick(R.id.btn_confirm)
    public void onConfirmClicked() {
        if (selectedSim != 0) {
            dismiss();
            if (getActivity() instanceof LinkBankAccountActivity) {
                ((LinkBankAccountActivity) getActivity()).linkBankAccount(selectedSim);
            }
        } else {
            Toaster.showToast(getActivity(), getString(R.string.no_sim_selected));
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        } else {
            // Permission already granted
            getSimCardInfo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    getSimCardInfo();
                } else {
                    // permission denied
                }
                return;
            }
        }
    }
}
