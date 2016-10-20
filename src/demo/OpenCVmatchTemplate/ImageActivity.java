package demo.OpenCVmatchTemplate;

import java.util.List;
import java.util.ListIterator;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ImageActivity extends Activity implements CvCameraViewListener2 {
	private static final String TAG  = "OCVSample::Activity";
	private ScanTool mOpenCvCameraView;

	private boolean onCameraViewStarted = true;
	private List<android.hardware.Camera.Size> mResolutionList;
	private android.hardware.Camera.Size resolution = null;
	private SubMenu mResolutionMenu;
	private MenuItem[] mResolutionMenuItems;
	
	private double threshold1 = 60;
	private double threshold2 = 60;

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				Log.i(TAG, "OpenCV loaded successfully");
				mOpenCvCameraView.enableView();
			}
			break;
			default: {
				super.onManagerConnected(status);
			}
			break;
			}
		}
	};

	public ImageActivity() {
		Log.i(TAG, "Instantiated new " + this.getClass());
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "called onCreate");
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.image_manipulations_surface_view_4);
		mOpenCvCameraView = (ScanTool) findViewById(R.id.image_activity_view);
		mOpenCvCameraView.setCvCameraViewListener(this);
		
		TextView textViewName0 = (TextView)findViewById(R.id.textViewName0);
		textViewName0.setText("Threshold1");
		SeekBar seekBar0 = (SeekBar)findViewById(R.id.seekBar0);
		seekBar0.setMax(200);
		seekBar0.setProgress((int) threshold1);
		final TextView seekBarValue0 = (TextView)findViewById(R.id.textViewStatus0);
		seekBarValue0.setText(String.valueOf(threshold1));
		seekBar0.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
				// TODO Auto-generated method stub
				seekBarValue0.setText(String.valueOf(progress));
				threshold1 = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
		
		TextView textViewName1 = (TextView)findViewById(R.id.textViewName1);
		textViewName1.setText("Threshold2");
		SeekBar seekBar1 = (SeekBar)findViewById(R.id.seekBar1);
		seekBar1.setMax(200);
		seekBar1.setProgress((int) threshold2);
		final TextView seekBarValue1 = (TextView)findViewById(R.id.textViewStatus1);
		seekBarValue1.setText(String.valueOf(threshold2));
		seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
				// TODO Auto-generated method stub
				seekBarValue1.setText(String.valueOf(progress));
				threshold2 = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
		
//		SeekBar seekBar2 = (SeekBar)findViewById(R.id.seekBar2);
//		final TextView seekBarValue2 = (TextView)findViewById(R.id.textViewStatus2);
//		seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//			@Override
//			public void onProgressChanged(SeekBar seekBar, int progress,
//			boolean fromUser) {
//				// TODO Auto-generated method stub
//				seekBarValue2.setText(String.valueOf(progress));
//			}
//
//			@Override
//			public void onStartTrackingTouch(SeekBar seekBar) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void onStopTrackingTouch(SeekBar seekBar) {
//				// TODO Auto-generated method stub
//			}
//		});
//		
//		SeekBar seekBar3 = (SeekBar)findViewById(R.id.seekBar3);
//		final TextView seekBarValue3 = (TextView)findViewById(R.id.textViewStatus3);
//		seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//			@Override
//			public void onProgressChanged(SeekBar seekBar, int progress,
//			boolean fromUser) {
//				// TODO Auto-generated method stub
//				seekBarValue3.setText(String.valueOf(progress));
//			}
//
//			@Override
//			public void onStartTrackingTouch(SeekBar seekBar) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void onStopTrackingTouch(SeekBar seekBar) {
//				// TODO Auto-generated method stub
//			}
//		});
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	@Override
	public void onResume() {
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
	}

	public void onDestroy() {
		super.onDestroy();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	public void onCameraViewStarted(int width, int height) {
		if(onCameraViewStarted == true) {
			onCameraViewStarted = false;
			mResolutionList = mOpenCvCameraView.getResolutionList();
			for(int i=0; i<mResolutionList.size(); i++) {
				if(mResolutionList.get(i).width == 640) {
					resolution = mResolutionList.get(i);
					mOpenCvCameraView.setResolution(resolution);
					resolution = mOpenCvCameraView.getResolution();
					String caption = Integer.valueOf(resolution.width).toString() + "x" + Integer.valueOf(resolution.height).toString();
					Toast.makeText(this, caption, Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	public void onCameraViewStopped() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(TAG, "called onCreateOptionsMenu");

		mResolutionMenu = menu.addSubMenu("Resolution");
		mResolutionList = mOpenCvCameraView.getResolutionList();
		mResolutionMenuItems = new MenuItem[mResolutionList.size()];
		ListIterator<android.hardware.Camera.Size> resolutionItr = mResolutionList.listIterator();
		int idx = 0;
		while(resolutionItr.hasNext()) {
			android.hardware.Camera.Size element = resolutionItr.next();
			mResolutionMenuItems[idx] = mResolutionMenu.add(2, idx, Menu.NONE,
			                            Integer.valueOf(element.width).toString() + "x" + Integer.valueOf(element.height).toString());
			idx++;
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);

		if (item.getGroupId() == 2) {
			int id = item.getItemId();
			android.hardware.Camera.Size resolution = mResolutionList.get(id);
			mOpenCvCameraView.setResolution(resolution);
			resolution = mOpenCvCameraView.getResolution();
			String caption = Integer.valueOf(resolution.width).toString() + "x" + Integer.valueOf(resolution.height).toString();
			Toast.makeText(this, caption, Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		Mat rgba = inputFrame.rgba();
//		Imgproc.Canny(rgba, rgba, threshold1, threshold2);
		return rgba;
	}
}