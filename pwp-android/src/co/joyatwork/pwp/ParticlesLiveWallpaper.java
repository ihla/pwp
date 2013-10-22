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

import android.content.SharedPreferences;
import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;

public class ParticlesLiveWallpaper extends AndroidLiveWallpaperService {
	
	public static final String PREFS_NAME = "particles_settings";
	
	@Override
	public void onCreateApplication () {
		super.onCreateApplication();
		
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useAccelerometer = false;
        cfg.useCompass = false;
        cfg.useWakelock = false;
        cfg.getTouchEventsForLiveWallpaper = true;
		cfg.useGL20 = false;
		
		ApplicationListener listener = new MyLiveWallpaperListener();
		initialize(listener, cfg);
	}
	
	// implement AndroidWallpaperListener additionally to ApplicationListener 
	// if you want to receive callbacks specific to live wallpapers
	public class MyLiveWallpaperListener 
		extends ParticleEffectDemo 
		implements AndroidWallpaperListener, SharedPreferences.OnSharedPreferenceChangeListener {
		
        private SharedPreferences prefs;

		public MyLiveWallpaperListener() {
			super();
            prefs = ParticlesLiveWallpaper.this.getSharedPreferences(PREFS_NAME, 0);
            prefs.registerOnSharedPreferenceChangeListener(this);
            onSharedPreferenceChanged(prefs, null);

		}
		
		@Override
		public void offsetChange (float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset,
			int yPixelOffset) {
			Log.i("ParticlesLiveWallpaper test", "offsetChange(xOffset:"+xOffset+" yOffset:"+yOffset+" xOffsetSteep:"+xOffsetStep+" yOffsetStep:"+yOffsetStep+" xPixelOffset:"+xPixelOffset+" yPixelOffset:"+yPixelOffset+")");
		}

		@Override
		public void previewStateChange (boolean isPreview) {
			Log.i("ParticlesLiveWallpaper test", "previewStateChange(isPreview:"+isPreview+")");
		}

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences prefs, String key) {
			Log.i(PREFS_NAME, "onSharedPreferenceChanged" 
				+ " - value " 
					+ Integer.valueOf(prefs.getString("particles_type", "0")));
			int index = Integer.valueOf(prefs.getString("particles_type", "0"));
			setEmmitter(index);
			
		}
	}
}