/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package ngoctdn.vng.gesture;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public class DragGestureDetector {

	protected OnGestureListener mListener;
	private static final String LOG_TAG = "CupcakeGestureDetector";
	float mLastTouchX;
	float mLastTouchY;
	final float mTouchSlop;
	final float mMinimumVelocity;

	private float mFirstRawTouchY;
	private float mLastRawTouchY;
	private int mActivePointerId;

	public void setOnGestureListener(OnGestureListener listener) {
		this.mListener = listener;
	}

	public DragGestureDetector(Context context) {
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		mTouchSlop = configuration.getScaledTouchSlop();
	}

	private VelocityTracker mVelocityTracker;
	private boolean mIsDragging;

	float getActiveX(MotionEvent ev) {
		return ev.getX();
	}

	float getActiveY(MotionEvent ev) {
		return ev.getY();
	}

	public boolean isScaling() {
		return false;
	}

	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			mVelocityTracker = VelocityTracker.obtain();
			if (null != mVelocityTracker) {
				mVelocityTracker.addMovement(ev);
			} else {
				Log.i(LOG_TAG, "Velocity tracker is null");
			}

			mLastTouchX = getActiveX(ev);
			mLastTouchY = getActiveY(ev);
			mFirstRawTouchY = ev.getRawY();
			mLastRawTouchY = ev.getRawY();
			mIsDragging = false;
			mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
			break;
		}
		
		case MotionEvent.ACTION_POINTER_DOWN:
			mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
			break;

		case MotionEvent.ACTION_MOVE: {
			final float x = getActiveX(ev);
			final float y = getActiveY(ev);
			final float dx = x - mLastTouchX, dy = y - mLastTouchY;

			if (!mIsDragging) {
				// Use Pythagoras to see if drag length is larger than
				// touch slop
				mIsDragging = (float)Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;
			}

			if (mIsDragging) {
				if (ev.getPointerCount() == 1)
					mListener.onDrag(mLastRawTouchY, ev.getRawY(), dx, dy);
				mLastTouchX = x;
				mLastTouchY = y;
				mLastRawTouchY = ev.getRawY();
				// if (mListener != null) {
				// mListener.onScrolling(mLastRawTouchY, ev.getRawY());
				// mLastRawTouchY = ev.getRawY();
				// }

				if (null != mVelocityTracker) {
					mVelocityTracker.addMovement(ev);
				}
			}
			if(mActivePointerId == -1 && mListener != null ){
				mListener.onRelease(mFirstRawTouchY, ev.getRawY(), 0);
			}
			break;
		}

//		case MotionEvent.ACTION_CANCEL: {
//			if (mListener != null) {
//				mLastRawTouchY = ev.getRawY();
//				mListener.onRelease(mFirstRawTouchY, ev.getRawY());
//			}
//			// Recycle Velocity Tracker
//			if (null != mVelocityTracker) {
//				mVelocityTracker.recycle();
//				mVelocityTracker = null;
//			}
//			break;
//		}
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP: {
			
			if (mIsDragging) {
				if (null != mVelocityTracker) {

					mLastTouchX = getActiveX(ev);
					mLastTouchY = getActiveY(ev);

					// if (mListener != null) {
					// mListener.onScrolling(mFirstTouchY, mLastTouchY);
					// }

					// Compute velocity within the last 1000ms
					mVelocityTracker.addMovement(ev);
					mVelocityTracker.computeCurrentVelocity(1000);

					final float vX = mVelocityTracker.getXVelocity(), vY = mVelocityTracker.getYVelocity();

					// If the velocity is greater than minVelocity, call
					// listener
					if (Math.max(Math.abs(vX), Math.abs(vY)) >= mMinimumVelocity) {
						mListener.onFling(mLastTouchX, mLastTouchY, -vX, -vY);
					}
				}
			}
			
			if (mListener != null) {
				mLastRawTouchY = ev.getRawY();
				if(mVelocityTracker != null)
					mListener.onRelease(mFirstRawTouchY, ev.getRawY(), mVelocityTracker.getYVelocity());
				else
					mListener.onRelease(mFirstRawTouchY, ev.getRawY(), 0);
			}

			// Recycle Velocity Tracker
			if (null != mVelocityTracker) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			break;
		}
		}

		return true;
	}
}
