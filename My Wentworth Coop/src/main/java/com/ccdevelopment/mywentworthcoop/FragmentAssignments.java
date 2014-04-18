package com.ccdevelopment.mywentworthcoop;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentAssignments extends Fragment {

    EditText mAssignmentInput;
    ListView mListView;
    AssignmentAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            // onCreate First Time
        } else {
            // onCreate Subsequent Time
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_assignments, container, false);

        ParseObject.registerSubclass(Assignment.class);

        mAssignmentInput = (EditText) rootView.findViewById(R.id.assignment_input);
        mListView = (ListView) rootView.findViewById(R.id.assignment_list);
        mAdapter = new AssignmentAdapter(getActivity(), new ArrayList<Assignment>());
        mListView.setAdapter(mAdapter);

        Button submitAssignment = (Button) rootView.findViewById(R.id.submit_assignment_button);
        submitAssignment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createAssignment();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Assignment assignment = mAdapter.getItem(position);
                TextView assignmentDescription = (TextView) view.findViewById(R.id.assignment_description);
                TextView assignmentAuthor = (TextView) view.findViewById(R.id.assignment_author);
                TextView assignmentDueDate = (TextView) view.findViewById(R.id.assignment_duedate);

                assignment.setCompleted(!assignment.isCompleted());

                if (assignment.isCompleted()) {
                    assignmentDescription.setPaintFlags(assignmentDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    assignmentDescription.setPaintFlags(assignmentDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    assignmentDescription.setTypeface(null, Typeface.NORMAL);
                    assignmentDueDate.setTypeface(null, Typeface.NORMAL);
                    assignmentAuthor.setTypeface(null, Typeface.NORMAL);
                    assignmentDescription.setTextColor(Color.GRAY);
                    assignmentDueDate.setTextColor(Color.GRAY);
                    assignmentAuthor.setTextColor(Color.GRAY);
                } else {
                    assignmentDescription.setPaintFlags(assignmentDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    assignmentDescription.setTypeface(null, Typeface.BOLD);
                    assignmentDueDate.setTypeface(null, Typeface.BOLD);
                    assignmentAuthor.setTypeface(null, Typeface.BOLD);
                    assignmentDescription.setTextColor(Color.BLACK);
                    assignmentDueDate.setTextColor(Color.BLACK);
                    assignmentAuthor.setTextColor(Color.BLACK);
                }

                assignment.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e != null) {
                            Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {
                final Assignment assignment = mAdapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Warning");
                builder.setMessage("Are you sure you want to delete this?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        assignment.deleteInBackground(new DeleteCallback() {
                            public void done(ParseException e) {
                                if (e == null)
                                    mAdapter.remove(assignment);
                                else
                                    Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { // Do nothing, User Cancelled Delete
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });

        updateData();
        return rootView;
    }

    public void createAssignment() {
        if (mAssignmentInput.getText().length() > 0) {
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener,
                    mYear, mMonth, mDay);
            dialog.show();
        }
    }
    public void saveAssignment(Date utilDate) {
        if (mAssignmentInput.getText().length() > 0){
            final Assignment assignment = new Assignment();

            assignment.setDescription(mAssignmentInput.getText().toString());
            assignment.setUsername(ParseUser.getCurrentUser().getUsername());
            assignment.setDueDate(utilDate);
            assignment.setCompleted(false);
            assignment.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        mAssignmentInput.setText("");
                        mAdapter.insert(assignment, 0);
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void updateData(){
        ParseQuery<Assignment> publicQuery = ParseQuery.getQuery(Assignment.class);
        ParseQuery<Assignment> privateQuery = ParseQuery.getQuery(Assignment.class);

        privateQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        publicQuery.whereEqualTo("isPublic", true);

        List<ParseQuery<Assignment>> queries = new ArrayList<ParseQuery<Assignment>>();
        queries.add(publicQuery);
        queries.add(privateQuery);
        ParseQuery<Assignment> mainQuery = ParseQuery.or(queries);
        mainQuery.orderByDescending("dueDate");

        mainQuery.findInBackground(new FindCallback<Assignment>() {
            @Override
            public void done(List<Assignment> assignments, ParseException error) {
                if(assignments != null){
                    mAdapter.clear();
                    mAdapter.addAll(assignments);
                }
                else{
                    Toast.makeText(getActivity(),"Something went wrong! Try again!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            if (view.isShown()) { // Some reason this would normally be called twice unless this is checked
                String date = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
                java.util.Date utilDate;

                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                    utilDate = formatter.parse(date);
                    saveAssignment(utilDate);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
