package com.ccdevelopment.mywentworthcoop;

        import java.util.ArrayList;
        import java.util.List;

        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.EditText;
        import android.widget.Switch;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.parse.ParseException;
        import com.parse.ParseObject;
        import com.parse.ParseUser;
        import com.parse.SaveCallback;

        import org.w3c.dom.Text;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<JournalPost> journalPostList = null;
    private ArrayList<JournalPost> arraylist;

    public ListViewAdapter(Context context,
                           List<JournalPost> journalPostList) {
        mContext = context;
        this.journalPostList = journalPostList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<JournalPost>();
        this.arraylist.addAll(journalPostList);
    }

    public class ViewHolder {
        TextView title;
        TextView description;
        TextView date;
        TextView username;
        Switch isPrivate;
    }

    @Override
    public int getCount() {
        return journalPostList.size();
    }

    @Override
    public JournalPost getItem(int position) {
        return journalPostList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_itemview, null);
            // Locate the TextViews in listview_item.xml
            holder.title = (TextView) view.findViewById(R.id.textViewTitle);
            holder.description = (TextView) view.findViewById(R.id.textViewDescription);
            holder.date = (TextView) view.findViewById(R.id.textViewDate);
            holder.username = (TextView) view.findViewById(R.id.textViewUser);
            holder.isPrivate = (Switch) view.findViewById(R.id.switchPrivate);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.title.setText(journalPostList.get(position).getTitle());
        holder.description.setText(journalPostList.get(position).getDescription()
                .substring(0, Math.min(journalPostList.get(position).getDescription().toString().length(), 40)) + "...");
        holder.date.setText(journalPostList.get(position).getDate());
        //holder.isPrivate.setChecked(journalPostList.get(position).getIsPrivate());
        String publicPrivate = journalPostList.get(position).getIsPrivate() == true ? "Private" : "Public";
        holder.username.setText(journalPostList.get(position).getUsername() + " [" + publicPrivate + "]");


        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
               /* // Send single item click data to SingleItemView Class
                Intent intent = new Intent(mContext, SingleItemView.class);
                // Pass all data rank
                intent.putExtra("rank",
                        (journalPostList.get(position).getTitle()));
                // Pass all data country
                intent.putExtra("country",
                        (journalPostList.get(position).getDescription()));
                // Pass all data population
                intent.putExtra("population",
                        (journalPostList.get(position).getDate()));
                // Start SingleItemView Class
                mContext.startActivity(intent);*/
                /*LayoutInflater factory = LayoutInflater.from(ViewPagerActivity);
                final View textEntryView = factory.inflate(R.layout.dialog_newpost, null);

                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText2);
                final Switch switchPrivate = (Switch) textEntryView.findViewById(R.id.switchPrivate);
                input1.setText(journalPostList.get(position).getTitle());
                input2.setText(journalPostList.get(position).getDescription());
                switchPrivate.setChecked(journalPostList.get(position).getIsPrivate());


                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("New Post")
                        .setView(textEntryView)
                        .setPositiveButton("Save",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        ParseObject post = new ParseObject("Post");
                                        post.put("Title", input1.getText().toString());
                                        post.put("Message", input2.getText().toString());
                                        post.put("isPrivate", switchPrivate.isChecked());
                                        post.put("user", ParseUser.getCurrentUser());
                                        post.put("username", ParseUser.getCurrentUser().getUsername());

                                        // Save the post and return
                                        post.saveInBackground(new SaveCallback() {
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    getActivity().finish();
                                                    Intent myIntent = new Intent(getActivity(), ViewPagerActivity.class);
                                                    myIntent.putExtra("FirstTab", 1);
                                                    startActivity(myIntent);
                                                } else {
                                                    Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                    }
                                });
                alert.show();*/ // todo make this shit work



            }
        });

        return view;
    }
}