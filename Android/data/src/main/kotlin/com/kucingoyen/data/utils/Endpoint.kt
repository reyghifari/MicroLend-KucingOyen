package com.kucingoyen.data.utils

object Endpoint {
    const val SIGN_IN = "auth/google/verify"

    const val DEPOSIT = "api/wallet/deposit"

    const val GET_BALANCE = "api/wallet/balance"

    const val TRANSFER = "api/wallet/transfer"

    const val LIST_LENDING = "api/lending/marketplace"

    const val CREATE_LOAN_REQUEST = "api/lending/request"

    const val LENDER_FILL_LOAN = "api/lending/request/fill"

    const val CANCEL_LOAN_REQUEST = "api/lending/loan/my-loans"

    const val CREATE_PROFILE = "api/lending/profile"

    const val MY_FUNDED = "api/lending/loan/funded"
}