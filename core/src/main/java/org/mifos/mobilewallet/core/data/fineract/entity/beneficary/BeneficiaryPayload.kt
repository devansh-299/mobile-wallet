package org.mifos.mobilewallet.core.data.fineract.entity.beneficary

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by dilpreet on 16/6/17.
 */
@Parcelize
data class BeneficiaryPayload(
        var locale: String = "en_GB",

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("accountNumber")
        var accountNumber: String? = null,

        @SerializedName("accountType")
        var accountType: Int? = null,

        @SerializedName("transferLimit")
        var transferLimit: Int? = null,

        @SerializedName("officeName")
        var officeName: String? = null) : Parcelable