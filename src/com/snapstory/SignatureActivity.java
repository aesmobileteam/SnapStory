package com.snapstory;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.github.gcacace.signaturepad.views.SignaturePad;

public class SignatureActivity extends Activity {
    public final static String SIGNATURE_EXTRA = "signature_extra";
    private SignaturePad signaturePad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signature);

        signaturePad = (SignaturePad)findViewById(R.id.signature_pad);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                findViewById(R.id.signature_submit).setEnabled(true);
            }

            @Override
            public void onClear() {
                findViewById(R.id.signature_submit).setEnabled(false);
            }
        });

    }

    public void clear(View v) {
        signaturePad.clear();
    }

    public void submit(View v) {
       Bitmap signature = signaturePad.getSignatureBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signature.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        Intent intent = new Intent();
        intent.putExtra(SIGNATURE_EXTRA, b);
        setResult(RESULT_OK, intent);
        finish();
    }
    
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
    	
    	if((AppClass.spLogin.getAll().isEmpty()))
		{
			startActivity(new Intent(SignatureActivity.this,Login.class));
			finish();	
		}
    	
    	
    }
}
