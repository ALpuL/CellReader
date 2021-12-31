package dev.zwander.cellreader.layout

import android.annotation.SuppressLint
import android.telephony.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import dev.zwander.cellreader.R
import dev.zwander.cellreader.utils.*

@SuppressLint("MissingPermission")
@Composable
fun AdvancedSubInfo(telephony: TelephonyManager, subs: SubscriptionManager) {
    ProvideTextStyle(value = LocalTextStyle.current.copy(textAlign = TextAlign.Center)) {
        FlowRow(
            mainAxisSpacing = 16.dp,
            mainAxisAlignment = MainAxisAlignment.Center,
            mainAxisSize = SizeMode.Expand
        ) {
            with(telephony.signalStrength) {
                Text(
                    text = stringResource(id = R.string.signal_strength),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                FormatText(R.string.level_format, "$level")
                FormatText(R.string.timestamp_format, "$timestampMillis")
            }

            PaddedDivider(modifier = Modifier.fillMaxWidth())

            telephony.serviceState?.apply {
                Text(
                    text = stringResource(id = R.string.service_state),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                FormatText(
                    R.string.operator_format,
                    setOf(
                        operatorAlphaShort,
                        operatorAlpha,
                        operatorAlphaLong,
                        operatorAlphaShortRaw,
                        operatorAlphaLongRaw,
                        operatorNumeric,
                        dataOperatorAlphaShort,
                        dataOperatorNumeric,
                        voiceOperatorAlphaLong,
                        voiceOperatorAlphaShort,
                        voiceOperatorNumeric
                    ).joinToString("/")
                )

                FormatText(R.string.roaming_format, "${roaming}/${dataRoaming}/${voiceRoaming}")
                FormatText(
                    R.string.roaming_type_format,
                    "${ServiceState.roamingTypeToString(dataRoamingType)}/${
                        ServiceState.roamingTypeToString(
                            voiceRoamingType
                        )
                    }"
                )
                FormatText(R.string.data_roaming_from_reg_format, "$dataRoamingFromRegistration")

                FormatText(
                    R.string.state_format,
                    "${ServiceState.rilServiceStateToString(dataRegState)}/${
                        ServiceState.rilServiceStateToString(
                            voiceRegState
                        )
                    }"
                )
                FormatText(R.string.emergency_only_format, "$isEmergencyOnly")

                FormatText(
                    R.string.network_type_format,
                    "${
                        ServiceState.rilRadioTechnologyToString(
                            ServiceState.networkTypeToRilRadioTechnology(
                                voiceNetworkType
                            )
                        )
                    }/" + ServiceState.rilRadioTechnologyToString(
                        ServiceState.networkTypeToRilRadioTechnology(
                            dataNetworkType
                        )
                    )
                )

                FormatText(R.string.bandwidths_format, cellBandwidths.joinToString(", "))
                FormatText(R.string.duplex_format, duplexModeToString(duplexMode))
                FormatText(R.string.channel_format, "$channelNumber")

                FormatText(R.string.searching_format, "$isSearching")
                FormatText(R.string.manual_format, "$isManualSelection")

                FormatText(R.string.iwlan_preferred_format, "$isIwlanPreferred")
                FormatText(R.string.cssi_format, "$cssIndicator")

                cdmaSystemId.onNegAvail {
                    FormatText(R.string.cdma_system_id_format, "$cdmaSystemId")
                }
                cdmaNetworkId.onNegAvail {
                    FormatText(R.string.cdma_network_id_format, "$cdmaNetworkId")
                }
                cdmaRoamingIndicator.onNegAvail {
                    FormatText(
                        R.string.cdma_roaming_indicator_format,
                        "${cdmaRoamingIndicator}/${cdmaDefaultRoamingIndicator}"
                    )
                }
                cdmaEriIconMode.onNegAvail {
                    FormatText(R.string.cdma_eri_icon_format, "${cdmaEriIconMode}/${cdmaEriIconIndex}")
                }
                FormatText(R.string.arfcn_rsrp_boost_format, "$arfcnRsrpBoost")

                PaddedDivider(modifier = Modifier.fillMaxWidth())

                Text(
                    text = stringResource(id = R.string.network_registrations),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Card(
                    elevation = 8.dp
                ) {
                    FlowRow(
                        mainAxisSize = SizeMode.Expand,
                        mainAxisAlignment = MainAxisAlignment.Center,
                        mainAxisSpacing = 16.dp,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        networkRegistrationInfoList.forEach { networkRegistrationInfo ->
                            with(networkRegistrationInfo) {
                                this.dataSpecificInfo?.apply {
                                    FormatText(R.string.endc_available_format, "$isEnDcAvailable")
                                    FormatText(R.string.nr_available_format, "$isNrAvailable")
                                    FormatText(R.string.dcnr_restricted_format, "$isDcNrRestricted")

                                    vopsSupportInfo?.apply {
                                        FormatText(R.string.vops_supported_format, "$isVopsSupported")
                                        FormatText(
                                            R.string.vops_emergency_service_supported_format,
                                            "$isEmergencyServiceSupported"
                                        )
                                        FormatText(
                                            R.string.vops_emergency_service_fallback_supported_format,
                                            "$isEmergencyServiceFallbackSupported"
                                        )
                                    }
                                }

                                this.voiceSpecificInfo?.apply {
                                    FormatText(R.string.prl_format, "$systemIsInPrl")

                                    roamingIndicator.onNegAvail {
                                        FormatText(
                                            R.string.roaming_indicator_format,
                                            "$roamingIndicator/$defaultRoamingIndicator"
                                        )
                                    }

                                    FormatText(R.string.css_format, "$cssSupported")
                                }

                                FormatText(
                                    R.string.transport_format,
                                    AccessNetworkConstants.transportTypeToString(
                                        transportType
                                    )
                                )
                                FormatText(
                                    R.string.access_format,
                                    ServiceState.rilRadioTechnologyToString(
                                        ServiceState.networkTypeToRilRadioTechnology(
                                            accessNetworkTechnology
                                        )
                                    )
                                )

                                FormatText(R.string.registered_format, "$isRegistered")
                                FormatText(R.string.in_service_format, "$isInService")
                                FormatText(R.string.emergency_enabled_format, "$isEmergencyEnabled")
                                FormatText(R.string.searching_format, "$isSearching")
                                FormatText(
                                    R.string.roaming_format,
                                    "$isRoaming/${
                                        ServiceState.roamingTypeToString(
                                            roamingType
                                        )
                                    }"
                                )

                                FormatText(
                                    R.string.registration_state_format,
                                    NetworkRegistrationInfo.registrationStateToString(
                                        registrationState
                                    )
                                )
                                FormatText(R.string.reject_cause_format, "$rejectCause")

                                FormatText(R.string.rplmn_format, registeredPlmn)

                                FormatText(
                                    R.string.services_format,
                                    availableServices.joinToString(", ") {
                                        NetworkRegistrationInfo.serviceTypeToString(
                                            it
                                        )
                                    })
                                FormatText(R.string.domain_format, domainToString(domain))
                                FormatText(
                                    R.string.carrier_aggregation_format,
                                    isUsingCarrierAggregation.toString()
                                )
                                FormatText(
                                    R.string.nr_state_format,
                                    NetworkRegistrationInfo.nrStateToString(nrState).toString()
                                )

                                cellIdentity?.apply {
                                    CellIdentity(
                                        cellIdentity = this,
                                        simple = true,
                                        advanced = true
                                    )
                                }
                            }
                        }
                    }
                }
            }

            subs.allSubscriptionInfoList.find { it.subscriptionId == telephony.subscriptionId }
                ?.apply {
                    PaddedDivider(modifier = Modifier.fillMaxWidth())

                    Text(
                        text = stringResource(id = R.string.subscription_info),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    FlowRow(
                        mainAxisSpacing = 16.dp,
                        mainAxisAlignment = MainAxisAlignment.Center,
                        mainAxisSize = SizeMode.Expand,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        FormatText(R.string.sim_slot_format, "$simSlotIndex")
                        FormatText(R.string.number_format, number)
                        FormatText(R.string.display_name_format, "$displayName")
                        FormatText(R.string.carrier_name_format, "$carrierName")
                        FormatText(R.string.carrier_id_format, "$carrierId")
                        FormatText(R.string.subscription_type_format,
                            subscriptionTypeToString(subscriptionType)
                        )
                        FormatText(R.string.subscription_id_format, "$subscriptionId")
                        FormatText(R.string.profile_class_format,
                            profileClassToString(profileClass)
                        )
                        FormatText(R.string.name_source_format, nameSourceToString(nameSource))
                        FormatText(R.string.opportunistic_format, "$isOpportunistic")
                        FormatText(R.string.embedded_format, "$isEmbedded")
                        if (iccId.isNotBlank()) {
                            FormatText(R.string.icc_id_format, iccId)
                        }
                        if (hplmns.isNotEmpty()) {
                            FormatText(R.string.hplmns_format, hplmns.joinToString(", "))
                        }
                        if (ehplmns.isNotEmpty()) {
                            FormatText(R.string.ehplmns_format, ehplmns.joinToString(", "))
                        }
                        FormatText(R.string.group_disabled_format, "$isGroupDisabled")
                        this.groupUuid?.apply {
                            FormatText(R.string.group_uuid_format, "$this")
                        }
                        this.groupOwner?.apply {
                            FormatText(R.string.group_owner_format, this)
                        }
                        FormatText(R.string.country_iso_format, countryIso)
                        if (cardString.isNotBlank()) {
                            FormatText(R.string.card_string_format, cardString)
                        }
                        FormatText(R.string.card_id_format, "$cardId")
                        FormatText(R.string.data_roaming_from_reg_format, "$dataRoaming")
                        FormatText(R.string.access_rules_format, "$allAccessRules")
                        FormatText(R.string.mcc_mnc_format, "$mccString-$mncString")
                        FormatText(R.string.uicc_apps_format, "${areUiccApplicationsEnabled()}")
                    }
                }
        }
    }
}