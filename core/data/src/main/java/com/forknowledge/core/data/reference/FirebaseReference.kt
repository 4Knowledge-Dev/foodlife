package com.forknowledge.core.data.reference

object FirestoreReference {
    const val USER_COLLECTION = "User" // Root collection
    const val USER_RECORD_SUB_COLLECTION = "IntakeNutrition" // Sub collection
    const val USER_DOCUMENT_IS_NEW_USER_FIELD = "isNewUser"
    const val USER_DOCUMENT_EMAIL_FIELD = "email"
}

object FirebaseException {
    const val FIREBASE_EXCEPTION = "FirebaseException"
    const val FIREBASE_GET_DATA_EXCEPTION = "FirebaseDataException"
    const val FIREBASE_TRANSACTION_EXCEPTION = "FirebaseTransactionException"
}
