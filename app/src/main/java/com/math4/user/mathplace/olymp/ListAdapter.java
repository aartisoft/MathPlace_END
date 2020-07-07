//package com.math4.user.mathplace.olymp;
//
//import android.content.Context;
//import android.graphics.Typeface;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.TextView;
//
//import com.math4.user.mathplace.R;
//
//import java.util.HashMap;
//import java.util.List;
//
//public class ListAdapter extends BaseExpandableListAdapter {
//
//    private Context context;
//    private List<String> expListTitle;
//    private HashMap<String, List<String>> expListDetail;
//
//    public ListAdapter(Context context, List<String> expListTitle,
//                       HashMap<String, List<String>> expListDetail) {
//        this.context = context;
//        this.expListTitle = expListTitle;
//        this.expListDetail = expListDetail;
//    }
//
//    @Override
//    public Object getChild(int listPosition, int expListPosition) {
//        return expListDetail.get(
//                expListTitle.get(listPosition)
//        ).get(expListPosition);
//    }
//
//    @Override
//    public long getChildId(int listPosition, int expandedListPosition) {
//        return expandedListPosition;
//    }
//
//    @Override
//    public View getChildView(int listPosition, final int expandedListPosition,
//                             boolean isLastChild, View convertView, ViewGroup parent) {
//        // получаем дочерний элемент
//        String expListText = (String) getChild(listPosition, expandedListPosition);
//        if (convertView == null) {
//            LayoutInflater mInflater = (LayoutInflater)
//                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = mInflater.inflate(R.layout.sections_item, null);
//        }
//        TextView expListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
//        expListTextView.setText(expListText);
//        return convertView;
//    }
//
//    @Override
//    public int getChildrenCount(int listPosition) {
//        return expListDetail.get(
//                expListTitle.get(listPosition)
//        ).size();
//    }
//
//    @Override
//    public Object getGroup(int listPosition) {
//        return expListTitle.get(listPosition);
//    }
//
//    @Override
//    public int getGroupCount() {
//        return expListTitle.size();
//    }
//
//    @Override
//    public long getGroupId(int listPosition) {
//        return listPosition;
//    }
//
//    @Override
//    public View getGroupView(int listPosition, boolean isExpanded,
//                             View convertView, ViewGroup parent) {
//        // получаем родительский элемент
//        String listTitle = (String) getGroup(listPosition);
//        String listThemes = String.valueOf(getChildrenCount(listPosition));
//        if (convertView == null) {
//            LayoutInflater mInflater = (LayoutInflater) context.
//                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = mInflater.inflate(R.layout.sections_group, null);
//        }
//        TextView listTitleTextView = (TextView) convertView
//                .findViewById(R.id.listTitle);
//        listTitleTextView.setTypeface(null, Typeface.BOLD);
//        listTitleTextView.setText(listTitle);
//        TextView listThemesTextView = (TextView) convertView
//                .findViewById(R.id.listThemes);
//        listThemesTextView.setTypeface(null, Typeface.BOLD);
//        listThemesTextView.setText(listThemes);
//        return convertView;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }
//
//    @Override
//    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
//        return true;
//    }
//}
