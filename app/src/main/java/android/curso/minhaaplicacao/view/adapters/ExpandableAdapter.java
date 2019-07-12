package android.curso.minhaaplicacao.view.adapters;

import android.content.Context;
import android.curso.minhaaplicacao.R;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableAdapter extends BaseExpandableListAdapter {
    private List<String> listGroup;
    private HashMap<String,List<String>> listData;
    private LayoutInflater inflater;

    public ExpandableAdapter(Context context,List<String> listGroup,HashMap<String,List<String>> listData ){
        this.listGroup = listGroup;
        this.listData = listData;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listData.get(listGroup.get(groupPosition)).size(); //pega o tamanho das sublistas dada a posição do grupo a qual pertence
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listData.get(listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderGroup holder;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.header_expandable_list_view, null);
            holder = new ViewHolderGroup();
            convertView.setTag(holder);

            holder.tvGroup = (TextView) convertView.findViewById(R.id.tvGroup);
        }
        else{
            holder = (ViewHolderGroup) convertView.getTag();
        }

        holder.tvGroup.setText(listGroup.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem holder;
        String val = (String) getChild(groupPosition, childPosition);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_expandable_list_view, null);
            holder = new ViewHolderItem();
            convertView.setTag(holder);

            holder.tvItem = (TextView) convertView.findViewById(R.id.tvItem);
        }
        else{
            holder = (ViewHolderItem) convertView.getTag();
        }

        holder.tvItem.setText(val);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    class ViewHolderGroup{
        TextView tvGroup;

    }

    class ViewHolderItem{
        TextView tvItem;

    }
}
