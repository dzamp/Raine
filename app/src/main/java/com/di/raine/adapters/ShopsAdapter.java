package com.di.raine.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.di.raine.branches.Branch;
import com.di.raine.R;


import java.util.List;

/**
 * Created by di on 8/9/2016.
 */
public class ShopsAdapter extends BaseAdapter {
    private List<Branch> sShopsList;
    private LayoutInflater sInflater;
    private boolean sShowCheckbox;

    public ShopsAdapter(List<Branch> list, LayoutInflater inflater, boolean showCheckbox) {
        sShopsList = list;
        sInflater = inflater;
        sShowCheckbox = showCheckbox;
    }

    @Override
    public int getCount() {
        return sShopsList.size();
    }

    @Override
    public Object getItem(int position) {
        return sShopsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewShop shop;

        if (convertView == null) {
            convertView = sInflater.inflate(R.layout.shop,
                    null);
            shop = new ViewShop();

            shop.shopTitle = (TextView) convertView.findViewById(R.id.TextViewShop);

            shop.shopCheckbox = (CheckBox) convertView.findViewById(R.id.ShopCheckBoxSelected);

            convertView.setTag(shop);
        } else {
            shop = (ViewShop) convertView.getTag();
        }

        Branch curBranch = sShopsList.get(position);


        shop.shopTitle.setText(curBranch.name);

        if (!sShowCheckbox) {
            shop.shopCheckbox.setVisibility(View.GONE);
        } else {
            if (curBranch.selected == true)
                shop.shopCheckbox.setChecked(true);
            else
                shop.shopCheckbox.setChecked(false);
        }


        return convertView;
    }


    private class ViewShop {

        TextView shopTitle;
        CheckBox shopCheckbox;
    }
}
