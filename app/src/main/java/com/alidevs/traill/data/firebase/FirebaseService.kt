package com.alidevs.traill.data.firebase

import com.alidevs.traill.data.model.UserModel
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.CompletableEmitter

class FirebaseService private constructor() {
	
	private val auth = Firebase.auth
	private val firestore = Firebase.firestore
	
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
				
				updateUserProfile(user, emitter)
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
	
	private fun updateUserProfile(
		user: UserModel,
		emitter: CompletableEmitter
	) {
		val currentUser = auth.currentUser!!
		val profileUpdates = UserProfileChangeRequest.Builder()
			.setDisplayName(user.fullname)
			.build()
		
		firestore
			.collection("users")
			.document(currentUser.uid)
			.set(user)
			.addOnCompleteListener { setTask ->
				if (setTask.isSuccessful) {
					auth.currentUser
						?.updateProfile(profileUpdates)
						?.addOnCompleteListener { updateProfileTask ->
							if (updateProfileTask.isSuccessful) {
								emitter.onComplete()
							} else {
								emitter.onError(updateProfileTask.exception!!)
							}
						}
				} else {
					emitter.onError(setTask.exception!!)
				}
			}
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