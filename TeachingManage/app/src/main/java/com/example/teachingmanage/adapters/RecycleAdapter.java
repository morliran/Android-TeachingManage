package com.example.teachingmanage.adapters;
import com.example.teachingmanage.*;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.teachingmanage.Message;
import com.example.teachingmanage.R;
import com.example.teachingmanage.model.Attendance;
import com.example.teachingmanage.model.Students;
import com.example.teachingmanage.myDbAdapter;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.Myholder> {
    List<Attendance> dataModelArrayList;
    private Bitmap bitmap;

    public RecycleAdapter(List<Attendance> dataModelArrayList) {
        this.dataModelArrayList = dataModelArrayList;
    }

    class Myholder extends RecyclerView.ViewHolder {
        TextView studentid, studentname, studentstatus;
        RadioButton radioPresent, radioLate, radioNotPresent;
        Button updater;
        ImageView myProfile;
        boolean flag = false;

        public Myholder(View itemView) {
            super(itemView);
            studentid = (TextView) itemView.findViewById(R.id.studentId1);
            studentname = (TextView) itemView.findViewById(R.id.studentName1);
            studentstatus = (TextView) itemView.findViewById(R.id.studentStatus1);
            myProfile = (ImageView)itemView.findViewById(R.id.profile_image);
            radioPresent = (RadioButton) itemView.findViewById(R.id.radioButton1);
            radioLate = (RadioButton) itemView.findViewById(R.id.radioButton2);
            radioNotPresent = (RadioButton) itemView.findViewById(R.id.radioButton3);
            radioPresent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = ((RadioButton) view).isChecked();
                    if (checked) {
                        studentstatus.setText(radioPresent.getText());
                        flag = true;
                    }
                }
            });
            radioLate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = ((RadioButton) view).isChecked();
                    if (checked) {
                        studentstatus.setText(radioLate.getText());
                        flag = true;
                    }
                }
            });
            radioNotPresent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = ((RadioButton) view).isChecked();
                    if (checked) {
                        studentstatus.setText(radioNotPresent.getText());
                        flag = true;
                    }
                }
            });
            updater = (Button) itemView.findViewById(R.id.btn_RecycleUpdate);
            updater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag) {
                        Attendance a = dataModelArrayList.get(getAdapterPosition());
                        a.setId(a.getId());
                        a.setAttendanceStudentId(a.getAttendanceStudentId());
                        a.setAttendanceStudentName(a.getAttendanceStudentName());
                        a.setAttendanceClassId(a.getAttendanceClassId());
                        a.setAttendanceCourseId(a.getAttendanceCourseId());
                        a.setAttendanceStatus(studentstatus.getText().toString());
                        myDbAdapter myHelper = new myDbAdapter(view.getContext());
                        myHelper.updateAttendance(a);
                        Message.message(view.getContext(), "Update successfully");
                    }
                }
            });
        }
    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview,null);
        return new Myholder(view);
    }
    @Override
    public void onBindViewHolder(final Myholder holder, int position)
    {
        Attendance dataModel = dataModelArrayList.get(position);
        holder.studentid.setText(String.valueOf(dataModel.getAttendanceStudentId()));
        holder.studentname.setText(dataModel.getAttendanceStudentName());
        holder.studentstatus.setText(dataModel.getAttendanceStatus());
        holder.myProfile.setImageResource(R.drawable.avatar);
        if(dataModel.getAttendanceStudentPic() == 1)
        {
            BitmapFactory.Options options = new BitmapFactory.Options();//To avoid byte allocation exception of big pictures
            options.inSampleSize = 8;
            bitmap = BitmapFactory.decodeFile(dataModel.getPictures().getPath() + "/" + dataModel.getPictures().getName(), options);
            holder.myProfile.setImageBitmap(bitmap);
        }
        else
            holder.myProfile.setImageResource(R.drawable.avatar);
    }
    @Override
    public int getItemCount()
    {
        return dataModelArrayList.size();
    }
}
