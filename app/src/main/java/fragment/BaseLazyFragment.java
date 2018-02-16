package fragment;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bigkoo.svprogresshud.SVProgressHUD;

import ui.CustomerProgress;

/**
 * Created by Administrator on 2017-6-10.
 */

public abstract  class BaseLazyFragment extends Fragment {

    private static final String TAG = "BaseLazyFragment";
    private boolean hasCreateView; //标记该fragment是否创建了View
    private boolean isFragmentVisible;//标记该Fragment是否为可见状态

    //保存布局
    View rootView;
    static CustomerProgress progress; //自定义滚动进度条


    @Nullable
    @Override
    final public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null)
        {
            rootView=inflater.inflate(getLayout(),container,false);
            progress=new CustomerProgress(getActivity());
            //判断是否需要Show滚动条
            if(isNeedShowProgress())
            {
                onCreateProgressView();
            }
            initView(rootView);
            return rootView;
        }
        return rootView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.w(getTag(), "setUserVisibleHint() -> isVisibleToUser: " + isVisibleToUser);
        if (rootView == null) {
            Log.w(getTag(), "setUserVisibleHint: ->rootView为null");
            return ;
        }
        hasCreateView = true;
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            Log.w(getTag(), "setUserVisibleHint: isVisbleToUser="+isVisibleToUser+" isFragmentVisible="+isFragmentVisible);
            return;
        }
        if (isFragmentVisible) {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
            Log.w(getTag(), "setUserVisibleHint: isVisbleToUser="+isVisibleToUser+" isFragmentVisible="+isFragmentVisible);
        }
        Log.w(getTag(), "setUserVisibleHint: isVisbleToUser="+isVisibleToUser+" isFragmentVisible="+isFragmentVisible);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.w(getTag(), "onViewCreated: getUserVisBleHint()= "+getUserVisibleHint()+"hasCreateview="+hasCreateView);
        if (!hasCreateView && getUserVisibleHint()) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
        }
    }

    private void initVariable() {
        hasCreateView = false;
        isFragmentVisible = false;
    }

    /**************************************************************
     *  自定义的回调方法，子类可根据需求重写
     *************************************************************/

    /**
     * 当前fragment可见状态发生变化时会回调该方法
     * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作，因为配合fragment的view复用机制，你不用担心在对控件操作中会报 null 异常
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected  void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            loadData();
        }else
        {
            Log.d(TAG,"该FragmentViewsibleChange ->"+isVisible);
        }

    }

    //子类实现加载布局
    public abstract int getLayout();

    //子类初始化View
    public abstract void initView(View view);

    //子类操作数据的方法；
    public abstract void loadData();

    //子类Fragment是否需要显示对话框,默认不显示
    public boolean isNeedShowProgress()
    {
        return false;
    }

    /**
     * 调用CustomerProgress封装的打开对话框方法
     */
    protected void onCreateProgressView()
    {
        if(progress!=null)
        {
            progress.showProgress();
        }
    }
    /**
     * 调用CustomerProgress封装的关闭对话框方法
     */
    public void closeProgress() {
        if(progress!=null)
        {
            progress.closeProgress();
            Log.d(TAG, "closeProgress: 关闭加载条");
        }
        Log.d(TAG, "closeProgress: null");
    }

}
