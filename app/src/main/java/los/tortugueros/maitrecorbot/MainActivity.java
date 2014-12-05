package los.tortugueros.maitrecorbot;

import android.app.Activity;
import android.graphics.Picture;
import android.hardware.Camera;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;

//Suppress warning for Camera API deprecated. Camera2 is only available for Lollipop devices
@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements OnMessageReceived {

    private ListeningServer mListeningServer;
    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback pictureCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        pictureCallback = new PicCallback();
        setContentView(R.layout.activity_main);
        mCamera = Camera.open(1);
        FrameLayout layout = (FrameLayout) findViewById(R.id.camera_preview);
        CameraPreview mPreview = new CameraPreview(this,mCamera);
        layout.addView(mPreview);
        try {
            this.mListeningServer = ListeningServer.getInstance(this);
            if(!mListeningServer.isAlive())
                mListeningServer.start();
            else
                mListeningServer.updateCallback(this);
        } catch (Exception e) {
            Toast.makeText(this,"Couldn't start server",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCamera.release();
        mListeningServer.stopThread();
    }

    @Override
    public void onMessage(String message) {
        Log.d("OMG", "picture taken");
        mCamera.takePicture(null,null,pictureCallback);
    }

}
