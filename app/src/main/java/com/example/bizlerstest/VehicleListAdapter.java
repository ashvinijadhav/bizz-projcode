package com.example.bizlerstest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.MyViewHolder> {

        private ArrayList<DataVehicle> dataSet;

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView vNumber,vMake,vModel,vVariant,vFuelType;
            ImageView vPhoto;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.vNumber = (TextView) itemView.findViewById(R.id.list_vNo);
                this.vMake = (TextView) itemView.findViewById(R.id.list_vMake);
                this.vModel = (TextView) itemView.findViewById(R.id.list_vModel);
                this.vVariant = (TextView) itemView.findViewById(R.id.list_vVarient);
                this.vFuelType = (TextView) itemView.findViewById(R.id.list_vFuelType);
                this.vPhoto = (ImageView) itemView.findViewById(R.id.list_vPhoto);
            }
        }

        public VehicleListAdapter(ArrayList<DataVehicle> data) {
            this.dataSet = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cards_layout, parent, false);

            view.setOnClickListener(VehicleList.myOnClickListener);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
            TextView vNumber,vMake,vModel,vVariant,vFuelType;
            ImageView vPhoto;

            TextView textVNumber = holder.vNumber;
            TextView textVMake= holder.vMake;
            TextView textVModel = holder.vModel;
            TextView textVVariant= holder.vVariant;
            TextView textVFuelType = holder.vFuelType;
            ImageView imageVPhoto = holder.vPhoto;

            textVNumber.setText(dataSet.get(listPosition).getVehicleNumber());
            textVMake.setText(dataSet.get(listPosition).getVehicleMake());
            textVModel.setText(dataSet.get(listPosition).getVehicleModel());
            textVVariant.setText(dataSet.get(listPosition).getVehicleVariant());
            textVFuelType.setText(dataSet.get(listPosition).getVehicleFueltype());
            imageVPhoto.setImageResource(dataSet.get(listPosition).getVehiclePhoto());
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }


