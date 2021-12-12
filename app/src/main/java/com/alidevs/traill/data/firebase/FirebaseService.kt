package com.alidevs.traill.data.firebase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable

class FirebaseService private constructor() {

	private val auth = Firebase.auth

	fun register(email: String, password: String) = Completable.create { emitter ->
		auth.createUserWithEmailAndPassword(email, password)
			.addOnCompleteListener { task ->
				if (!emitter.isDisposed) {
					if (task.isSuccessful) {
						emitter.onComplete()
					} else {
						emitter.onError(task.exception!!)
					}
				}
			}
	}

	fun login(email: String, password: String) = Completable.create { emitter ->
		auth.signInWithEmailAndPassword(email, password)
			.addOnCompleteListener { task ->
				if (!emitter.isDisposed) {
					if (task.isSuccessful) {
						emitter.onComplete()
					} else {
						emitter.onError(task.exception!!)
					}
				}
			}
	}

	fun logout() = auth.signOut()

	fun getCurrentUser() = auth.currentUser

	companion object {
		private var instance: FirebaseService? = null

		fun getInstance(): FirebaseService {
			if (instance == null) {
				instance = FirebaseService()
			}
			return instance!!
		}
	}
}