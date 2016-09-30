package org.partner.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.partner.R;

import java.util.ArrayList;


public class NavigationDrawer extends Fragment {

    private static String TAG = NavigationDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerRegisterUserAdapter mrRegisteradapter;
    private View containerView;
    private FragmentDrawerListener drawerListener;
    private ArrayList<String> menus=new ArrayList<String>();
    private Context mContext;
    private String[] mMenuRegisterNames;
    private Integer[] mMenuRegisterImages;
    private Integer[] mMenuRegisterImages_select;
    public NavigationDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }




    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
          recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        //LinearLayoutManager shows items in a vertical or horizontal scrolling list.
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mMenuRegisterNames = getResources().getStringArray(R.array.menu_list);



            TypedArray tArray = getResources().obtainTypedArray(R.array.menu_list);
            int count = tArray.length();
            mMenuRegisterImages = new Integer[count];
            for (int i = 0; i < mMenuRegisterImages.length; i++) {
                mMenuRegisterImages[i] = tArray.getResourceId(i, 0);
            }


            TypedArray tArray_select = getResources().obtainTypedArray(R.array.menu_list);
            mMenuRegisterImages_select = new Integer[count];
            for (int i = 0; i < mMenuRegisterImages_select.length; i++) {
                mMenuRegisterImages_select[i] = tArray_select.getResourceId(i, 0);
            }

            //Specify an Recycle view Adapter
            mrRegisteradapter = new NavigationDrawerRegisterUserAdapter(getActivity(), mMenuRegisterNames,mMenuRegisterImages,mMenuRegisterImages_select);
            recyclerView.setAdapter(mrRegisteradapter);


        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        ImageView slider_menu=(ImageView)toolbar.findViewById(R.id.imageView_menu);
        slider_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });




    }


    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
  private class NavigationDrawerRegisterUserAdapter extends RecyclerView.Adapter<NavigationDrawerRegisterUserAdapter.MyViewHolder> {

        private LayoutInflater inflater;
        private Context context;
        private ArrayList<String> al;
        private String[] menuss;
        private Integer[] mImages;
        private Integer[] mImages_select;
        public  int selected_item = -1;

        public NavigationDrawerRegisterUserAdapter(Context context,String[] menuss,Integer mImages[],Integer mImages_select[]) {
            this.context = context;

            this.al=al;
            this.menuss=menuss;
            this.mImages=mImages;
            this.mImages_select=mImages_select;
            inflater = LayoutInflater.from(context);

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.fragment_navigation_drawer_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
//        NavDrawerItem current = data.get(position);
            holder.profile.setText(menuss[position]);
            holder.img.setVisibility(View.VISIBLE);
            //holder.img.setImageResource(mImages[position]);

            if(position==1){
                holder.bg.setBackgroundResource(R.color.grey_divider_menu);
                holder.bg.setVisibility(View.VISIBLE);
            }else{
                holder.bg.setBackgroundResource(R.color.tranparent);
                holder.bg.setVisibility(View.GONE);
            }


            if(position==selected_item){
                holder.profile.setTextColor(context.getResources().getColor(R.color.blue_text_menu));
                holder.img.setVisibility(View.VISIBLE);

                // holder.img.setImageResource(mImages_select[position]);
            }else{
                holder.profile.setTextColor(context.getResources().getColor(R.color.menu_text_color));
                holder.img.setVisibility(View.VISIBLE);
             }


            holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected_item=position;
                    drawerListener.onDrawerItemSelected(v, position);
                    mDrawerLayout.closeDrawer(containerView);
                    notifyDataSetChanged();
                }
            });


        }

        @Override
        public int getItemCount() {
            return menuss.length;
        }

        //You can use card view for recycleview rows
        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView profile,lodge;
            ImageView img;
            LinearLayout mParentLayout;
            View bg;

            public MyViewHolder(final View itemView) {
                super(itemView);
                profile = (TextView) itemView.findViewById(R.id.menu_title);
                img=(ImageView) itemView.findViewById(R.id.imageView_menu);
                mParentLayout=(LinearLayout) itemView.findViewById(R.id.menu_layout);

                bg=(View) itemView.findViewById(R.id.title_border);

            }
        }
    }









}
