package ui;

import android.content.Context;

import com.bigkoo.svprogresshud.SVProgressHUD;

/**封装了滚动条,作为Frgament的组件之一
 * Created by cz on 2017-6-10.
 */

public class CustomerProgress  {

     SVProgressHUD progress;

   public  CustomerProgress(Context context)
    {
        progress=new SVProgressHUD(context);

    }
    public void showProgress()
    {
        if(progress!=null)
        {
            progress.showWithStatus("正在加载");
        }
    }

    public boolean isShowing()
    {
        if(progress!=null&&progress.isShowing())
        {
           return true;
        }
        return false;
    }


    public void closeProgress()
    {
        if (progress != null && progress.isShowing())
        {
            progress.dismiss();
        }
    }

}
