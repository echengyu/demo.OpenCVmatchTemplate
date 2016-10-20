package demo.OpenCVmatchTemplate;

import java.util.List;
import java.util.ListIterator;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.WindowManager;
import android.widget.ImageView;
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
	
	private ImageView imageView0, imageView1;
	private TextView textView0, textView1, textView2, textView3, textView4, textView5;
	
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
		setContentView(R.layout.picture_view);
		mOpenCvCameraView = (ScanTool) findViewById(R.id.picture_view0);
		mOpenCvCameraView.setCvCameraViewListener(this);
		
		imageView0 = (ImageView) findViewById(R.id.imageView0);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		textView0 = (TextView) findViewById(R.id.textView0);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		textView4 = (TextView) findViewById(R.id.textView4);
		textView5 = (TextView) findViewById(R.id.textView5);
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
		Mat src = inputFrame.rgba();	
		
		/*
		Mat template = new Mat();
		final Mat src = inputFrame.rgba();
	    int match_method = Imgproc.TM_SQDIFF;
		
		Bitmap bt1 = BitmapFactory.decodeResource(getResources(), R.drawable.facemin);
		Utils.bitmapToMat(bt1, template);
//		Imgproc.cvtColor(template, template, Imgproc.COLOR_BGR2RGBA);
		
		// Create the result matrix
	    int result_cols = src.cols() - template.cols() + 1;
	    int result_rows = src.rows() - template.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32F);
		
		// Do the Matching and Normalize
	    Imgproc.matchTemplate(src, template, result, match_method);
	    Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
	    
	    // Localizing the best match with minMaxLoc
	    MinMaxLocResult mmr = Core.minMaxLoc(result);

	    Point matchLoc;
	    if (match_method == Imgproc.TM_SQDIFF || match_method == Imgproc.TM_SQDIFF_NORMED) {
	        matchLoc = mmr.minLoc;
	    } else {
	        matchLoc = mmr.maxLoc;
	    }
	    
//	    Core.rectangle(src, matchLoc, matchLoc, new Scalar(255, 0, 0, 255), 2);
	    
	    Rect roi = new Rect((int) matchLoc.x, (int) matchLoc.y, template.cols(), template.rows());
	    Core.rectangle(src, new Point(roi.x, roi.y), new Point(roi.width - 2, roi.height - 2), new Scalar(255, 0, 0, 255), 2);
		
	    final Mat mTmp = new Mat();
	    template.copyTo(mTmp);
	    */
	    
		Thread t = new Thread() {
		    public void run() {
		        runOnUiThread(new Runnable() {
		            @Override
		            public void run() {
		            	
		            	/*
		            	textView0.setText(String.valueOf(m0));
		            	textView1.setText(String.valueOf(m1));
		            	textView2.setText(String.valueOf(m2));
		            	textView3.setText(String.valueOf(m3));
		            	textView4.setText(String.valueOf(m4));
		            	textView5.setText(String.valueOf(m5));
		            	*/
		            	
		            	/*
		            	Bitmap bt3 = Bitmap.createBitmap(mTmp.cols(), mTmp.rows(), Config.RGB_565);
		        		Utils.matToBitmap(mTmp, bt3);
		        		imageView0.setImageBitmap(bt3);
						*/
		            	
		            	imageView0.setImageResource(R.drawable.why);
		            	imageView1.setImageResource(R.drawable.why);

		            }
		        });
		    }
		};
		t.start();
		
		return src;
	}
}
