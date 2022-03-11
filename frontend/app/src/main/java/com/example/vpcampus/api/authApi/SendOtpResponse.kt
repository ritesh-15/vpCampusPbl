package com.example.vpcampus.api.authApi

import java.io.Serializable

data class SendOtpResponse(
    val ok:Boolean,
    val otp: OtpData
):Serializable