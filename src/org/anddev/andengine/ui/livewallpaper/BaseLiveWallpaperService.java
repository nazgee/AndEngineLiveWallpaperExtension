package org.anddev.andengine.ui.livewallpaper;

import net.rbgrn.opengl.GLWallpaperService;

import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.opengl.view.RenderSurfaceView;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.IGameInterface;

import android.app.WallpaperManager;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;

public abstract class BaseLiveWallpaperService extends GLWallpaperService implements IGameInterface {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private org.anddev.andengine.engine.Engine mEngine;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void onCreate() {
		super.onCreate();

		this.mEngine = this.onLoadEngine();
		this.applyEngineOptions(this.mEngine.getEngineOptions());

		this.onLoadResources();
		final Scene scene = this.onLoadScene();
		this.mEngine.onLoadComplete(scene);
		this.onLoadComplete();
		this.mEngine.start();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public org.anddev.andengine.engine.Engine getEngine() {
		return this.mEngine;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected void onPause(){
		this.mEngine.stop();
	}

	protected void onResume(){
		this.mEngine.start();
	}

	@Override
	public Engine onCreateEngine() {
		return new BaseWallpaperGLEngine();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void onTap(final int pX, final int pY) {

	}

	protected void onDrop(final int pX, final int pY) {

	}

	private void applyEngineOptions(final EngineOptions pEngineOptions) {

	}

	protected void enableAccelerometer(final IAccelerometerListener pAccelerometerListener) {
		this.mEngine.enableAccelerometer(this, pAccelerometerListener);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	protected class BaseWallpaperGLEngine extends GLEngine {
		// ===========================================================
		// Fields
		// ===========================================================

		private Renderer mRenderer;

		// ===========================================================
		// Constructors
		// ===========================================================

		public BaseWallpaperGLEngine() {
			this.mRenderer = new RenderSurfaceView.Renderer(BaseLiveWallpaperService.this.mEngine);
			this.setRenderer(this.mRenderer);
			this.setRenderMode(RENDERMODE_CONTINUOUSLY);
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public Bundle onCommand(String pAction, int pX, int pY, int pZ, Bundle pExtras, boolean pResultRequested) {
			if(pAction.equals(WallpaperManager.COMMAND_TAP)) {
				BaseLiveWallpaperService.this.onTap(pX, pY);
			} else if (pAction.equals(WallpaperManager.COMMAND_DROP)) {
				BaseLiveWallpaperService.this.onDrop(pX, pY);
			}

			return super.onCommand(pAction, pX, pY, pZ, pExtras, pResultRequested);
		}

		@Override
		public void onResume() {
			super.onResume();
			BaseLiveWallpaperService.this.getEngine().reloadTextures();
			BaseLiveWallpaperService.this.onResume();
		}

		@Override
		public void onPause() {
			super.onPause();
			BaseLiveWallpaperService.this.onPause();
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			if (this.mRenderer != null) {
				// mRenderer.release();
			}
			this.mRenderer = null;
		}
	}
}