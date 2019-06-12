package com.example.qrvalentin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    boolean shouldAddModelRond = true;
    boolean shouldAddModelCube = true;
    boolean shouldAddModelTriangle = true;
    private Activiy_Decode activiy_decode;
    private Button button;
    private AnchorNode anchorNode;
    private boolean isModeled = false;


    ArFragment arFragment;
    Node node;
    private ModelRenderable mon_modele;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String qrText = new String();
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
         
            qrText = extras.getString("qrTxt");
        }

        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityDecode();
            }
        });


        arFragment = (ArFragment)getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);
        //arFragment.getPlaneDiscoveryController().hide();
        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);

            //
        // if(qrText.equals("cube")) {
                ModelRenderable.builder()
                        .setSource(this, Uri.parse("cube1.sfb"))
                        .build()
                        .thenAccept(renderable -> mon_modele = renderable);


    }



    private void onUpdateFrame(FrameTime frameTime) {


        if(isModeled==true){
            return;
        }
        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<Plane> planes = frame.getUpdatedTrackables(Plane.class);
        for (Plane plane : planes){
            if(plane.getTrackingState() == TrackingState.TRACKING){
                // Anchor anchor = plane.createAnchor((plane.getCenterPose()));

                if (arFragment.getArSceneView().getArFrame() == null) {
                    return;
                }

                // If ARCore is not tracking yet, then don't process anything.
                if (arFragment.getArSceneView().getArFrame().getCamera().getTrackingState() != TrackingState.TRACKING ) {
                    return;
                }
                // Session session = arFragment.getArSceneView().getSession();

                // float[] pos = { 0,0, };
                //float[] rotation = {0,0,0,1};


                    Anchor anchor = plane.createAnchor(plane.getCenterPose());

                    anchorNode = new AnchorNode(anchor);
                    anchorNode.setRenderable(mon_modele);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    isModeled = true;


                    break;

            }
        }






    /*
        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<AugmentedImage> augmentedImages = frame.getUpdatedTrackables(AugmentedImage.class);
        for (AugmentedImage augmentedImage : augmentedImages) {
            if (augmentedImage.getTrackingState() == TrackingState.TRACKING) {
                if (activiy_decode.getQrText()=="cube") {
                    placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("cube1.sfb"));

                    Node node2 = new Node();
                    node2.setParent(node);
                    node2.setLocalPosition(new Vector3(1,0,0));
                    node2.setRenderable(mon_modele);



                    shouldAddModelCube = false;
                }
                else {

                    if (activiy_decode.getQrText()=="Pierre Ambs")
                    {
                        placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("rond1.sfb"));


                        shouldAddModelRond = false;
                    }
                    else
                        {
                        if (activiy_decode.getQrText()=="Annexe Sanitaire")
                        {
                            placeObject(arFragment, augmentedImage.createAnchor(augmentedImage.getCenterPose()), Uri.parse("triangle1.sfb"));


                            shouldAddModelTriangle = false;
                        }
                    }
                }
            }
        }
    */

    }



    public void openActivityDecode(){
        Intent intent = new Intent(this,Activiy_Decode.class);
        startActivity(intent);
    }
    public String getQrText(){
        return "";
    }

}
