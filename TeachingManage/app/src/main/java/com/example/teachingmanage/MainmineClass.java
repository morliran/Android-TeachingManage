package com.example.teachingmanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.teachingmanage.model.Classes;
import com.example.teachingmanage.mySpinners.ClassesSpinnerVO;

import java.util.ArrayList;
import java.util.List;

public class MainmineClass extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static String STRING_EMPTY = "";
    static Spinner sItems = null;
    private static int selectedClassId = 0;
    private static boolean isEdit = false;
    myDbAdapter myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmine_class);
        myHelper = new myDbAdapter(this);
        populateSpinner();

        Button addClass = (Button)findViewById(R.id.btnAdd);
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewClass();
            }
        });
        Button closeMe = (Button)findViewById(R.id.btnClose);
        closeMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainSystem.class);
                startActivity(i);
                finish();
            }
        });
    }
    private void addNewClass()
    {
        Classes classesDetails = new Classes();
        EditText classId = (EditText) findViewById(R.id.txtClassIdAdd);
        EditText className = (EditText) findViewById(R.id.txtClassNameAdd);
        if (!STRING_EMPTY.equals(classId.getText().toString()) && !STRING_EMPTY.equals(className.getText().toString()))
        {
            classesDetails.setId(Integer.parseInt(classId.getText().toString()));
            classesDetails.setClassesName(className.getText().toString());
            myHelper.addClasses(classesDetails);
            populateSpinner();
            classId.setText("");
            className.setText("");
        }
        else
            Message.message(this, getString(R.string.noticeMesseage, "One or more fields left empty!"));
    }
    private void populateSpinner()
    {
        List classesDetailsList = myHelper.getAllClasses();
        List classesSpinnerList = new ArrayList<>();
        for (int i = 0; i < classesDetailsList.size(); i++)
        {
            Classes Classes = (Classes)classesDetailsList.get(i);
            ClassesSpinnerVO classesSpinnerVO = new ClassesSpinnerVO(Classes.getId(), Classes.getClassesName());
            classesSpinnerList.add(classesSpinnerVO);
        }
        if(classesDetailsList.size() == 0)
        {
            LinearLayout displayArea = (LinearLayout)findViewById(R.id.displayArea);
            displayArea.setVisibility(LinearLayout.GONE);

            LinearLayout editButton = (LinearLayout)findViewById(R.id.editButton);
            editButton.setVisibility(LinearLayout.GONE);

            LinearLayout editArea = (LinearLayout)findViewById(R.id.editArea);
            editArea.setVisibility(LinearLayout.GONE);

            LinearLayout classesSpinnerLayout = (LinearLayout)findViewById(R.id.classSpinnerLayout);
            classesSpinnerLayout.setVisibility(LinearLayout.GONE);
        }
        else
        {
            LinearLayout classesSpinnerLayout = (LinearLayout)findViewById(R.id.classSpinnerLayout);
            classesSpinnerLayout.setVisibility(LinearLayout.VISIBLE);

        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, classesSpinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems = (Spinner)findViewById(R.id.classSpinner);
        sItems.setAdapter(adapter);
        sItems.setOnItemSelectedListener(this);
    }
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        ClassesSpinnerVO classesSpinnerVO = (ClassesSpinnerVO)sItems.getSelectedItem();
        int classesId = classesSpinnerVO.getClassId();
        selectedClassId = classesId;
        Classes ClassDetails = myHelper.getClasses(classesId);
        TextView txtClassIdDisplay = (TextView)findViewById(R.id.txtClassIdDisplay);
        TextView txtClassNameDisplay = (TextView)findViewById(R.id.txtClassNameDisplay);
        txtClassNameDisplay.setText(ClassDetails.getClassesName());

        String ClassId = Integer.toString(ClassDetails.getId());
        txtClassIdDisplay.setText(ClassId);

        LinearLayout displayArea = (LinearLayout)findViewById(R.id.displayArea);
        displayArea.setVisibility(LinearLayout.VISIBLE);

        LinearLayout editButton = (LinearLayout)findViewById(R.id.editButton);
        editButton.setVisibility(LinearLayout.VISIBLE);

        Button btnEdit = (Button)findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editClass();
            }
        });
        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteClass();
            }
        });
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void editClass()
    {
        Classes ClassDetails = null;
        EditText classId = (EditText)findViewById(R.id.txtClassIdEdit);
        EditText className = (EditText) findViewById(R.id.txtClassNameEdit);
        if(isEdit)
        {
            if (!STRING_EMPTY.equals(classId.getText().toString()) &&  !STRING_EMPTY.equals(className.getText().toString()))
            {
                ClassDetails = new Classes();
                LinearLayout displayArea = (LinearLayout) findViewById(R.id.displayArea);
                displayArea.setVisibility(LinearLayout.VISIBLE);
                LinearLayout editArea = (LinearLayout) findViewById(R.id.editArea);
                editArea.setVisibility(LinearLayout.GONE);
                Button btnDelete = (Button) findViewById(R.id.btnDelete);
                btnDelete.setVisibility(Button.VISIBLE);

                ClassDetails.setId(selectedClassId);
                ClassDetails.setClassesName(className.getText().toString());
                myHelper.updateClasses(ClassDetails);
                populateSpinner();
                sItems = (Spinner) findViewById(R.id.classSpinner);
                sItems.setSelection(getSpinnerPosition(sItems));
                isEdit = false;
            }
            else
            {
                Message.message(this, "One or more fields left empty!");
            }
        }
        else
        {
            ClassDetails = myHelper.getClasses(selectedClassId);
            LinearLayout displayArea = (LinearLayout)findViewById(R.id.displayArea);
            displayArea.setVisibility(LinearLayout.GONE);
            LinearLayout editArea = (LinearLayout)findViewById(R.id.editArea);
            editArea.setVisibility(LinearLayout.VISIBLE);
            Button btnDelete = (Button)findViewById(R.id.btnDelete);
            btnDelete.setVisibility(Button.INVISIBLE);
            classId.setText(Integer.toString(ClassDetails.getId()));
            className.setText(ClassDetails.getClassesName());
            isEdit = true;
        }
    }
    private int getSpinnerPosition(Spinner spinner)
    {
        Adapter adapter = spinner.getAdapter();
        int i=0;
        for (; i < adapter.getCount() ; i++)
        {
            ClassesSpinnerVO classesSpinnerVO = (ClassesSpinnerVO) adapter.getItem(i);
            if(selectedClassId == classesSpinnerVO.getClassId())
            {
                return i;
            }
        }
        return 0;
    }
    private void deleteClass()
    {
        myHelper.deleteClasses(selectedClassId);
        populateSpinner();
    }
}
