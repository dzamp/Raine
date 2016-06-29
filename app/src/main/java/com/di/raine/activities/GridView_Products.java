package com.di.raine.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.di.raine.R;
import com.di.raine.services.NetworkService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GridView_Products extends AppCompatActivity {
    private NetworkService networkService;
    private boolean mBound;
    private Menu menu;
    private ArrayList<Integer> productIds = new ArrayList<Integer>(
            Arrays.asList(R.mipmap.smart_phone, R.mipmap.pc,
                    R.mipmap.monitors));
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NetworkService.NetworkBinder binder = (NetworkService.NetworkBinder) service;
            networkService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_products);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, productIds));


        // Set an setOnItemClickListener on the GridView
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Snackbar.make(v, "clicked  item " + position, Snackbar.LENGTH_SHORT).show();
            }
        });

    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.grip_product_menu, menu);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        getMenuInflater().inflate(R.menu.grip_product_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, NetworkService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            networkService.sendLogoutRequest();
            unbindService(mConnection);
            mBound = false;
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private static final int PADDING = 8;
        private static final int WIDTH = 200;
        private static final int HEIGHT = 200;
        private Context mContext;
        private List<Integer> mThumbIds;

        // Store the list of image IDs
        public ImageAdapter(Context c, List<Integer> ids) {
            mContext = c;
            this.mThumbIds = ids;
        }

        // Return the number of items in the Adapter
        @Override
        public int getCount() {
            return mThumbIds.size();
        }

        // Return the data item at position
        @Override
        public Object getItem(int position) {
            return mThumbIds.get(position);
        }

        // Will get called to provide the ID that
        // is passed to OnItemClickListener.onItemClick()
        @Override
        public long getItemId(int position) {
            return mThumbIds.get(position);
        }

        // Return an ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageView = (ImageView) convertView;

            // if convertView's not recycled, initialize some attributes
            if (imageView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(WIDTH, HEIGHT));
                imageView.setPadding(PADDING, PADDING, PADDING, PADDING);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            imageView.setImageResource(mThumbIds.get(position));
            return imageView;
        }
    }

}