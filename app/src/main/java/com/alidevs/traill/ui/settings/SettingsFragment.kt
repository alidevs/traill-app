package com.alidevs.traill.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.alidevs.traill.R
import com.alidevs.traill.ui.auth.AuthViewModel
import com.yariksoffice.lingver.Lingver
import io.reactivex.disposables.CompositeDisposable

class SettingsFragment : PreferenceFragmentCompat() {
	
	private val viewModel: AuthViewModel by viewModels()
	private val disposables = CompositeDisposable()
	
	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.root_preferences, rootKey)
		
		// Language switcher preference
		with(findPreference<ListPreference>("languages_listPreference")!!) {
			setSummaryProvider {
				Lingver.getInstance().getLocale().displayName
			}
			
			setOnPreferenceChangeListener { _, newValue ->
				val language = newValue as String
				val languageCode = language.substring(0, 2).lowercase()
				Lingver.getInstance().setLocale(requireContext(), languageCode)
				activity?.recreate()
				true
			}
		}
		
		// Display name preference
		with(findPreference<EditTextPreference>("displayName")!!) {
			setSummaryProvider {
				viewModel.user?.displayName
			}
			
			setOnPreferenceChangeListener { _, newValue ->
				viewModel.updateUserDisplayName(newValue as String)
					.subscribe({
						Toast.makeText(
							activity,
							"Updated display name: $newValue",
							Toast.LENGTH_SHORT
						)
							.show()
						activity?.recreate()
					}, {
						Toast.makeText(
							activity,
							"Failed to update display name: ${it.message}",
							Toast.LENGTH_SHORT
						)
							.show()
					}).also { disposables.add(it) }
				
				true
			}
		}
		
		// Email address preference (read-only)
		with(findPreference<EditTextPreference>("email")!!) {
			setSummaryProvider {
				viewModel.user?.email
			}
		}
		
		// Phone number preference
		with(findPreference<EditTextPreference>("phoneNumber")!!) {
			setSummaryProvider {
				viewModel.user?.phoneNumber
			}
			
			setOnPreferenceChangeListener { _, newValue ->
				
				true
			}
		}
		
	}
	
}