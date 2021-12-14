package com.alidevs.traill.data.firebase

import com.alidevs.traill.data.model.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable

class FirebaseService private constructor() {
	
	private val auth = Firebase.auth
	
	fun register(user: UserModel) = Completable.create { emitter ->
		auth.createUserWithEmailAndPassword(user.email, user.password)
			.addOnCompleteListener { creationTask ->
				if (emitter.isDisposed) {
					return@addOnCompleteListener
				}
				
				if (!creationTask.isSuccessful) {
					emitter.onError(creationTask.exception!!)
					return@addOnCompleteListener
				}
				
				updateUserProfile(user)
					?.addOnCompleteListener updateProfileListener@ { updateTask ->
						if (!updateTask.isSuccessful) {
							emitter.onError(updateTask.exception!!)
							return@updateProfileListener
						}
						
						emitter.onComplete()
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
	
	private fun updateUserProfile(user: UserModel): Task<Void>? {
		val profileUpdates = UserProfileChangeRequest.Builder()
			.setDisplayName(user.fullname)
			.build()
		return auth.currentUser?.updateProfile(profileUpdates)
	}
	
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