package com.meishimeike.View;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.meishimeike.ActLogin;
import com.meishimeike.R;

public class MainAttentionNoLoginView extends LinearLayout {
	private Context mContext;
	private Button btnToLogin = null;

	public MainAttentionNoLoginView(Context context) {
		super(context);
		mContext = context;

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.main_attention_no_login, this);

		btnToLogin = (Button) view.findViewById(R.id.btnToLogin);
		btnToLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, ActLogin.class);
				mContext.startActivity(intent);
			}
		});
	}

}
