package com.romanusynin.mobreg.mobreg.activities;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.romanusynin.mobreg.mobreg.adapters.*;
import com.romanusynin.mobreg.mobreg.*;
import com.romanusynin.mobreg.mobreg.objects.Department;
import com.romanusynin.mobreg.mobreg.objects.Hospital;
import com.romanusynin.mobreg.mobreg.objects.Parser;

import java.util.ArrayList;

public class DepartmentsActivity extends Activity {

    private Hospital hospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        Intent intent = getIntent();
        hospital = (Hospital) intent.getExtras().getSerializable("hospital");
        GetListDepartment task = new GetListDepartment();
        task.execute(hospital);
    }

    class GetListDepartment extends AsyncTask<Hospital,  ArrayList<Department>,  ArrayList<Department>> {

        @Override
        protected  ArrayList<Department> doInBackground(Hospital... params) {
            return Parser.getDepartmentsList(params[0]);
        }

        @Override
        protected void onPostExecute( ArrayList<Department> departments) {
            super.onPostExecute(departments);
            if (departments == null){
                setContentView(R.layout.error_net_layout);
            }
            else {
                setContentView(R.layout.departments_layout);
                TextView nameHospital = (TextView)findViewById(R.id.nameHospital);
                nameHospital.setText(hospital.getName());
                TextView addressHospital = (TextView)findViewById(R.id.addressHospital);
                addressHospital.setText(hospital.getAddress());

                Button callNumber = (Button) findViewById(R.id.phoneHospital);
                callNumber.setText("Тел. " + hospital.getNumberPhone());
                callNumber.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + hospital.getNumberPhone()));
                        startActivity(intent);
                    }
                });
                if (hospital.getNumberPhone() == null) {
                    callNumber.setVisibility(View.GONE);
                }
            }
            DepartmentAdapter adapter = new DepartmentAdapter(DepartmentsActivity.this, departments);
            ListView listView = (ListView) findViewById(R.id.lvDepartments);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> view, View v, int position,long id){

                        Department selectedDepartment = (Department) view.getItemAtPosition(position);
                        Intent intent = new Intent(DepartmentsActivity.this, DoctorsActivity.class);
                        intent.putExtra("department", selectedDepartment);
                        startActivity(intent);
                    }

                });
            listView.setAdapter(adapter);
            }
        }
}


