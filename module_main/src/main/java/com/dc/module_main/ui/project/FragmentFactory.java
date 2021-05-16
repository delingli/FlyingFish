package com.dc.module_main.ui.project;

import com.dc.baselib.mvvm.BaseFragment;

import java.util.HashMap;
import java.util.Map;

public class FragmentFactory {
    public static final int HOME_FOCUSONFRAGMENT_URL = 0;
    public static final int HOME_RECOMMENDEDFRAGMENT_URL = 1;
    public static final int HOME_ELECTROMAGNETICCOMPATIBILITYFRAGMENT_URL = 2;
    public static final int HOME_RADIOFREQUENCYDESIGN_URL = 3;
    public static final int HOME_SPISIMULATIONFRAGENT_URL = 4;
    public static final int HOME_EMBEDDEDFRAGMENT_URL = 5;
    public static final int HOME_MEASUREMENTFRAGMENT_URL = 6;
    public static final int HOME_RELIABILITYFRAGEMNET_URL = 7;
    public static final int HOME_MANUFACTURINGDESIGN_URL = 8;
    public static final int HOME_DEVICEENGINEERINGFRAGMENT_URL = 9;
    public static final int HOME_DEVELOPMENTMANAGERFRAGMENT_URL = 10;
    public static final int HOME_HARDWAREDESIGNFRAGMENT_URL = 11;
    public static final int HOME_PCBDESIGNFRAGMENT_URL = 12;
    private static Map<Integer, BaseFragment> mFragmentCache = new HashMap<Integer, BaseFragment>();

/*    public static Fragment createFragment(int pos) {
        BaseFragment fragment = mFragmentCache.get(pos);
        if (fragment != null) {
            return fragment;
        }
        switch (pos) {
            case HOME_FOCUSONFRAGMENT_URL:
                fragment = (BaseFragment) ARouter.getInstance().build(ArounterManager.HOME_FOCUSONFRAGMENT_URL).navigation();
                break;
            case HOME_RECOMMENDEDFRAGMENT_URL:
                fragment = (BaseFragment) ARouter.getInstance().build(ArounterManager.HOME_RECOMMENDEDFRAGMENT_URL).navigation();
                break;
            case HOME_ELECTROMAGNETICCOMPATIBILITYFRAGMENT_URL:
                fragment = (BaseFragment) ARouter.getInstance().build(ArounterManager.HOME_ELECTROMAGNETICCOMPATIBILITYFRAGMENT_URL).navigation();
                break;
            case HOME_HARDWAREDESIGNFRAGMENT_URL:
                fragment = (BaseFragment) ARouter.getInstance().build(ArounterManager.HOME_HARDWAREDESIGNFRAGMENT_URL).navigation();
                break;
            case HOME_RADIOFREQUENCYDESIGN_URL:
                fragment = (BaseFragment) ARouter.getInstance().

                        build(ArounterManager.HOME_RADIOFREQUENCYDESIGN_URL).

                        navigation();
                break;
            case HOME_SPISIMULATIONFRAGENT_URL:
                fragment = (BaseReactFragment) ARouter.getInstance().

                        build(ArounterManager.HOME_SPISIMULATIONFRAGENT_URL).

                        navigation();
                break;
            case HOME_EMBEDDEDFRAGMENT_URL:
                fragment = (BaseReactFragment) ARouter.getInstance().
                        build(ArounterManager.HOME_EMBEDDEDFRAGMENT_URL).
                        navigation();
                break;
            case HOME_MEASUREMENTFRAGMENT_URL:
                fragment = (BaseReactFragment) ARouter.getInstance().

                        build(ArounterManager.HOME_MEASUREMENTFRAGMENT_URL).

                        navigation();
                break;
            case HOME_RELIABILITYFRAGEMNET_URL:
                fragment = (BaseReactFragment) ARouter.getInstance().

                        build(ArounterManager.HOME_RELIABILITYFRAGEMNET_URL).

                        navigation();
                break;
            case HOME_MANUFACTURINGDESIGN_URL:
                fragment = (BaseReactFragment) ARouter.getInstance().

                        build(ArounterManager.HOME_MANUFACTURINGDESIGN_URL).

                        navigation();
                break;
            case HOME_DEVICEENGINEERINGFRAGMENT_URL:
                fragment = (BaseReactFragment) ARouter.getInstance().

                        build(ArounterManager.HOME_DEVICEENGINEERINGFRAGMENT_URL).

                        navigation();
                break;
            case HOME_DEVELOPMENTMANAGERFRAGMENT_URL:
                fragment = (BaseReactFragment) ARouter.getInstance().

                        build(ArounterManager.HOME_DEVELOPMENTMANAGERFRAGMENT_URL).

                        navigation();
                break;
            case HOME_PCBDESIGNFRAGMENT_URL:
                fragment = (BaseReactFragment) ARouter.getInstance().

                        build(ArounterManager.HOME_PCBDESIGNFRAGMENT_URL).

                        navigation();
                break;


        }
        mFragmentCache.put(pos, fragment);
        return fragment;
    }*/
}
