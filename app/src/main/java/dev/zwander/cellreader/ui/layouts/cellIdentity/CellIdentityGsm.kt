package dev.zwander.cellreader.ui.layouts.cellIdentity

import android.os.Build
import android.telephony.CellIdentityGsm
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.zwander.cellreader.R
import dev.zwander.cellreader.utils.ARFCNTools
import dev.zwander.cellreader.utils.FormatText
import dev.zwander.cellreader.utils.asMccMnc
import dev.zwander.cellreader.utils.onAvail

@Composable
fun CellIdentityGsm.CellIdentityGsm(
    simple: Boolean,
    advanced: Boolean
) {
    val arfcnInfo = remember(arfcn) {
        ARFCNTools.gsmArfcnToInfo(arfcn)
    }

    if (simple) {
        val bands = remember(arfcn) { arfcnInfo.map { it.band } }

        if (bands.isNotEmpty()) {
            FormatText(
                textId = R.string.bands_format,
                bands.joinToString(", ")
            )
        }
    }

    if (advanced) {
        lac.onAvail {
            FormatText(R.string.lac_format, "$it")
        }
        cid.onAvail {
            FormatText(R.string.cid_format, "$it")
        }
        bsic.onAvail {
            FormatText(R.string.bsic_format, "$bsic")
        }
        arfcn.onAvail {
            FormatText(R.string.arfcn_format, "$arfcn")
        }
        mobileNetworkOperator?.apply {
            if (isNotBlank()) {
                FormatText(R.string.operator_format, this)
            }
        }

        val dlFreqs = remember(arfcn) { arfcnInfo.map { it.dlFreq } }
        val ulFreqs = remember(arfcn) { arfcnInfo.map { it.ulFreq } }

        if (dlFreqs.isNotEmpty()) {
            FormatText(
                textId = R.string.dl_freqs_format,
                dlFreqs.joinToString(", ")
            )
        }

        if (ulFreqs.isNotEmpty()) {
            FormatText(
                textId = R.string.ul_freqs_format,
                ulFreqs.joinToString(", ")
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!additionalPlmns.isNullOrEmpty()) {
                FormatText(
                    R.string.additional_plmns_format,
                    additionalPlmns.joinToString(", ") { it.asMccMnc }
                )
            }
        }
    }
}