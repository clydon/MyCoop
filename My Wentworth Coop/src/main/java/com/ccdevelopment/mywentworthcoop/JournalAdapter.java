package com.ccdevelopment.mywentworthcoop;

        import java.text.SimpleDateFormat;
        import java.util.List;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

public class JournalAdapter extends ArrayAdapter<JournalPost> {

    Context mContext;
    private List<JournalPost> journalPostList;

    public JournalAdapter(Context context, List<JournalPost> journalPostList) {
        super(context,R.layout.journal_row_item, journalPostList);
        this.mContext = context;
        this.journalPostList = journalPostList;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.journal_row_item, null);
        }

        JournalPost journalPost = journalPostList.get(position);

        TextView titleView = (TextView) convertView.findViewById(R.id.textViewTitle);
        titleView.setText(journalPost.getTitle());

        TextView descriptionView = (TextView) convertView.findViewById(R.id.textViewDescription);
        descriptionView.setText(journalPost.getDescription());

        TextView dateView = (TextView) convertView.findViewById(R.id.textViewDate);
        dateView.setText(new SimpleDateFormat("MMM d, h:mm a").format(journalPost.getUpdatedAt()));

        TextView usernameView = (TextView) convertView.findViewById(R.id.textViewUser);
        usernameView.setText(journalPost.getUsername());

        if(journalPost.getIsPrivate()){
            usernameView.setText(usernameView.getText()+ " [private]");
        }else{
            usernameView.setText(usernameView.getText()+ " [public]");
        }

        return convertView;
    }
}