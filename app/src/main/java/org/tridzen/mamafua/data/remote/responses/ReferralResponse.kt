package org.tridzen.mamafua.data.remote.responses

import org.tridzen.mamafua.data.local.entities.Referral

data class ReferralResponse(val message: String, val referrals: Referral)