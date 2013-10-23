/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package co.joyatwork.pwp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class ParticlesLivewallpaperSettings extends Activity {

	public static class SettingsFragment extends PreferenceFragment 
		//TODO not sure if i need this listener here
		implements SharedPreferences.OnSharedPreferenceChangeListener  {
	    
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        //TODO not sure if this ok here, should be in Activity.onCreate()?
	        getPreferenceManager().setSharedPreferencesName(
	        		ParticlesLiveWallpaper.PREFS_NAME);
	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.particles_lwp_preferences);
	        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(
	                this);

	    }

	    @Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
				String key) {
			// TODO Auto-generated method stub
			
		}

	    @Override
		public void onDestroy() {
	        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
	                this);
	        super.onDestroy();
	    }

	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

}