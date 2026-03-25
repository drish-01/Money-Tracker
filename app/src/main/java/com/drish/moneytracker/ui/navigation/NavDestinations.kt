package com.drish.moneytracker.ui.navigation

object NavDestinations {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val FORGOT_PASSWORD = "forgot_password"
    const val DASHBOARD = "dashboard"
    const val TRANSACTIONS = "transactions"
    const val PERSON_DETAIL = "person_detail/{personId}"
    const val ADD_TRANSACTION = "add_transaction"
    const val EDIT_TRANSACTION = "edit_transaction/{transactionId}"
    const val GROUPS = "groups"
    const val CREATE_GROUP = "create_group"
    const val GROUP_DETAIL = "group_detail/{groupId}"
    const val PROFILE = "profile"
    const val THEME_SETTINGS = "theme_settings"

    fun personDetail(personId: String) = "person_detail/$personId"
    fun editTransaction(transactionId: String) = "edit_transaction/$transactionId"
    fun groupDetail(groupId: String) = "group_detail/$groupId"
}
