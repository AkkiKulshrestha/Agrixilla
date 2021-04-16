package agrixilla.in.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import agrixilla.in.R;
import agrixilla.in.models.SalesUploadModel;
import agrixilla.in.utils.UtilitySharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SalesReturnsListAdapter extends RecyclerView.Adapter<SalesReturnsListAdapter.MyViewHolder> {
    private final ArrayList<SalesUploadModel> uploadList;
    private LayoutInflater mInflater;
    private Context context;
    String UserId, Id;
    public ProgressDialog mProgressDialog;
    TextView row_download_ezzyreport_content, row_otp_ezzyreport_content, row_date_ezzyreport_content, date, otp, link;
    String filenames,file_paths;
    String Otp;
    public SalesReturnsListAdapter(ArrayList<SalesUploadModel> dealList) {
        this.uploadList = dealList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_file_upload_content, parent, false);

        context = parent.getContext();
        UserId = UtilitySharedPreferences.getPrefs(context, "User_id");
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SalesUploadModel dealsModel = uploadList.get(position);

        String finalDate = null;
        try {
            finalDate = new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(dealsModel.getDate()));
        } catch (ParseException e) {
            Log.d("Exception", e.toString());
        }

        holder.row_upload_date.setText(finalDate);
        holder.row_uploaded_file_link.setText(dealsModel.getFileLink());
        holder.row_upload_filename.setText(dealsModel.getFileName()+"."+dealsModel.getFileExtension());


        Linkify.addLinks(holder.row_uploaded_file_link, Linkify.WEB_URLS);
        holder.row_uploaded_file_link.setMovementMethod(LinkMovementMethod.getInstance());


    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView row_upload_date,row_upload_filename,row_uploaded_file_link;

        public MyViewHolder(View view) {
            super(view);

            date = (TextView) view.findViewById(R.id.date);
            row_upload_date = (TextView) view.findViewById(R.id.row_upload_date);
            row_uploaded_file_link = (TextView)  view.findViewById(R.id.row_uploaded_file_link);
            row_upload_filename = (TextView) view.findViewById(R.id.row_upload_filename);






        }
    }



}



