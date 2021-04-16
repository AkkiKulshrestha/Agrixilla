package agrixilla.in.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import agrixilla.in.R;
import agrixilla.in.models.ProductModel;

import java.util.ArrayList;

public class ProductsAdapter  extends BaseAdapter implements Filterable {

    public Context context;
    public ArrayList<ProductModel> productList;
    public ArrayList<ProductModel> orig;

    public ProductsAdapter(Context context, ArrayList<ProductModel> productList) {
        super();
        this.context = context;
        this.productList = productList;
    }


    public class EmployeeHolder
    {
        TextView name;
        TextView age;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ProductModel> results = new ArrayList<ProductModel>();
                if (orig == null)
                    orig = productList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final ProductModel g : orig) {
                            if (g.getProduct_name().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                productList = (ArrayList<ProductModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EmployeeHolder holder;
        if(convertView==null)
        {
            convertView=LayoutInflater.from(context).inflate(R.layout.row_list_card, parent, false);
            holder=new EmployeeHolder();
            holder.name=(TextView) convertView.findViewById(R.id.row_item_name);
            //holder.age=(TextView) convertView.findViewById(R.id.txtAge);
            convertView.setTag(holder);
        }
        else
        {
            holder=(EmployeeHolder)convertView.getTag();
        }

        holder.name.setText(productList.get(position).getProduct_category()+" - "+productList.get(position).getProduct_name());
        //holder.age.setText(String.valueOf(productList.get(position).getAge()));

        return convertView;

    }

}
