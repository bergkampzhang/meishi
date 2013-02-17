
package com.meishimeike.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meishimeike.ActPerson;
import com.meishimeike.ActTa;
import com.meishimeike.R;
import com.meishimeike.Bean.BeanUserInfo;
import com.meishimeike.Bll.BllSend;
import com.meishimeike.Utils.ImageDownloadThread;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadCallback;
import com.meishimeike.Utils.ImageDownloadThread.ImageDownloadItem;
import com.meishimeike.Utils.LocalVariable;

import java.util.ArrayList;

/**
 * @author :LiuQiang
 * @version 锛�012-6-18 class desc
 */
public class DarenAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<BeanUserInfo> arrList = null;
    private LayoutInflater mInflater;
    private String strUrl = "";
    private Bitmap bitmap = null;
    private ViewHolder viewHolder = null;
    private BeanUserInfo bean = null;
    private String strUId = "";
    private AlertDialog dlgProgress = null;
    private String strMessage;
    private Thread comitThread = null;
    private BllSend bllSend = null;
    private Button btn = null;
    private LocalVariable lv = null;
    private View convertView = null;

    public DarenAdapter(Context context, ArrayList<BeanUserInfo> arr) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        arrList = arr;
        bllSend = new BllSend(mContext);
        lv = new LocalVariable(mContext);
    }

    @Override
    public int getCount() {
        if (arrList == null) {
            return 0;
        } else {
            return arrList.size();
        }
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int pos, View cView, ViewGroup arg2) {
        convertView = cView;
        bean = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.daren_item, null);
            viewHolder = new ViewHolder();
            viewHolder.setTxtName((TextView) convertView
                    .findViewById(R.id.txtName));
            viewHolder.setBtnAttention((Button) convertView
                    .findViewById(R.id.btnAttention));
            viewHolder.setImgHead((ImageView) convertView
                    .findViewById(R.id.imgHead));
            viewHolder.setTxtLevel((TextView) convertView
                    .findViewById(R.id.txtLevel));
            viewHolder.setTxtSexy((TextView) convertView.findViewById(R.id.txtSexy));
            viewHolder.setTxtDesc((TextView) convertView.findViewById(R.id.txtDesc));
            viewHolder.setImg1((ImageView) convertView
                    .findViewById(R.id.imgFood1));
            viewHolder.setImg2((ImageView) convertView
                    .findViewById(R.id.imgFood2));
            viewHolder.setImg3((ImageView) convertView
                    .findViewById(R.id.imgFood3));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bean = arrList.get(pos);
        viewHolder.getTxtName().setText(bean.getName());
        if (bean.getUid().equals(lv.getUid())) {	
            viewHolder.getBtnAttention().setVisibility(View.INVISIBLE);
        } else {
            viewHolder.getBtnAttention().setVisibility(View.VISIBLE);
        }
        if (lv.getIsLogin()) {
            viewHolder.getBtnAttention().setText(bean.isAttention() ? "宸插叧娉�" : "鍏虫敞");
        } else {
            viewHolder.getBtnAttention().setText("鍏虫敞");
        }

        strUrl = bean.getPhoto_180_url();
        if (!"".equals(strUrl)) {
            viewHolder.getImgHead().setTag(strUrl);
            ImageDownloadItem item = new ImageDownloadItem();
            item.imageUrl = strUrl;
            item.callback = new ImageDownloadCallback() {
                @Override
                public void update(Bitmap bitmap, String imageUrl) {
                    ImageView imageViewByTag = (ImageView) convertView
                            .findViewWithTag(imageUrl);
                    if (imageViewByTag != null) {
                        imageViewByTag.setImageBitmap(bitmap);
                        notifyDataSetChanged();
                    }
                    bitmap = null;
                }
            };
            ImageDownloadThread imageDownloadThread = ImageDownloadThread
                    .getInstance();
            bitmap = imageDownloadThread.downloadWithCache(item);
            if (bitmap != null) {// 浠庣紦瀛樹腑鍙栧埌
                viewHolder.getImgHead().setImageBitmap(bitmap);
            }
            bitmap = null;
        }
        /* zhangpeng
        viewHolder.getImgHead().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String strUid = arrList.get(pos).getUid();
                Intent intent = new Intent();
                if (strUid.equals(lv.getUid())) {
                    intent.setClass(mContext, ActPerson.class);
                } else {
                    intent.setClass(mContext, ActTa.class);
                    intent.putExtra(ActTa.KEY_UID, strUid);
                }
                mContext.startActivity(intent);
            }
        );*/

        viewHolder.getBtnAttention().setTag(bean);
        viewHolder.getBtnAttention().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (lv.getIsLogin()) {
                    btn = (Button) v;
                    bean = (BeanUserInfo) btn.getTag();
                    if (bean.isAttention()) {
                        Toast.makeText(mContext, "璇ョ敤鎴峰凡鍏虫敞", Toast.LENGTH_SHORT)
                                .show();
                        btn.setText("宸插叧娉");
                    } else {
                        strUId = bean.getUid();
                        send();
                    }
                } else {
                    Toast.makeText(mContext,"浜瞺浣犺繕娌℃湁鐧诲綍鍝�", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String str = bean.getGender().equals("F") ? "<font color='#c83214'>濂�/font> "
                : "<font color='#50A7D3'>鐢�/font> ";
        str += " <font color='#000000'>" + bean.getCity() + "</font>";
        viewHolder.getTxtLevel().setText(bean.getIdentity());
        viewHolder.getTxtDesc().setText(bean.getIntro());
        viewHolder.getTxtSexy().setText(Html.fromHtml(str));
        strUrl = bean.getPhoto1();
        if (!"".equals(strUrl)) {
            viewHolder.getImg1().setTag(strUrl);
            ImageDownloadItem item = new ImageDownloadItem();
            item.imageUrl = strUrl;
            item.callback = new ImageDownloadCallback() {
                @Override
                public void update(Bitmap bitmap, String imageUrl) {
                    ImageView imageViewByTag = (ImageView) convertView
                            .findViewWithTag(imageUrl);
                    if (imageViewByTag != null) {
                        imageViewByTag.setImageBitmap(bitmap);
                        notifyDataSetChanged();
                    }
                    bitmap = null;
                }
            };
            ImageDownloadThread imageDownloadThread = ImageDownloadThread
                    .getInstance();
            bitmap = imageDownloadThread.downloadWithCache(item);
            if (bitmap != null) {// 浠庣紦瀛樹腑鍙栧埌
                viewHolder.getImg1().setImageBitmap(bitmap);
            }
            bitmap = null;
        }
        strUrl = bean.getPhoto2();
        if (!"".equals(strUrl)) {
            viewHolder.getImg2().setTag(strUrl);
            ImageDownloadItem item = new ImageDownloadItem();
            item.imageUrl = strUrl;
            item.callback = new ImageDownloadCallback() {
                @Override
                public void update(Bitmap bitmap, String imageUrl) {
                    ImageView imageViewByTag = (ImageView) convertView
                            .findViewWithTag(imageUrl);
                    if (imageViewByTag != null) {
                        imageViewByTag.setImageBitmap(bitmap);
                        notifyDataSetChanged();
                    }
                    bitmap = null;
                }
            };
            ImageDownloadThread imageDownloadThread = ImageDownloadThread
                    .getInstance();
            bitmap = imageDownloadThread.downloadWithCache(item);
            if (bitmap != null) {// 浠庣紦瀛樹腑鍙栧埌
                viewHolder.getImg2().setImageBitmap(bitmap);
            }
            bitmap = null;
        }
        strUrl = bean.getPhoto3();
        if (!"".equals(strUrl)) {
            viewHolder.getImg3().setTag(strUrl);
            ImageDownloadItem item = new ImageDownloadItem();
            item.imageUrl = strUrl;
            item.callback = new ImageDownloadCallback() {
                @Override
                public void update(Bitmap bitmap, String imageUrl) {
                    ImageView imageViewByTag = (ImageView) convertView
                            .findViewWithTag(imageUrl);
                    if (imageViewByTag != null) {
                        imageViewByTag.setImageBitmap(bitmap);
                        notifyDataSetChanged();
                    }
                    bitmap = null;
                }
            };
            ImageDownloadThread imageDownloadThread = ImageDownloadThread
                    .getInstance();
            bitmap = imageDownloadThread.downloadWithCache(item);
            if (bitmap != null) {// 浠庣紦瀛樹腑鍙栧埌
                viewHolder.getImg3().setImageBitmap(bitmap);
            }
            bitmap = null;
        }

        return convertView;
    }

    final static class ViewHolder {
        TextView txtName = null;
        TextView txtLevel = null;
        Button btnAttention = null;
        ImageView imgHead = null;
        ImageView img1, img2, img3;
        TextView txtSexy;
        TextView txtDesc;

        public TextView getTxtLevel() {
            return txtLevel;
        }

        public void setTxtLevel(TextView txtLevel) {
            this.txtLevel = txtLevel;
        }

        public ImageView getImgHead() {
            return imgHead;
        }

        public void setImgHead(ImageView imgHead) {
            this.imgHead = imgHead;
        }

        public TextView getTxtName() {
            return txtName;
        }

        public void setTxtName(TextView txtName) {
            this.txtName = txtName;
        }

        public Button getBtnAttention() {
            return btnAttention;
        }

        public void setBtnAttention(Button btnAttention) {
            this.btnAttention = btnAttention;
        }

        public ImageView getImg1() {
            return img1;
        }

        public void setImg1(ImageView img1) {
            this.img1 = img1;
        }

        public ImageView getImg2() {
            return img2;
        }

        public void setImg2(ImageView img2) {
            this.img2 = img2;
        }

        public ImageView getImg3() {
            return img3;
        }

        public void setImg3(ImageView img3) {
            this.img3 = img3;
        }

        public TextView getTxtSexy() {
            return txtSexy;
        }

        public void setTxtSexy(TextView txtSexy) {
            this.txtSexy = txtSexy;
        }

        public TextView getTxtDesc() {
            return txtDesc;
        }

        public void setTxtDesc(TextView txtDesc) {
            this.txtDesc = txtDesc;
        }

    }

    private void send() {
        dlgProgress = ProgressDialog.show(mContext, "璇风瓑寰�..", "姝ｅ湪杩涜鎿嶄綔.....",
                true);
        dlgProgress.show();
        comitThread = new Thread() {
            public void run() {
                try {
                    if (dataCheck()) {
                        handler.sendEmptyMessage(1);
                    }
                } catch (Exception ex) {
                    strMessage = ex.getLocalizedMessage();
                    handler.sendEmptyMessage(2);
                }
            };
        };
        comitThread.start();
    }

    private boolean dataCheck() {
        boolean b = false;
        String str = "";
        str = bllSend.Attention(strUId);
        if (str.split("#")[0].equals("1")) {
            strMessage = str.split("#")[1];
            b = true;
        } else {
            strMessage = str.split("#")[1];
            handler.sendEmptyMessage(2);
        }
        return b;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(mContext, strMessage, Toast.LENGTH_LONG).show();
                    if (dlgProgress != null && dlgProgress.isShowing()) {
                        dlgProgress.dismiss();
                    }
                    bean.setAttention(true);
                    btn.setText("宸插叧娉�");
                    break;
                case 2:
                    if (dlgProgress != null && dlgProgress.isShowing()) {
                        dlgProgress.dismiss();
                    }
                    Toast.makeText(mContext, strMessage, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

}
